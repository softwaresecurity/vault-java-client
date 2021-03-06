package uk.co.secsoft.vault.usecase.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import uk.co.secsoft.vault.client.auth.AuthFacade;
import uk.co.secsoft.vault.client.engine.secret.KVFacade;
import uk.co.secsoft.vault.client.engine.secret.kv1.KVStoreService;
import uk.co.secsoft.vault.client.utils.HttpGateway;
import uk.co.secsoft.vault.domain.auth.response.AuthToken;
import uk.co.secsoft.vault.domain.config.VaultConfiguration;
import uk.co.secsoft.vault.domain.engine.secret.SecretStore;
import uk.co.secsoft.vault.domain.token.Token;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.co.secsoft.vault.usecase.utils.VaultConfigUtils.getVaultConfig;

/**
 *
 */
public class ConfigUtils {

  private final VaultConfiguration vaultConfig;
  private final HttpGateway httpGateway;

  public ConfigUtils(VaultConfiguration vaultConfig, HttpGateway httpGateway) {
    this.vaultConfig = vaultConfig;
    this.httpGateway = httpGateway;
  }


  public static void main(String[] args) {
    ConfigUtils configUtils = new ConfigUtils(getVaultConfig(), new HttpGateway(null));
    AuthToken token = configUtils.getAuthToken();
    List<String> todoList = configUtils.readConfig();

    todoList.forEach(actions-> {
      Map<String, Token> secretStoreMap = new HashMap<>();
      String[] actionsToken = StringUtils.split(actions, ",");
      String keyName = actionsToken[1];
      String secretPath = System.getProperty("SECRETS_ROOT")+ actionsToken[2];
      String generatorName = actionsToken[3];
      String generatorParams = actionsToken[4];
      int keyLength = Integer.parseInt(generatorParams.split(":")[1]);

      SecretStore secretStore = configUtils.getSecretStore(secretPath, token.getToken());
      if (secretStore != null && secretStore.getData() != null) {
        secretStoreMap.putAll(secretStore.getData());
      }
      secretStoreMap.put(keyName, new Token(ApiKeyGenerator.generateKey(keyLength)));
      //configUtils.updateStore(secretPath, new SecretStore(secretStoreMap), token);

    });
    System.out.println("done!!!");
  }

  private void updateStore(String storePath, SecretStore secretStore, AuthToken token) {
    KVFacade kvFacade = new KVFacade(new KVStoreService(vaultConfig, httpGateway));
    kvFacade.updateStore(storePath, secretStore, token.getToken());
  }

  private SecretStore getSecretStore(String secretPath, Token token) {
    KVFacade kvFacade = new KVFacade(new KVStoreService(vaultConfig, httpGateway));
    return kvFacade.getSecretStore(secretPath, token);
  }

  private AuthToken getAuthToken() {
    AuthFacade authFacade = new AuthFacade(vaultConfig, httpGateway);
    return authFacade.login(VaultConfigUtils.getLDAPAuth());
  }

  private List<String> readConfig() {
    String fileName = "release-10.config";
    File csvFile = new File(getClass().getClassLoader().getResource(fileName).getFile());
    try {
      return FileUtils.readLines(csvFile, "utf-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }
}
