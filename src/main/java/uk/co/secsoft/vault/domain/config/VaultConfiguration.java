package uk.co.secsoft.vault.domain.config;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class VaultConfiguration {
  private final String baseUrl;
  private final KeyStore trustStore;
  private final KeyStore keyStore;

  @JsonCreator
  public VaultConfiguration(@JsonProperty("baseUrl") String baseUrl, @JsonProperty("trustStore") KeyStore trustStore, @JsonProperty("keyStore") KeyStore keyStore) {
    this.baseUrl = baseUrl;
    this.trustStore = trustStore;
    this.keyStore = keyStore;
  }

  public String getApiVersion() {
    return "v1";
  }
}
