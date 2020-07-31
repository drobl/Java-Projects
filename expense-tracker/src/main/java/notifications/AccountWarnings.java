package notifications;

import java.text.DecimalFormat;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * Provides popup warnings when account balance is 50 euro from 0, and from limit, or when account
 * is negative
 * 
 * @author Daniel Robles
 */

public class AccountWarnings implements Notifier {
  private static String pattern = "###,##0.00";
  static DecimalFormat decimalFormat = new DecimalFormat(pattern);

  public AccountWarnings() {

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
    Alert alert = new Alert(AlertType.WARNING, message, ButtonType.CLOSE);
    alert.setHeaderText("Alert!");
    alert.setTitle("Attention Needed on account: " + accountName);
    alert.showAndWait();

  }

  /**
   * This method is used only in JUNIT tests to compare the formating of messages to the expected
   * message format, including decimal formatting.
   * 
   * @param String accountName
   * @param String message
   * @param double amount
   *
   */
  public String notifyUserTestingOnly(String accountName, String message, double amount) {
    String composedMessage = "Alert! " + "Attention Needed on account: " + accountName
        + "\n Amount " + decimalFormat.format(amount) + "\n Additional Info: " + message;
    return composedMessage;

  }

}
