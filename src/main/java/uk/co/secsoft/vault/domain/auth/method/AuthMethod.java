package uk.co.secsoft.vault.domain.auth.method;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AuthMethod {
  @JsonProperty("user-pass")
  USERNAME_PASSWORD,
  @JsonProperty("ldap")
  LDAP,
  @JsonProperty("asw-ec2")
  AWS_EC2
}
