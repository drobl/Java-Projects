package transaction;

import account.*;
import exceptions.LoginException;
import exceptions.TransactionException;
import junit.framework.TestCase;
import java.util.Date;

public class IncomeTest extends TestCase {

  public void testPerformTransaction() {
    Account account = getTestCashAccount();
    Income income = getTestIncome();
    income.performTransaction(account);

    // check if the transaction is in the account's list of transactions:
    assertTrue(account.getTransactionList().contains(income));

    // test if exception is thrown if the account number of the income doesn't match with the
    // account
    Income otherIncome = new Income(500, new Date(), 100, "test", 2, 2);
    try {
      otherIncome.performTransaction(account);
      fail();
    } catch (Exception e) {
      assertEquals(e.getClass(), TransactionException.class);
    }
  }

  private Income getTestIncome() {
    return new Income(200, new Date(), 100, "test", 1, 3);
  }

  private CashAccount getTestCashAccount() {
    return new CashAccount(1, "Test cash", 1000, new Date(), Currency.EUR);
  }



}
