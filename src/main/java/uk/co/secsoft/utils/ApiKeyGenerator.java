package uk.co.secsoft.utils;


import org.apache.commons.lang3.RandomStringUtils;

public  class ApiKeyGenerator {
    public static String generateKey(int keyLength) {
      return RandomStringUtils.randomAlphanumeric(keyLength);
    }
}
