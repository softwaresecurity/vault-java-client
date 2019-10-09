package uk.co.secsoft.vault.domain.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import uk.co.secsoft.vault.domain.token.Token;

import java.util.List;

@Getter
public class AuthToken {
  private Token token;
  private String accessor;
  private List<String> policies;
  private List<String> tokenPolicies;
  private AuthMetadata metadata;
  private int leaseDuration;
  private boolean renewable;
  private String entityId;
  private String tokenType;

  public AuthToken(@JsonProperty("client_token") String clientToken,
                   @JsonProperty("accessor") String accessor,
                   @JsonProperty("policies") List<String> policies,
                   @JsonProperty("token_policies") List<String> tokenPolicies,
                   @JsonProperty("metadata") AuthMetadata metadata,
                   @JsonProperty("lease_duration") int leaseDuration,
                   @JsonProperty("renewable") boolean renewable,
                   @JsonProperty("entity_id") String entityId,
                   @JsonProperty("token_type") String tokenType) {
    this.token = new Token(clientToken);
    this.accessor = accessor;
    this.policies = policies;
    this.tokenPolicies = tokenPolicies;
    this.metadata = metadata;
    this.leaseDuration = leaseDuration;
    this.renewable = renewable;
    this.entityId = entityId;
    this.tokenType = tokenType;
  }
}
