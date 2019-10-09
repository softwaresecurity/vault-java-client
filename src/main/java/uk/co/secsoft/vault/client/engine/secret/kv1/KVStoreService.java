package uk.co.secsoft.vault.client.engine.secret.kv1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import uk.co.secsoft.vault.client.exception.KVStoreException;
import uk.co.secsoft.vault.client.utils.HttpGateway;
import uk.co.secsoft.vault.domain.auth.response.AuthToken;
import uk.co.secsoft.vault.domain.config.VaultConfiguration;
import uk.co.secsoft.vault.domain.engine.secret.SecretStore;
import uk.co.secsoft.vault.domain.token.Token;

import java.io.IOException;
import java.util.Map;

import static uk.co.secsoft.vault.client.VaultConstants.VAULT_TOKEN_HEADER;

public class KVStoreService {
  private static final String URLFORMAT = "%s/%s/%s";
  private ObjectMapper objectMapper = new ObjectMapper();

  private final VaultConfiguration vaultConfig;
  private final HttpGateway httpGateway;

  public KVStoreService(VaultConfiguration vaultConfiguration, HttpGateway httpGateway) {
    this.vaultConfig = vaultConfiguration;
    this.httpGateway = httpGateway;
  }

  public SecretStore getStore(String path, Token authToken) {
    SecretStore secretStore = null;
    HttpResponse response = null;
    String getUrl = String.format(URLFORMAT, vaultConfig.getBaseUrl(), vaultConfig.getApiVersion(), path);
    HttpGet secretRequest = new HttpGet(getUrl);
    secretRequest.addHeader(VAULT_TOKEN_HEADER, String.valueOf(authToken.getToken()));

    try {
      response = httpGateway.getHttpClient().execute(secretRequest);

      JsonNode authResponse = objectMapper.readTree(response.getEntity().getContent());
      JsonNode kvDataNode  = authResponse.path("data");
      Map<String, String> kvMap = objectMapper.convertValue(kvDataNode, new TypeReference<Map<String, String>>() {});

      secretStore = new SecretStore(kvMap);
    } catch (Exception e) {
      throw new KVStoreException("Error in retrieving kv store");
    }
    finally {
      HttpClientUtils.closeQuietly(response);
    }
    return secretStore;
  }

  public void persistSecrets(String path, SecretStore secretStore, Token token) {
    HttpResponse response = null;
    String createUrl = String.format(URLFORMAT, vaultConfig.getBaseUrl(), vaultConfig.getApiVersion(), path);
    HttpPost secretCreateRequest = new HttpPost(createUrl);
    secretCreateRequest.addHeader(VAULT_TOKEN_HEADER, String.valueOf(token.getToken()));

    try {

      String jsonData = objectMapper.writeValueAsString(secretStore.getData());
      secretCreateRequest.setEntity(new StringEntity(jsonData, ContentType.APPLICATION_JSON));
      response = httpGateway.getHttpClient().execute(secretCreateRequest);
    } catch (Exception e) {
      throw new KVStoreException("Error in retrieving kv store");
    }
    finally {
      HttpClientUtils.closeQuietly(response);
    }
  }
}
