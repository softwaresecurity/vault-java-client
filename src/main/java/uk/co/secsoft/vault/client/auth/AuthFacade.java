package uk.co.secsoft.vault.client.auth;

import uk.co.secsoft.vault.client.utils.HttpGateway;
import uk.co.secsoft.vault.domain.auth.method.AuthModel;
import uk.co.secsoft.vault.domain.auth.response.AuthToken;
import uk.co.secsoft.vault.domain.config.VaultConfiguration;

public class AuthFacade {
  private final VaultConfiguration vaultConfiguration;
  private final HttpGateway httpGateway;

  public AuthFacade(VaultConfiguration vaultConfiguration, HttpGateway httpGateway) {
    this.vaultConfiguration = vaultConfiguration;
    this.httpGateway = httpGateway;
  }

  public AuthToken login(AuthModel authModel) {
    Authenticator authenticator = new AuthFactory(httpGateway, vaultConfiguration).getAuthenticator(authModel.getAuthMethod());
    AuthToken authToken = authenticator.authenticate(authModel);
    return authToken;
  }
}
