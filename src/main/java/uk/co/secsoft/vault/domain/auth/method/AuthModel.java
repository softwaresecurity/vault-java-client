package uk.co.secsoft.vault.domain.auth.method;

import javax.security.auth.Destroyable;

public interface AuthModel extends Destroyable {
  public AuthMethod getAuthMethod();
  public String getAuthJson();
}
