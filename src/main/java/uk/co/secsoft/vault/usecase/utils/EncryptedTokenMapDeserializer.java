package uk.co.secsoft.vault.usecase.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import uk.co.secsoft.vault.domain.token.Token;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EncryptedTokenMapDeserializer extends JsonDeserializer<Map<String, Token>> {
  private final SimpleAESUtils simpleAESUtils;

  public EncryptedTokenMapDeserializer(SimpleAESUtils simpleAESUtils) {
    this.simpleAESUtils = simpleAESUtils;
  }


  @Override
  public Map<String, Token> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    Map<String, Token> dataMap = new HashMap<>();
    JsonNode node = jsonParser.readValueAsTree();
    node.fields().forEachRemaining(entry -> addEntryToMap(entry.getKey(), entry.getValue().textValue(), dataMap));

    return dataMap;
  }

  private void addEntryToMap(String key, String value, Map<String, Token> dataMap) {
    if(StringUtils.isBlank(value)) {
      dataMap.put(key, new Token(""));
    }
    dataMap.put(key, new Token(simpleAESUtils.decrypt(value)));
  }
}
