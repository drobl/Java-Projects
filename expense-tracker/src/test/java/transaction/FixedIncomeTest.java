package transaction;

import account.Account;
import account.CashAccount;
import account.Currency;
import exceptions.TransactionException;
import junit.framework.TestCase;
import java.util.Date;

public class FixedIncomeTest extends TestCase {

  public void testPerformTransaction() {
    Account account = getTestCashAccount();
    FixedIncome fixedIncome = getTestFixedIncome();
    fixedIncome.performTransaction(account);

    // check if the transaction is in the account's list of transactions:
    assertTrue(account.getTransactionList().contains(fixedIncome));
    // test if exception is thrown if the account number of the fixed income doesn't match with the
    // account
    FixedIncome otherFixedIncome = new FixedIncome(5, new Date(), 100, "test", 2, 2,
        FrequencyCategory.getFrequencyCategoryByValue(0), new Date());
    try {
      otherFixedIncome.performTransaction(account);
      fail();
    } catch (Exception e) {
      assertEquals(e.getClass(), TransactionException.class);
    }
  }

  private FixedIncome getTestFixedIncome() {
    return new FixedIncome(5, new Date(), 100, "test", 1, 3,
        FrequencyCategory.getFrequencyCategoryByValue(0), new Date());
  }

  private CashAccount getTestCashAccount() {
    return new CashAccount(1, "Test cash", 1000, new Date(), Currency.EUR);
  }

}
