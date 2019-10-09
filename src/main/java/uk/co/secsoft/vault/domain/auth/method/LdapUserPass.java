package uk.co.secsoft.vault.domain.auth.method;

import lombok.Getter;

import javax.security.auth.DestroyFailedException;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.valueOf;
import static uk.co.secsoft.vault.domain.auth.AuthConstants.AUTH_JSON_LDAP;

@Getter
public class LdapUserPass implements AuthModel {
  private final String authPath;
  private final String username;
  private final char[] password;

  public LdapUserPass(String authPath, String username, String password) {
    this.authPath = authPath;
    this.username = checkNotNull(username);
    this.password = password.toCharArray();
  }

  @Override
  public AuthMethod getAuthMethod() {
    return AuthMethod.LDAP;
  }

  @Override
  public String getAuthJson() {
    return String.format(AUTH_JSON_LDAP, valueOf(password));
  }

  @Override
  public void destroy() throws DestroyFailedException {
    if (password != null) {
      Arrays.fill(password, ' ');
    }
  }
}
