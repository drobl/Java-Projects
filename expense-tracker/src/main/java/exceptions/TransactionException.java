package exceptions;

public class TransactionException extends RuntimeException {
  public TransactionException(String errorMsg) {
    super(errorMsg);
  }
}
