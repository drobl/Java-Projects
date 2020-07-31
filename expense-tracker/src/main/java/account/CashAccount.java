package account;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Represents a certain amount of cash money physically owned by the user. Unlike other account
 * types, where it is self-evident that the currency in use is EUR cash accounts have a specified
 * {@link Currency}
 * 
 * @author Natalia Tretiakova
 */
public class CashAccount extends Account {
  private Currency currency;
  private String typeName = "CashAccount";

  /**
   * Creates an new Cash Account. Cash accounts have a minimum balance of 0 in contrary to other
   * account types.
   * 
   * @param accountNumber unique identifier of the account
   * @param accountName name of Account, can be chosen arbitrary
   * @param balance the amount of money on the account
   * @param minimumBalance the amount of money on the account, can not be less than limit and may
   *        not be negative
   * @param openedDate date when account was opened
   * @param currency the currency of the cash money.
   */
  public CashAccount(int accountNumber, String accountName, double balance, double minimumBalance,
      Date openedDate, Currency currency) {
    super(accountNumber, accountName, balance, minimumBalance, openedDate);
    if (minimumBalance < 0) {
      throw new IllegalArgumentException("Cash accounts cannot have a negative minimum balance");
    }
    this.currency = currency;
    setFeeCalculator(new CashFeeCalculator());
  }

  /**
   * Creates an new Cash Account with zero minimum balance
   * 
   * @param accountNumber unique identifier of the account
   * @param accountName name of Account, can be chosen arbitrary
   * @param balance the amount of money on the account
   * @param openedDate date when account was opened
   * @param currency the currency of the cash money.
   */
  public CashAccount(int accountNumber, String accountName, double balance, Date openedDate,
      Currency currency) {
    super(accountNumber, accountName, balance, 0, openedDate);
    this.currency = currency;
    setFeeCalculator(null);
  }

  public Currency getCurrency() {
    return currency;
  }

  public String getAccountType() {
    return typeName;
  }

  @Override
  public String getBalanceFormatted() {
    String pattern = "###,##0.00";
    DecimalFormat decimalFormat = new DecimalFormat(pattern);
    return currency.getSymbol() + decimalFormat.format(balance);
  }

  @Override
  public void setFeeCalculator(FeeCalculator feeCalculator) {
    // no fees for cash accounts
    return;
  }


}
