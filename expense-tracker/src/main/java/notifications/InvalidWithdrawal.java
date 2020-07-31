
package notifications;

import java.text.DecimalFormat;

/**
 * Part of the implementation of the decorator design pattern. This notifies the user when
 * attempting to make an expense worth more than available in that account
 * 
 * @author Daniel Robles
 */
public class InvalidWithdrawal extends AccountWarningsDecorator {
  private double balance;
  private double withdrawAmount;
  private String accountName;
  private static String pattern = "###,##0.00";
  static DecimalFormat decimalFormat = new DecimalFormat(pattern);


  /**
   * Constructor that also calls the create message method.
   * 
   * @param Notifier notifier - an instance of the AccountWarnings class used to decorate messages
   * @param String accountName - To alert the user which account needs attention
   * @param double withdrawAmount - the expense amount the user attempted to perform
   * @param double balance - the balance on an account, used to create the message to pass to the
   *        user
   */
  public InvalidWithdrawal(Notifier notifier, String accountName, double withdrawAmount,
      double balance) {
    super(notifier);
    this.balance = balance;
    this.withdrawAmount = withdrawAmount;
    this.accountName = accountName;
    createMessage();
  }

  /**
   * Method that creates the message and then passes it on to be sent to the user Uses local
   * variables set in the constructor to create the message to be sent.
   * 
   * @return
   *
   */
  private void createMessage() {
    String message = "This expense surpasses the balance left on this account! \n"
        + "Expense amount: " + decimalFormat.format(withdrawAmount) + "\nCurrent balance: "
        + decimalFormat.format(balance) + "\n\n Transaction not executed!";
    notifyUser(accountName, message);
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
  public void notifyUser(String acctName, String message) {
    super.notifyUser(acctName, message);
  }

}
