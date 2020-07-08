package uk.co.secsoft.vault.usecase.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleAESUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAESUtils.class);

  public static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";
  private final SecretKey secretKey;
  private final Cipher cipher;

  public SimpleAESUtils(String secretKeyString) {
    byte[] keyBytes = checkNotNull(secretKeyString).getBytes();
    this.secretKey = initKey(secretKeyString);
    this.cipher = initCipher();
  }

  /**
   * AES only supports key sizes of 16, 24 or 32 bytes.
   * This is the reason to create message digest, to enlarge the bytes, then take first 16 bytes in this method.
   * @param secretKeyString .
   * @return SecretKey.
   */
  private SecretKey initKey(String secretKeyString)  {
    try {
      MessageDigest sha = MessageDigest.getInstance("SHA-256");
      byte[] keyBytes = sha.digest(secretKeyString.getBytes());
      keyBytes = Arrays.copyOf(keyBytes, 16);
      return new SecretKeySpec(Arrays.copyOf(keyBytes, 16), 0, 16, "AES");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  private Cipher initCipher() {
    try {
      return Cipher.getInstance(AES_ALGORITHM);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new RuntimeException(e);
    }
  }

  public String encrypt(String plainText)  {
    try {
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }
    catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
      LOGGER.error("Error in encrypting text", e);
      throw new RuntimeException(e);
    }
  }

  public String decrypt(String cipherText)  {
    try {
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }
    catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
      LOGGER.error("Error in decrypting cipher", e);
      throw new RuntimeException(e);
    }
  }
}
