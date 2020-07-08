package uk.co.secsoft.vault.client.engine.secret;

import uk.co.secsoft.vault.client.engine.secret.kv1.KVStoreService;
import uk.co.secsoft.vault.domain.engine.secret.SecretStore;
import uk.co.secsoft.vault.domain.token.Token;

import java.util.List;

public class KVFacade {
  private final KVStoreService kvStoreService;

  public KVFacade(KVStoreService kvStoreService) {
    this.kvStoreService = kvStoreService;
  }

  public List<String> listStoresPath(String rootLocation, Token vaultToken) {
    return kvStoreService.listStoresPath(rootLocation, vaultToken);
  }

  public SecretStore getStore(String path, Token vaultToken) {
    return kvStoreService.getStore(path, vaultToken);
  }

  public Token getToken(String path, String key, Token vaultToken) {
    SecretStore secretStore = getStore(path, vaultToken);
    if (secretStore.getData() != null && secretStore.getData().containsKey(key)) {
      return secretStore.getData().get(key);
    }
    return null;
  }

  public void createStore(String path, SecretStore secretStore, Token vaultToken) {
     kvStoreService.persistSecrets(path, secretStore, vaultToken);
  }

  public void updateStore(String path, SecretStore secretStore, Token vaultToken) {
//    Map<String, String> finalStore = new HashMap<>();
//    SecretStore existingData = kvStoreService.getStore(path, vaultToken);
//    if(existingData.getData() != null) {
//      finalStore.putAll(existingData.getData());
//    }
//    finalStore.putAll(secretStore.getData());
//    kvStoreService.persistSecrets(path, new SecretStore(finalStore), vaultToken);
  }
}
