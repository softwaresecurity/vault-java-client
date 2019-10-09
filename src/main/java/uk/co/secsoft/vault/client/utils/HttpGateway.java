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
import uk.co.secsoft.vault.domain.config.KeyStore;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


public class HttpGateway {
  private final KeyStore trustStore;

  public HttpGateway(KeyStore trustStore) {
    this.trustStore = trustStore;
  }

  public HttpClient getHttpClient() {
    SSLContext sslContext = null;
    try {
      sslContext = SSLContexts.custom()
          .loadTrustMaterial(new TrustSelfSignedStrategy())
          .build();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (KeyManagementException e) {
      e.printStackTrace();
    } catch (KeyStoreException e) {
      e.printStackTrace();
    }
    SSLConnectionSocketFactory socketFactory =
        new SSLConnectionSocketFactory(sslContext);
    Registry<ConnectionSocketFactory> reg =
        RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", socketFactory)
            .build();



    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(reg);
    connectionManager.setMaxTotal(200);
    connectionManager.setDefaultMaxPerRoute(20);

    CloseableHttpClient httpClient = HttpClients.custom()
        .setConnectionManager(connectionManager)
        .build();
    return httpClient;
  }
}
