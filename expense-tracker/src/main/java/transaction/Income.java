package transaction;

import account.Account;
import account.FeeCalculator;
import exceptions.TransactionException;
import java.util.Date;

/**
 * Represents a unique one-time receipt transaction of a certain amount of money.
 * 
 * @author Vladana Jovanovic
 */
public class Income extends Transaction {
  private int incomeCategoryId;

  public Income(int transactionNumber, Date date, double amount, String description,
      int accountNumber, int incomeCategoryId) {
    super(transactionNumber, date, amount, description, accountNumber);
    this.incomeCategoryId = incomeCategoryId;
  }

  /**
   * Deposits the income amount to the given account.
   * 
   * @param account The account where the money is to be deposited.
   */
  public void performTransaction(Account account) {
    if (account.getAccountNumber() != getAccountNumber()) {
      throw new TransactionException("Transaction failed: invalid account");
    }
    account.deposit(getAmount());
    account.addTransaction(this);
  }

  public void undoTransaction(Account account) {
    FeeCalculator oldFeeCalculator = account.getFeeCalculator();
    account.setFeeCalculator(null);
    account.withdraw(getAmount());
    account.setFeeCalculator(oldFeeCalculator);
    account.removeTransaction(this);
  }

  public int getIncomeCategoryId() {
    return incomeCategoryId;
  }
}
