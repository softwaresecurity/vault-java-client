package uk.co.secsoft.vault.domain.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ErrorResponse {

  private final List<String> errors;

  @JsonCreator
  public ErrorResponse(@JsonProperty("errors") List<String> errors) {
    this.errors = errors;
  }

  public List<String> getErrors() {
    return errors;
  }
}
