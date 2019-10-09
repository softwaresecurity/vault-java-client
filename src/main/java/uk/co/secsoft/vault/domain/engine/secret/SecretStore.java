package uk.co.secsoft.vault.domain.engine.secret;

import java.util.Collections;
import java.util.Map;

public class SecretStore {
  //TODO change the Map<String, String> to Map<String, Token>
  private final Map<String, String> data;

  public SecretStore(Map<String, String> data) {
    this.data = data;
  }

  //TODO Change
  public Map<String, String> getData() {
    return data == null ? null : Collections.unmodifiableMap(data);
  }
}
