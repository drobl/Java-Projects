package exceptions;

/**
 * A custom exception, used when a login request via {@link backendservices.LoginService} fails.
 */
public class LoginException extends Exception {
  public LoginException(String errorMsg) {
    super(errorMsg);
  }
}
