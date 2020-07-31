package exceptions;

public class AddUserException extends RuntimeException {
  public AddUserException(String errorMsg) {
    super(errorMsg);
  }
}
