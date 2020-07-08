package uk.co.secsoft.vault.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.io.IOUtils;
import uk.co.secsoft.vault.client.engine.secret.KVFacade;
import uk.co.secsoft.vault.client.engine.secret.kv1.KVStoreService;
import uk.co.secsoft.vault.client.utils.HttpGateway;
import uk.co.secsoft.vault.domain.config.VaultConfiguration;
import uk.co.secsoft.vault.domain.engine.secret.SecretStore;
import uk.co.secsoft.vault.domain.token.Token;
import uk.co.secsoft.vault.usecase.utils.CommonUtils;
import uk.co.secsoft.vault.usecase.utils.EncryptedTokenSerializer;
import uk.co.secsoft.vault.usecase.utils.SimpleAESUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static uk.co.secsoft.vault.usecase.utils.VaultConfigUtils.getVaultConfig;

public class GetSecretJson {
  private final VaultConfiguration vaultConfig;
  private final HttpGateway httpGateway;
  public GetSecretJson(VaultConfiguration vaultConfig, HttpGateway httpGateway) {
    this.vaultConfig = vaultConfig;
    this.httpGateway = httpGateway;
  }

  public static void main(String[] args) {
    new GetSecretJson(getVaultConfig(), new HttpGateway(null, null)).startReadingSecretStoreFromVault();
  }

  public void startReadingSecretStoreFromVault() {
    Token vaultToken = CommonUtils.getVaultToken(vaultConfig, httpGateway);
    KVFacade kvFacade = new KVFacade(new KVStoreService(vaultConfig, httpGateway));

    List<String> secretStoreRoots = readSecretRootFromConfig();
    for (String secretStoreRoot: secretStoreRoots) {
      System.out.println("Starting to read: " + secretStoreRoot);
      SecretStore secretStore = kvFacade.getSecretStore(secretStoreRoot, vaultToken);
      writeToFile(secretStore);
      System.out.println("Finished reading: " + secretStoreRoot);
    }
  }

  public void startReadingSecretStoreFromJsonFile() {
    Token vaultToken = CommonUtils.getVaultToken(vaultConfig, httpGateway);
    KVFacade kvFacade = new KVFacade(new KVStoreService(vaultConfig, httpGateway));
    try(FileInputStream inputStream = new FileInputStream("secrets-rps-stage.json")) {
      SecretStore secretStore = getObjectMapper().readValue(inputStream, SecretStore.class);
      System.out.println("file reading finished");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void writeToFile(SecretStore secretStore) {
    String json = serialize(secretStore);
    try {
      Files.write(Paths.get(getTimePrefix()+"-"+secretStore.getName()+ ".json"), json.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String serialize(SecretStore secretStore) {
    try {
      return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(secretStore);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private ObjectMapper getObjectMapper() {
    String encryptionKey = System.getenv("TOKEN_ENC_KEY");
    SimpleAESUtils mySuperSecret = new SimpleAESUtils(encryptionKey);
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(Token.class, new EncryptedTokenSerializer(mySuperSecret));
    objectMapper.registerModule(module);
    return objectMapper;
  }

  private List<String> readSecretRootFromConfig() {
    String fileName = "secret-root.dat";
    try(InputStream inputStream = new FileInputStream(fileName)) {
      return IOUtils.readLines(inputStream, "utf-8");
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String getTimePrefix() {
    LocalDateTime dateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss SS");
    return dateTime.format(formatter);
  }
}
