package transaction;

import account.Account;
import account.StandardFeeCalculator;
import account.WeekendFeeCalculator;
import exceptions.TransactionException;
import java.util.Calendar;
import java.util.Date;

/**
 * Represents a unique one-time expenditure transaction of a certain amount of money.
 * 
 * @author Vladana Jovanovic
 */
public class Expense extends Transaction {
  private int expenseCategoryId;

  public Expense(int transactionNumber, Date date, double amount, String description,
      int accountNumber, int expenseCategoryId) {
    super(transactionNumber, date, amount, description, accountNumber);
    this.expenseCategoryId = expenseCategoryId;
  }

  /**
   * Withdraws the amount of this expense from the given account. Also checks which fee calculation
   * strategy needs to be applied.
   * 
   * @param account Account from which we want to withdraw.
   */
  public void performTransaction(Account account) {
    if (account.getAccountNumber() != getAccountNumber()) {
      throw new TransactionException("Transaction failed: invalid account");
    }
    // if it's Saturday or Sunday, we use a different algorithm for calculating the fee (Strategy
    // pattern):
    int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == 7 || dayOfWeek == 1) { // Saturday = 7, Sunday = 1
      account.setFeeCalculator(new WeekendFeeCalculator());
    } else {
      account.setFeeCalculator(new StandardFeeCalculator());
    }
    account.withdraw(getAmount());
    account.addTransaction(this);
  }

  public void undoTransaction(Account account) {
    account.deposit(getAmount());
    account.removeTransaction(this);
  }

  public static double calculateTransactionAmountWithFee(Account account, double amount) {
    // if it's Saturday or Sunday, we use a different algorithm for calculating the fee (Strategy
    // pattern):
    int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == 7 || dayOfWeek == 1) {
      account.setFeeCalculator(new WeekendFeeCalculator());
    } else {
      account.setFeeCalculator(new StandardFeeCalculator());
    }
    return account.calculateTransactionAmount(amount);
  }

  public int getExpenseCategoryId() {
    return expenseCategoryId;
  }

  public void setExpenseCategoryId(int expenseCategoryId) {
    this.expenseCategoryId = expenseCategoryId;
  }
}
