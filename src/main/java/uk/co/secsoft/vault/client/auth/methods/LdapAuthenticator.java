package uk.co.secsoft.vault.client.auth.methods;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import uk.co.secsoft.vault.client.auth.Authenticator;
import uk.co.secsoft.vault.client.exception.VaultAuthException;
import uk.co.secsoft.vault.client.utils.HttpGateway;
import uk.co.secsoft.vault.domain.auth.method.AuthModel;
import uk.co.secsoft.vault.domain.auth.method.LdapUserPass;
import uk.co.secsoft.vault.domain.auth.response.AuthToken;
import uk.co.secsoft.vault.domain.config.VaultConfiguration;

import static uk.co.secsoft.vault.domain.auth.AuthConstants.AUTH_PATH_LDAP;

public class LdapAuthenticator implements Authenticator {
  private final VaultConfiguration configuration;
  private final HttpGateway httpGateway;

  public LdapAuthenticator(HttpGateway httpGateway, VaultConfiguration config) {
    this.httpGateway = httpGateway;
    this.configuration = config;

  }

  @Override
  public AuthToken authenticate(AuthModel authModel) throws VaultAuthException {

    AuthToken authToken = null;
    HttpPost httpPost = new HttpPost(configuration.getBaseUrl() + getAuthPath(authModel));
    StringEntity stringEntity = new StringEntity(authModel.getAuthJson(), ContentType.APPLICATION_JSON);
    httpPost.setEntity(stringEntity);

    try {
      HttpResponse response = httpGateway.getHttpClient().execute(httpPost);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode authResponse = objectMapper.readTree(response.getEntity().getContent());
      JsonNode authNode  = authResponse.path("auth");
      authToken = objectMapper.convertValue(authNode, AuthToken.class);
    }
    catch (Exception e) {
     throw new VaultAuthException("LDAP authentication failed.");
    }
    return authToken;
  }

  private String getAuthPath(AuthModel authModel) {
    LdapUserPass ldapUserPass = (LdapUserPass) authModel;
    return String.format(AUTH_PATH_LDAP, configuration.getApiVersion(), ldapUserPass.getAuthPath(), ldapUserPass.getUsername());
  }
}
