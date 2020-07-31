package notifications;

/**
 * Interface that is implemented by AccountWarnings. For use in decorator design pattern.
 * 
 * @param String accountName - To alert the user which account needs attention
 * @param String message - the message to pass to the user, gets composed in decorator classes.
 * 
 * @author Daniel Robles
 */
public interface Notifier {
  void notifyUser(String accountName, String message);
}
