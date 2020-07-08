package uk.co.secsoft.vault.client.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.secsoft.vault.domain.config.KeyStore;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


public class HttpGateway {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpGateway.class);

  private final HttpClient httpClient;

  /**
   * Use this constructor to create client.
   * If Trust store and keystore is null then Http client will trusts all certificates including self signed one.
   * TBD. If Trust store if not empty then http client will trus only trust store certs.
   * TBD. To enable mutual authentication, keystore and truststore values must be passed
   * @return http client which trust all certificates.
   */
  public HttpGateway(KeyStore keyStore, KeyStore trustStore) {
    this.httpClient = initClient(keyStore, trustStore);
  }

  public HttpGateway(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public HttpClient getClient() {
    return httpClient;
  }

  private HttpClient initClient(KeyStore keyStore, KeyStore trustStore) {
    return initClient();
  }

  private HttpClient initClient() {
    SSLContext sslContext = null;
    try {
      sslContext = SSLContexts.custom()
          .loadTrustMaterial(new TrustSelfSignedStrategy())
          .build();
    }
    catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
      LOGGER.error("Error in creating SSL context to trust all certificates.");
    }
    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
    Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create().register("https", socketFactory).build();

    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(reg);
    connectionManager.setMaxTotal(200);
    connectionManager.setDefaultMaxPerRoute(20);

    CloseableHttpClient httpClient = HttpClients.custom()
        .setConnectionManager(connectionManager)
        .build();
    return httpClient;
  }
}
