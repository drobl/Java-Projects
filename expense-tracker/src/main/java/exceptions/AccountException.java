package exceptions;

public class AccountException extends RuntimeException {
  public AccountException(String errorMsg) {
    super(errorMsg);
  }
}
