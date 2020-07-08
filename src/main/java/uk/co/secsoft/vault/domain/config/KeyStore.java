package uk.co.secsoft.vault.domain.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

public class KeyStore implements Destroyable {
  private final String location;
  private final char[] keyManagerPassword;
  private final char[] password;

  @JsonCreator
  public KeyStore(@JsonProperty("location") String location, @JsonProperty("keyManagerPassword") String keyManagerPassword, @JsonProperty("password") String password) {
    this.location = checkNotNull(location);
    this.keyManagerPassword = StringUtils.isBlank(keyManagerPassword) ? null : keyManagerPassword.toCharArray();
    this.password = password.toCharArray();
  }

  public String getLocation() {
    return location;
  }

  public char[] getPassword() {
    return password;
  }

  public char[] getKeyManagerPassword() {
    return keyManagerPassword;
  }

  @Override
  public synchronized void destroy() {
    if (password != null) {
      Arrays.fill(password, ' ');
    }

    if (keyManagerPassword != null) {
      Arrays.fill(keyManagerPassword, ' ');
    }
  }
}
