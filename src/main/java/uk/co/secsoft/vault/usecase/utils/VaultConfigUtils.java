package uk.co.secsoft.vault.usecase.utils;

import uk.co.secsoft.vault.domain.auth.method.AuthModel;
import uk.co.secsoft.vault.domain.auth.method.LdapUserPass;
import uk.co.secsoft.vault.domain.config.VaultConfiguration;

public class VaultConfigUtils {

  public static final String VAULT_BASE_URL_KEY = "VAULT_BASE_URL";
  public static final String VAULT_AUTH_PATH_KEY = "VAULT_AUTH_PATH";
  public static final String VAULT_USR_KEY = "VAULT_USR";
  public static final String VAULT_PWD_KEY = "VAULT_PWD";
  public static final String VAULT_TOKEN_KEY = "VAULT_TOKEN";

  public static VaultConfiguration getVaultConfig() {
    return new VaultConfiguration(
        System.getenv(VAULT_BASE_URL_KEY),
        null,
        null);
  }

  public static AuthModel getLDAPAuth() {
    return new LdapUserPass(System.getenv(VAULT_AUTH_PATH_KEY), System.getenv(VAULT_USR_KEY), System.getenv(VAULT_PWD_KEY));
  }
}
