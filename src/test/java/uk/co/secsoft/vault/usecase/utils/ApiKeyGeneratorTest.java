package uk.co.secsoft.vault.usecase.utils;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;

class ApiKeyGeneratorTest {

  @Test
  void generateKey() {
    String generatedKey = ApiKeyGenerator.generateKey(10);
    assertThat(generatedKey, is(notNullValue()));
    assertThat(generatedKey.length(), is(10));
  }

  @Test
  void keyShouldBeRandomlyGenerated() {
    String key1 = ApiKeyGenerator.generateKey(10);
    String key2 = ApiKeyGenerator.generateKey(10);
    assertThat(key1, not(key2));
  }
}