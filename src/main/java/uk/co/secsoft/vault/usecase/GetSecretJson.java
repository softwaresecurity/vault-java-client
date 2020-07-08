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
import uk.co.secsoft.vault.usecase.utils.SimpleAESUtils;
import uk.co.secsoft.vault.usecase.utils.TokenSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static uk.co.secsoft.vault.usecase.utils.VaultConfigUtils.getVaultConfig;

public class GetSecretJson {
  private final VaultConfiguration vaultConfig;
  private final HttpGateway httpGateway;
  private final SimpleAESUtils simpleAESUtils;
  private final ObjectMapper objectMapper;
  public GetSecretJson(VaultConfiguration vaultConfig, HttpGateway httpGateway, SimpleAESUtils simpleAESUtils, ObjectMapper objectMapper) {
    this.vaultConfig = vaultConfig;
    this.httpGateway = httpGateway;
    this.simpleAESUtils = simpleAESUtils;
    this.objectMapper = objectMapper;
  }

  public static void main(String[] args) {
    String encryptionKey = System.getenv("TOKEN_ENC_KEY");
    SimpleAESUtils mySuperSecret = new SimpleAESUtils(encryptionKey);
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(Token.class, new TokenSerializer(mySuperSecret));
    objectMapper.registerModule(module);

    new GetSecretJson(getVaultConfig(), new HttpGateway(null, null), mySuperSecret, objectMapper).startProcessing();
  }

  public void startProcessing() {
    Token vaultToken = CommonUtils.getVaultToken(vaultConfig, httpGateway);
    List<String> secretStoreRoots = readSecretRootFromConfig();

    KVFacade kvFacade = new KVFacade(new KVStoreService(vaultConfig, httpGateway));
    for (String secretStoreRoot: secretStoreRoots) {
      System.out.println("Processing secret root: "+ secretStoreRoot);
      List<String> allSecretPaths = kvFacade.listStoresPath(secretStoreRoot, vaultToken);
      String json = getSecretJson(kvFacade, allSecretPaths, vaultToken);
      System.out.println(json);
    }

  }
  public String getSecretJson(KVFacade kvFacade, List<String> secretPaths, Token token) {
    SecretStore secretStores = kvFacade.getStore(secretPaths.get(0), token);

    try {
      return objectMapper.writeValueAsString(secretStores.getData());
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private List<String> readSecretRootFromConfig() {
    String fileName = "secret-root.dat";
    try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
      return IOUtils.readLines(inputStream, "utf-8");
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
