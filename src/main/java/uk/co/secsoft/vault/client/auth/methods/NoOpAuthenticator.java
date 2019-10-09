package uk.co.secsoft.vault.client.auth.methods;

import uk.co.secsoft.vault.client.auth.Authenticator;
import uk.co.secsoft.vault.client.exception.VaultAuthException;
import uk.co.secsoft.vault.domain.auth.method.AuthModel;
import uk.co.secsoft.vault.domain.auth.response.AuthToken;

import static uk.co.secsoft.vault.client.utils.ErrorMessages.UNSUPPORTED_OPS;

public class NoOpAuthenticator implements Authenticator {

  @Override
  public AuthToken authenticate(AuthModel authModel) throws VaultAuthException {
    throw new VaultAuthException(UNSUPPORTED_OPS);
  }
}
