package uk.co.secsoft.vault.client.exception;

public class KVStoreException extends RuntimeException {
  public KVStoreException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
