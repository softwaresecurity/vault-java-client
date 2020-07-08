package uk.co.secsoft.vault.usecase.utils;

import org.apache.commons.lang3.StringUtils;
import uk.co.secsoft.vault.client.auth.AuthFacade;
import uk.co.secsoft.vault.client.utils.HttpGateway;
import uk.co.secsoft.vault.domain.config.VaultConfiguration;
import uk.co.secsoft.vault.domain.token.Token;

import static uk.co.secsoft.vault.usecase.utils.VaultConfigUtils.VAULT_TOKEN_KEY;

public class CommonUtils {
  public static Token getVaultToken(VaultConfiguration vaultConfig, HttpGateway httpGateway) {
    String vaultToken  = System.getenv(VAULT_TOKEN_KEY);
    if(StringUtils.isBlank(vaultToken)) {
      AuthFacade authFacade = new AuthFacade(vaultConfig, httpGateway);
      return authFacade.login(VaultConfigUtils.getLDAPAuth()).getToken();
    }
    else {
      return new Token(vaultToken);
    }
  }
}
