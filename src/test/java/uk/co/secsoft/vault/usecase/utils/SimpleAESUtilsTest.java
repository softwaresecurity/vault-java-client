package uk.co.secsoft.vault.usecase.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class SimpleAESUtilsTest {

  public static final String SUPER_SECRET_KEY = "superSecretKey";
  private static final String cipherText = "+RffvhjOCcG9Fihl/TaEoQ==";
  private static final String painText = "aText";

  private static SimpleAESUtils underTest;

  @BeforeAll
  public static void setUpAll() {
    underTest = new SimpleAESUtils(SUPER_SECRET_KEY);
  }
  @Test
  void encrypt() {
    String cipher = underTest.encrypt(painText);
    assertThat(cipher, is(cipherText));
  }

  @Test
  void decrypt() {
    assertThat(underTest.decrypt(cipherText), is(painText));
  }
}