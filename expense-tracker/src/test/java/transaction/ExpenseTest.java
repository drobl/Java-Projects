package transaction;


import account.*;
import exceptions.LoginException;
import exceptions.TransactionException;
import junit.framework.TestCase;
import java.util.Calendar;
import java.util.Date;

/**
 * Tests if withdrawing some amount from an account results in the correct balance
 **/

public class ExpenseTest extends TestCase {

  public void testPerformTransaction() {
    Account account = getTestCashAccount();
    Expense expense = getTestExpense();
    expense.performTransaction(account);
    // check if remaining balance correct (on weekends, there should be a 10% fee)
    int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == 7 || dayOfWeek == 1) { // Saturday = 7, Sunday = 1
      assertEquals(account.getBalance(), 890.0);
    } else {
      assertEquals(account.getBalance(), 900.0);
    }
    // check if the transaction is in the account's list of transactions:
    assertTrue(account.getTransactionList().contains(expense));
    // test if exception is thrown if the account number of the expense doesnt match with the
    // account
    Expense otherExpense = new Expense(5, new Date(), 100, "test", 2, 2);
    try {
      otherExpense.performTransaction(account);
      fail();
    } catch (Exception e) {
      assertEquals(e.getClass(), TransactionException.class);
    }
  }

  private Expense getTestExpense() {
    return new Expense(5, new Date(), 100, "test", 1, 3);
  }

  private CashAccount getTestCashAccount() {
    return new CashAccount(1, "Test cash", 1000, new Date(), Currency.EUR);
  }
}
