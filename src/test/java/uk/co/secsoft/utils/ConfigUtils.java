package uk.co.secsoft.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import uk.co.secsoft.vault.client.auth.AuthFacade;
import uk.co.secsoft.vault.client.engine.secret.KVFacade;
import uk.co.secsoft.vault.client.engine.secret.kv1.KVStoreService;
import uk.co.secsoft.vault.client.utils.HttpGateway;
import uk.co.secsoft.vault.domain.auth.method.AuthModel;
import uk.co.secsoft.vault.domain.auth.method.LdapUserPass;
import uk.co.secsoft.vault.domain.auth.response.AuthToken;
import uk.co.secsoft.vault.domain.config.VaultConfiguration;
import uk.co.secsoft.vault.domain.engine.secret.SecretStore;
import uk.co.secsoft.vault.domain.token.Token;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
      Map<String, String> secretStoreMap = new HashMap<>();
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
      secretStoreMap.put(keyName, ApiKeyGenerator.generateKey(keyLength));
      configUtils.updateStore(secretPath, new SecretStore(secretStoreMap), token);

    });
    System.out.println("done!!!");
  }

  private void updateStore(String storePath, SecretStore secretStore, AuthToken token) {
    KVFacade kvFacade = new KVFacade(new KVStoreService(vaultConfig, httpGateway));
    kvFacade.updateStore(storePath, secretStore, token.getToken());
  }

  private SecretStore getSecretStore(String secretPath, Token token) {
    KVFacade kvFacade = new KVFacade(new KVStoreService(vaultConfig, httpGateway));
    return kvFacade.getStore(secretPath, token);
  }

  private AuthToken getAuthToken() {
    AuthFacade authFacade = new AuthFacade(vaultConfig, httpGateway);
    return authFacade.login(getLDAPAuth());
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

  private static VaultConfiguration getVaultConfig() {
    return new VaultConfiguration(
        System.getProperty("VAULT_BASE_URL"),
        null,
        null);
  }

  private static AuthModel getLDAPAuth() {
    return new LdapUserPass(System.getProperty("VAULT_AUTH_PATH"), System.getProperty("VAULT_USR"), System.getProperty("VAULT_PWD"));
  }
}
