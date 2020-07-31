package report;

import account.Account;
import backendservices.AccountService;
import org.junit.Test;
import transaction.Transaction;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

public class ReportCreatorTest {

  private ReportCreator reportCreator = new ReportCreator();
  private Date startDate;
  private Date endDate;

  public ReportCreatorTest() {
    super();
    List<Account> accountList = AccountService.getAccountListOfUser(1);
    try {
      for (Account a : accountList) {
        reportCreator.accept(a);
      }
    } catch (NullPointerException e) {
      e.getStackTrace();
    }

  }

  /**
   * Test if accept() method throws NullPointerException if empty Account array is passed
   */
  @Test
  public void acceptThrowsNullPtrExceptionTest() {
    try {
      reportCreator.accept(null);
    } catch (Exception e) {
      assertSame(e.getClass(), NullPointerException.class);
    }
  }

  /**
   * Test if input dates checker works well
   */
  @Test
  public void testCheckInputDates() {
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    // invalid input: start date > end date
    try {
      startDate = df.parse("01/01/2020");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      endDate = df.parse("31/12/2019");
    } catch (ParseException e) {
      e.printStackTrace();
    }

    boolean datesOk = reportCreator.checkInputDates(startDate, endDate);
    assertFalse(datesOk);

    // valid input: start date < end date
    try {
      startDate = df.parse("01/01/2019");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      endDate = df.parse("31/12/2019");
    } catch (ParseException e) {
      e.printStackTrace();
    }

    datesOk = reportCreator.checkInputDates(startDate, endDate);
    assertTrue(datesOk);
  }

  /**
   * Test if transactions between dates are being chosen correctly
   */
  @Test
  public void testChooseTransactionsBetweenDates() {
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    // some input where start date > end date and is in the future.
    // End date should be set to start date.
    try {
      startDate = df.parse("01/01/2021");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      endDate = df.parse("31/12/2019");
    } catch (ParseException e) {
      e.printStackTrace();
    }

    reportCreator.chooseTransactionsBetweenDates(startDate, endDate);
    List<Transaction> filteredTransactions = reportCreator.getTransactions();
    assertEquals(filteredTransactions.size(),0); // no transactions should be found

    // test some valid input
    try {
      startDate = df.parse("01/01/2019");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      endDate = df.parse("31/12/2019");
    } catch (ParseException e) {
      e.printStackTrace();
    }

    reportCreator.chooseTransactionsBetweenDates(startDate, endDate);
    List<Transaction> allTransactions = reportCreator.getAllTransactions();
    filteredTransactions = reportCreator.getTransactions();
    // size should be different, because some transactions are filtered out
    assertTrue(filteredTransactions.size() < allTransactions.size());

  }


}
