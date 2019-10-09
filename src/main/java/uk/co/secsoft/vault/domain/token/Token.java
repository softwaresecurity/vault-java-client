package uk.co.secsoft.vault.domain.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import java.util.Arrays;

public class Token implements Destroyable {
  private final char[] token;

  @JsonCreator
  public Token(@JsonProperty("token") String token) {
    this.token = token.toCharArray();
  }

  public char[] getToken() {
    return token;
  }

  public synchronized void destroy() throws DestroyFailedException {
    if (token != null) {
      Arrays.fill(token, ' ');
    }
  }
}
