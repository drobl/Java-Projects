package account;

import java.util.Date;

/**
 * Savings accounts may only be used for depositing money. One can withdraw the money from the
 * account only after the expiry date. If an attempt is made to get the money before the expiry
 * date, the account will be closed and the money is transferred to the CashAccount and the
 * SavingsAccount will be deleted.
 */
public class SavingsAccount extends Account {
  private Date expireDate;
  private String typeName = "SavingsAccount";

  /**
   * Creates a savings account w
   * 
   * @param accountNumber a unique identifier of an account, automatically assigned in the database
   * @param accountName name of the account, can be chosen arbitrary
   * @param balance the amount of money on the account, cannot be less than given minimum
   * @param minimumBalance the minimum balance threshold which must not be subceeded
   * @param openedDate date when account was opened
   * @param expireDate date when savings account expire. After this date, money can be withdrawn
   *        from this account
   */
  public SavingsAccount(int accountNumber, String accountName, double balance,
      double minimumBalance, Date openedDate, Date expireDate) {
    super(accountNumber, accountName, balance, minimumBalance, openedDate);
    setFeeCalculator(new SavingsFeeCalculator());
    this.expireDate = expireDate;
  }

  /**
   * Withdraws money by subtracting the given amount and the corresponding fee from the current
   * balance. Withdrawal is not possible before expiry or if the minimum balance would be subceeded.
   * 
   * @param amount The amount to be withdrawn (excluding the fee).
   */
  @Override
  public void withdraw(double amount) {
    Date today = new Date();
    if (this.expireDate.compareTo(today) < 0) {
      throw new RuntimeException(
          "Withdrawing money from savings accounts before expiry is not possible.");
    } else {
      super.withdraw(amount);
    }
  }

  /**
   * Closes the account and transfers the money onto the Cash Account
   * 
   * @param referenceAccount reference CashAccount where the money should be transferred to
   */
  public void closeAccount(CashAccount referenceAccount) {
    referenceAccount.deposit(this.balance);
    this.balance = 0;
    // TODO: remove account from user accountList
  }

  public Date getExpireDate() {
    return expireDate;
  }

  public String getAccountType() {
    return typeName;
  }
}
