package notifications;

/**
 * Used in the implementation of the decorator design pattern. In future versions of our app this
 * can be used to decorate additional messages and expand functionality.
 * 
 * @author Daniel Robles
 */

public class AccountWarningsDecorator implements Notifier {
  /**
   * The object being wrapped by the decorator
   *
   */
  private Notifier wrappee;

  /**
   * Constructor with an existing notifier. This will be the object being wrapped by the decorator
   *
   */
  AccountWarningsDecorator(Notifier notifier) {
    this.wrappee = notifier;
  }

  /**
   * Constructor without an existing notifier. Creates a new instance of AccountWarnings that will
   * be used as the object being wrapped by the decorator
   *
   */
  public AccountWarningsDecorator() {
    this.wrappee = new AccountWarnings();
  }

  /**
   * Method that is used to pass a message to a user.
   * 
   * @param String accountName - To alert the user which account needs attention
   * @param String message - the message to pass to the user, gets composed in decorator classes.
   * 
   * @return
   *
   */
  @Override
  public void notifyUser(String accountName, String message) {
    wrappee.notifyUser(accountName, message);
  }

}
