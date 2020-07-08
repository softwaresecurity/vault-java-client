package uk.co.secsoft.vault.client.engine.secret.kv1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import uk.co.secsoft.vault.client.exception.KVStoreException;
import uk.co.secsoft.vault.client.utils.HttpGateway;
import uk.co.secsoft.vault.domain.config.VaultConfiguration;
import uk.co.secsoft.vault.domain.engine.secret.SecretStore;
import uk.co.secsoft.vault.domain.token.Token;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static uk.co.secsoft.vault.client.VaultConstants.VAULT_TOKEN_HEADER;

public class KVStoreService {
  private static final String URL_FORMAT = "%s/%s/%s";
  public static final String URL_PATH_SEPARATOR = "/";
  private ObjectMapper objectMapper = new ObjectMapper();

  private final VaultConfiguration vaultConfig;
  private final HttpGateway httpGateway;

  public KVStoreService(VaultConfiguration vaultConfiguration, HttpGateway httpGateway) {
    this.vaultConfig = vaultConfiguration;
    this.httpGateway = httpGateway;
  }

  public SecretStore getSecretStore(String path, Token vaultToken) {
    String rootStoreName = getStoreNameFromPath(path);
    SecretStore rootStore = new SecretStore();
    rootStore.setName(rootStoreName);
    populateStore(path, vaultToken, rootStore);
    return rootStore;
  }

  public void persistSecrets(String path, SecretStore secretStore, Token token) {
    HttpResponse response = null;
    String createUrl = String.format(URL_FORMAT, vaultConfig.getBaseUrl(), vaultConfig.getApiVersion(), path);
    HttpPost secretCreateRequest = new HttpPost(createUrl);
    secretCreateRequest.addHeader(VAULT_TOKEN_HEADER, String.valueOf(token.getToken()));

    try {

      String jsonData = objectMapper.writeValueAsString(secretStore.getData());
      secretCreateRequest.setEntity(new StringEntity(jsonData, ContentType.APPLICATION_JSON));
      response = httpGateway.getClient().execute(secretCreateRequest);
    } catch (Exception e) {
      throw new KVStoreException("Error in retrieving kv store", e);
    }
    finally {
      HttpClientUtils.closeQuietly(response);
    }
  }

  private SecretStore getStoreData(String path, Token vaultToken) {
    String getUrl = String.format(URL_FORMAT, vaultConfig.getBaseUrl(), vaultConfig.getApiVersion(), path);
    try {
      URIBuilder uriBuilder = new URIBuilder(getUrl);
      JsonNode kvDataNode  = getDataNode(uriBuilder.build(), vaultToken);

      return objectMapper.convertValue(kvDataNode, SecretStore.class);
    }
    catch (Exception e) {
      throw new KVStoreException("Error in retrieving kv store", e);
    }
  }

  private String getStoreNameFromPath(String path) {
    String rootStoreName = StringUtils.removeEnd(path, URL_PATH_SEPARATOR);
    rootStoreName = StringUtils.contains(path, URL_PATH_SEPARATOR) ? StringUtils.substringAfterLast(rootStoreName, URL_PATH_SEPARATOR) : path;
    return rootStoreName;
  }

  private void populateStore(String rootPath, Token vaultToken, SecretStore rootStore) {
    String getUrl = String.format(URL_FORMAT, vaultConfig.getBaseUrl(), vaultConfig.getApiVersion(), rootPath);
    try {
      URIBuilder uriBuilder = new URIBuilder(getUrl).addParameter("list", "true");
      JsonNode kvDataNode  = getDataNode(uriBuilder.build(), vaultToken);
      List<String> pathList = objectMapper.convertValue(kvDataNode.path("keys"), new TypeReference<List<String>>() {});
      if(pathList == null || pathList.isEmpty()) {
        SecretStore secretStore = getStoreData(rootPath, vaultToken);
        rootStore.setData(secretStore.getData());
        return;
      }
      pathList.forEach( path -> {
        String storeName = StringUtils.removeEnd(path, URL_PATH_SEPARATOR);
        if(hasChildPath(path)) {
          SecretStore secretStore = new SecretStore();
          secretStore.setName(storeName);
          populateStore(rootPath + URL_PATH_SEPARATOR + storeName, vaultToken, secretStore);
          rootStore.getStores().add(secretStore);
        }
        else {
          SecretStore secretStore = getStoreData(rootPath + URL_PATH_SEPARATOR + storeName, vaultToken);
          secretStore.setName(path);
          rootStore.getStores().add(secretStore);
        }
      });
    }
    catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean hasChildPath(String path) {
    return StringUtils.endsWith(path, "/");
  }

  private JsonNode getDataNode(URI uri, Token authToken) {
    HttpResponse httpResponse = null;
    try {
      HttpGet listPathRequest = new HttpGet(uri);
      listPathRequest.addHeader(VAULT_TOKEN_HEADER, String.valueOf(authToken.getToken()));
      httpResponse = httpGateway.getClient().execute(listPathRequest);
      JsonNode jsonResponse = objectMapper.readTree(httpResponse.getEntity().getContent());
      return jsonResponse.path("data");
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    finally {
      HttpClientUtils.closeQuietly(httpResponse);
    }
  }
}
