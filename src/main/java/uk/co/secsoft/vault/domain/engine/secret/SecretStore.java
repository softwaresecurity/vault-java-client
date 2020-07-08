package uk.co.secsoft.vault.domain.engine.secret;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uk.co.secsoft.vault.domain.token.Token;

import java.util.*;

public class SecretStore {
  private String name;

  private List<SecretStore> stores = new ArrayList<>();
  private Map<String, Token> data = new LinkedHashMap<>();

  private Map<String, String> dataString;

  @JsonAnySetter
  public void setDataValue(String key, String value) {
    data.put(key, new Token(value));
  }

  public Map<String, Token> getData() {
    return data == null ? null : Collections.unmodifiableMap(data);
  }

  public String getName() {
    return name;
  }

  public List<SecretStore> getStores() {
    return stores;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setStores(List<SecretStore> stores) {
    this.stores = stores;
  }

  public void setData(Map<String, Token> data) {
    this.data = data;
  }
}
