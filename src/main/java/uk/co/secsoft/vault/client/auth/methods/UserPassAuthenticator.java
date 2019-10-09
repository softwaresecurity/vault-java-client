package uk.co.secsoft.vault.client.auth.methods;

import uk.co.secsoft.vault.client.auth.Authenticator;
import uk.co.secsoft.vault.client.exception.VaultAuthException;
import uk.co.secsoft.vault.domain.auth.method.AuthModel;
import uk.co.secsoft.vault.domain.auth.method.UserPass;
import uk.co.secsoft.vault.domain.auth.response.AuthToken;

public class UserPassAuthenticator implements Authenticator {

  public UserPassAuthenticator() {
  }

  @Override
  public AuthToken authenticate(AuthModel authModel) throws VaultAuthException {
    return null;
  }
}
