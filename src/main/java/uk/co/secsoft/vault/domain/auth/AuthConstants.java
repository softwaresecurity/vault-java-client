package uk.co.secsoft.vault.domain.auth;

public class AuthConstants {
  private AuthConstants() {}

  public static final String AUTH_PATH_LDAP = "/%s/auth/%s/login/%s";

  public static final String AUTH_JSON_LDAP = "{\"password\": \"%s\"}";
}
