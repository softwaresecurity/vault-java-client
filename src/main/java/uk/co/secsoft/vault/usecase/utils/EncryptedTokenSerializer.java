package uk.co.secsoft.vault.usecase.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import uk.co.secsoft.vault.domain.token.Token;

import java.io.IOException;

public class EncryptedTokenSerializer extends JsonSerializer<Token> {
  private final SimpleAESUtils simpleAESUtils;

  public EncryptedTokenSerializer(SimpleAESUtils simpleAESUtils) {
    this.simpleAESUtils = simpleAESUtils;
  }

  @Override
  public void serialize(Token token, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
      if(token != null && token.getToken() != null && token.getToken().length > 0) {
        jsonGenerator.writeString(simpleAESUtils.encrypt(new String(token.getToken())));
      }
      else {
        jsonGenerator.writeString("");
      }
  }
}
