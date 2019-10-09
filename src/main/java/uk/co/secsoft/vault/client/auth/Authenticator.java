package uk.co.secsoft.vault.client.auth;

import uk.co.secsoft.vault.client.exception.VaultAuthException;
import uk.co.secsoft.vault.domain.auth.method.AuthModel;
import uk.co.secsoft.vault.domain.auth.response.AuthToken;

public interface Authenticator {
  AuthToken authenticate(AuthModel authModel) throws VaultAuthException;
}
