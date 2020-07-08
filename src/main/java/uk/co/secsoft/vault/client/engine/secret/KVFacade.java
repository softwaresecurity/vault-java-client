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

  public SecretStore getSecretStore(String path, Token vaultToken) {
    return kvStoreService.getSecretStore(path, vaultToken);
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
