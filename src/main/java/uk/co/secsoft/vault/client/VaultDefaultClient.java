package uk.co.secsoft.vault.client;

import org.apache.http.client.HttpClient;
import uk.co.secsoft.vault.domain.auth.method.AuthModel;
import uk.co.secsoft.vault.domain.config.VaultConfiguration;

public class VaultDefaultClient {
  private final HttpClient httpClient;
  private final VaultConfiguration vaultConfiguration;
  private final AuthModel authModel;


  public VaultDefaultClient(HttpClient httpClient, VaultConfiguration vaultConfiguration, AuthModel authModel) {
    this.httpClient = httpClient;
    this.vaultConfiguration = vaultConfiguration;
    this.authModel = authModel;
  }
}
