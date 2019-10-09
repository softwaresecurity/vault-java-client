package uk.co.secsoft.vault.domain.auth.method;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.security.auth.DestroyFailedException;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class UserPass implements AuthModel {
  private final String username;
  private final char[] password;

  @JsonCreator
  public UserPass(@JsonProperty("username") String username, @JsonProperty("password") String password) {
    this.username = checkNotNull(username);
    this.password = password.toCharArray();
  }

  @Override
  public synchronized void destroy() throws DestroyFailedException {
    if (password != null) {
      Arrays.fill(password, ' ');
    }
  }

  @Override
  public AuthMethod getAuthMethod() {
    return AuthMethod.USERNAME_PASSWORD;
  }

  @Override
  public String getAuthJson() {
    return null;
  }
}
