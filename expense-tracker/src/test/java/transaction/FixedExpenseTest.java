package transaction;

import account.Account;
import account.CashAccount;
import account.Currency;
import exceptions.TransactionException;
import junit.framework.TestCase;
import java.util.Date;

/**
 * Tests if withdrawing some amount from an account results in the correct balance
 **/

public class FixedExpenseTest extends TestCase {

  public void testPerformTransaction() {
    Account account = getTestCashAccount();
    FixedExpense fixedExpense = getTestFixedExpense();
    fixedExpense.performTransaction(account);

    // check if the transaction is in the account's list of transactions:
    assertTrue(account.getTransactionList().contains(fixedExpense));
    // test if exception is thrown if the account number of the fixed expense doesn't match with the
    // account
    FixedExpense otherFixedExpense = new FixedExpense(5, new Date(), 100, "test", 2, 2,
        FrequencyCategory.getFrequencyCategoryByValue(2), new Date());
    try {
      otherFixedExpense.performTransaction(account);
      fail();
    } catch (Exception e) {
      assertEquals(e.getClass(), TransactionException.class);
    }
  }

  private FixedExpense getTestFixedExpense() {
    return new FixedExpense(5, new Date(), 100, "test", 1, 3,
        FrequencyCategory.getFrequencyCategoryByValue(0), new Date());
  }

  private CashAccount getTestCashAccount() {
    return new CashAccount(1, "Test cash", 1000, new Date(), Currency.EUR);
  }
}

