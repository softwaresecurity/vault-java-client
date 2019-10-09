package uk.co.secsoft.vault.domain.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

public class KeyStore implements Destroyable {
  private final String location;
  private final char[] password;

  @JsonCreator
  public KeyStore(@JsonProperty("location") String location, @JsonProperty("password") String password) {
    this.location = checkNotNull(location);
    this.password = password.toCharArray();
  }

  public String getLocation() {
    return location;
  }

  public char[] getPassword() {
    return password;
  }

  @Override
  public synchronized void destroy() throws DestroyFailedException {
    if (password != null) {
      Arrays.fill(password, ' ');
    }
  }
}
