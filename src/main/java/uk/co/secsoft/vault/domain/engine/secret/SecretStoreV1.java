package uk.co.secsoft.vault.domain.engine.secret;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import uk.co.secsoft.vault.domain.token.Token;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SecretStoreV1 {
  private Map<String, Token> data = new LinkedHashMap<>();

  @JsonAnySetter
  public void setDataValue(String key, String value) {
    data.put(key, new Token(value));
  }

  //TODO Change
  public Map<String, Token> getData() {
    return data == null ? null : Collections.unmodifiableMap(data);
  }
}
