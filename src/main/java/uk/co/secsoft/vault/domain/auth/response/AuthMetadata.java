package uk.co.secsoft.vault.domain.auth.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AuthMetadata {
  private String username;

  @JsonCreator
  public AuthMetadata(@JsonProperty("username") String username) {
    this.username = username;
  }
}
