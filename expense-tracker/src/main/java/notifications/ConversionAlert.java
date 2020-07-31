package notifications;

import java.text.DecimalFormat;
import account.Currency;
import account.CurrencyConverter;

/**
 * Part of the implementation of the decorator design pattern. This notifies the user of the
 * conversion amount when a cash account is created with a currency other than EUR.
 * 
 * @author Daniel Robles
 */
public class ConversionAlert extends AccountWarningsDecorator {
  private Currency currencyType;
  private double amount;
  private String accountName;
  private static String pattern = "###,##0.00";
  static DecimalFormat decimalFormat = new DecimalFormat(pattern);

  /**
   * Constructor that also calls the create message method.
   * 
   * @param Notifier notifier - an instance of the AccountWarnings class used to decorate messages
   * @param String accountName - To alert the user which account needs attention
   * @param Currency currencyType - the type of currency that the account was created with
   * @param double amount - the amount the account was created with, this will be converted to euro
   *
   */
  public ConversionAlert(Notifier notifier, String accountName, Currency currencyType,
      double amount) {
    super(notifier);
    this.accountName = accountName;
    this.currencyType = currencyType;
    this.amount = amount;
    createMessage();
  }

  /**
   * Method that creates the message and then passes it on to be sent to the user Uses local
   * variables set in the constructor to create the message to be sent.
   * 
   * It checks what type of currency the Alert was called with and then performs the appropriate
   * conversion in order to notify the user of the total conversion amount.
   * 
   * @return
   *
   */
  private void createMessage() {
    String message = "Amount will be converted to Euro at exchange rate: ";
    if (currencyType == Currency.USD) {
      message += CurrencyConverter.getUSDtoEURConversionRate() + "\nConverted amount: € "
          + CurrencyConverter.convertToEuro(Currency.USD, amount);
    }
    if (currencyType == Currency.GBP) {
      message += CurrencyConverter.getGBPtoEURConversionRate() + "\nConverted amount: € "
          + CurrencyConverter.convertToEuro(Currency.GBP, amount);
    }

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
  public void notifyUser(String accountName, String message) {
    super.notifyUser(accountName, message);
  }

}
