package uk.co.secsoft.vault.client.auth;

import uk.co.secsoft.vault.client.auth.methods.LdapAuthenticator;
import uk.co.secsoft.vault.client.auth.methods.NoOpAuthenticator;
import uk.co.secsoft.vault.client.auth.methods.UserPassAuthenticator;
import uk.co.secsoft.vault.client.utils.HttpGateway;
import uk.co.secsoft.vault.domain.auth.method.AuthMethod;
import uk.co.secsoft.vault.domain.auth.method.AuthModel;
import uk.co.secsoft.vault.domain.config.VaultConfiguration;

class AuthFactory {
  private final HttpGateway httpGateway;
  private final VaultConfiguration configuration;

  AuthFactory(HttpGateway httpGateway, VaultConfiguration configuration) {
    this.httpGateway = httpGateway;
    this.configuration = configuration;
  }

  Authenticator getAuthenticator(AuthMethod authMethod) {
    Authenticator authenticator;
    switch (authMethod) {
      case LDAP:
        authenticator = new LdapAuthenticator(httpGateway, configuration);
        break;
      case USERNAME_PASSWORD:
        authenticator = new UserPassAuthenticator();
        break;
      default:
        authenticator = new NoOpAuthenticator();
    }
    return authenticator;
  }

}
