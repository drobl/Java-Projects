package account;

import backendservices.AccountService;
import controllers.AccountDialogController;
import controllers.HomeSceneController;
import expensetracker.JavaFXApp;
import junit.framework.TestCase;
import user.User;
import java.util.Date;
import java.util.List;

public class AccountTest extends TestCase {

  /**
   * Tests if withdrawing some amount from an account results in the correct balance
   **/
  public void testWithdraw() {
    Account account = getTestCashAccount();
    account.withdraw(300);
    assertEquals(account.getBalance(), 700.0);
  }

  /**
   * Tests if deppositing some amount to an account results in the correct balance
   **/
  public void testDeposit() {
    Account account = getTestCashAccount();
    account.deposit(300);
    assertEquals(account.getBalance(), 1300.0);
  }

  public void testCreateNewAccount() {
    JavaFXApp.setCurrentUser(new User(69, "Test", "Testmann"));
    CashAccount testAcc = getTestCashAccount();
    AccountService.addCashAccount(testAcc.getAccountName(), testAcc.getBalance(),
        testAcc.getMinimumBalance(), testAcc.getCurrency());
    List<Account> accountList = AccountService.getAccountListOfUser(69);
    for (Account account : accountList) {
      if (account instanceof CashAccount
          && account.getAccountName().equals(testAcc.getAccountName())
          && account.getBalance() == testAcc.getBalance()
          && account.getMinimumBalance() == testAcc.getMinimumBalance()
          && ((CashAccount) account).getCurrency().equals(testAcc.getCurrency())) {
        return;
      }
    }
    fail();
  }

  public void testEditAccount() {
    JavaFXApp.setCurrentUser(new User(69, "Test", "Testmann"));
    Account newAccount = AccountService.addCashAccount("Another test acc", 1000, 50, Currency.EUR);
    newAccount = AccountService.editCashAccount(newAccount.getAccountNumber(), "Changed name", 3500,
        10, Currency.USD);
    List<Account> accountList = AccountService.getAccountListOfUser(69);
    for (Account account : accountList) {
      if (account instanceof CashAccount
          && account.getAccountName().equals(newAccount.getAccountName())
          && account.getBalance() == newAccount.getBalance()
          && account.getMinimumBalance() == newAccount.getMinimumBalance()
          && ((CashAccount) account).getCurrency()
              .equals(((CashAccount) newAccount).getCurrency())) {
        return;
      }
    }
    fail();
  }

  public void testDeleteAccount() {
    JavaFXApp.setCurrentUser(new User(69, "Test", "Testmann"));
    Account newAccount = AccountService.addCashAccount("Another test acc", 1000, 50, Currency.EUR);
    int accountNumber = newAccount.getAccountNumber();
    AccountService.deleteAccount(accountNumber);
    List<Account> accountList = AccountService.getAccountListOfUser(69);
    for (Account account : accountList) {
      if (account.getAccountNumber() == accountNumber) {
        fail();
      }
    }
  }

  private CashAccount getTestCashAccount() {
    return new CashAccount(420, "Test cash", 1000, new Date(), Currency.EUR);
  }

  private SavingsAccount getTestSavingsAccount() {
    return new SavingsAccount(2, "Test savings", 1000, 0, new Date(), new Date());
  }
}
