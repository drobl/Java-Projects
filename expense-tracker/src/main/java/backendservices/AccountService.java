package backendservices;

import account.Account;
import account.Currency;
import expensetracker.JavaFXApp;
import java.text.SimpleDateFormat;
import java.util.Date;
import account.CashAccount;
import account.SavingsAccount;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import user.User;

/**
 * Acts as an interface for reading/updating account entries from the database
 * 
 * @author Marko Zunic
 */
public class AccountService extends DatabaseService {

  /**
   * Finds all accounts belonging to some given user in the database. If no user is found an
   * exception should be thrown (TODO: implement exception)
   * 
   * @param userId The id of the user whose accounts we want to get
   * @return A list of all accounts belonging to the given user
   */
  static public List<Account> getAccountListOfUser(int userId) {
    try {
      connectToDatabase();
      List<Account> accountList = new ArrayList<>();
      // get savings accounts:
      accountList.addAll(getSavingsAccountListOfUser(stmt, userId));
      accountList.addAll(getCashAccountListOfUser(stmt, userId));
      closeConnection();
      return accountList;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Deletes an account from the database
   * 
   * @param accountNumber The number of the account to be deleted
   */
  static public void deleteAccount(int accountNumber) {
    try {
      connectToDatabase();
      String query = "DELETE FROM Account WHERE AccountNumber=" + accountNumber + ";";
      stmt.execute(query);
      closeConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds a new savings account to the database with given values.
   * 
   * @param accountName name of the account to be added
   * @param balance initial balance of the account to be added
   * @return The newly created account
   */
  static public Account addSavingsAccount(String accountName, double balance, double minimumBalance,
      Date expiryDate) {
    try {
      connectToDatabase();
      User currentUser = JavaFXApp.getCurrentUser();
      SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
      Date today = new Date();
      String dateTodayAsString = dateFormatter.format(today);
      String expiryDateAsString = dateFormatter.format(expiryDate);
      // make the account entry first:
      String query =
          "INSERT INTO Account(Name, Balance, MinimumBalance, OpenedDate, UserId) VALUES ('"
              + accountName + "', " + balance + ", " + minimumBalance + ", " + "TO_DATE('"
              + dateTodayAsString + "','DD/MM/YYYY')" + ", " + currentUser.getId() + ");";
      stmt.execute(query);
      // get the account number of the previously created entry (it's always the highest account
      // number):
      query = "SELECT MAX(AccountNumber) FROM Account;";
      ResultSet rs = stmt.executeQuery(query);
      rs.next();
      int newAccountNumber = rs.getInt(1);
      // .. and the make the corresponding savings account entry:
      query = "INSERT INTO SavingsAccount(ExpireDate, AccountNumber) VALUES(" + "TO_DATE('"
          + expiryDateAsString + "','DD/MM/YYYY')" + " ," + newAccountNumber + ")";
      stmt.execute(query);
      closeConnection();
      return new SavingsAccount(newAccountNumber, accountName, balance, minimumBalance, today,
          expiryDate);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Edits an already existing savings account in the database.
   * 
   * @param accountNumber unique identifier of the account to be edited
   * @param accountName the new edited name of the account
   * @param balance new edited balance of the account
   * @param minimumBalance new minimum balance of the account
   * @param expiryDate new expiry date of the account
   * @return The newly edited account
   */
  static public Account editSavingsAccount(int accountNumber, String accountName, double balance,
      double minimumBalance, Date expiryDate) {
    try {
      connectToDatabase();
      SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
      String expiryDateAsString = dateFormatter.format(expiryDate);
      // edit the account entry first:
      String query =
          "UPDATE Account SET " + "Name='" + accountName + "', " + "Balance=" + balance + ", "
              + "MinimumBalance=" + minimumBalance + " WHERE AccountNumber=" + accountNumber + ";";
      stmt.execute(query);
      // then edit the savings account entry:
      query = "UPDATE SavingsAccount SET " + "ExpireDate=TO_DATE('" + expiryDateAsString
          + "', 'DD/MM/YYYY')" + " WHERE AccountNumber=" + accountNumber + ";";
      stmt.execute(query);
      closeConnection();
      return new SavingsAccount(accountNumber, accountName, balance, minimumBalance, new Date(),
          expiryDate);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Adds a new cash account to the database with given values.
   * 
   * @param accountName name of the account to be added
   * @param balance initial balance of the account to be added
   * @param currency the currency of the cash account to be added
   * @return The newly created account
   */
  static public Account addCashAccount(String accountName, double balance, double minimumBalance,
      Currency currency) {
    try {
      connectToDatabase();
      User currentUser = JavaFXApp.getCurrentUser();

      SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
      Date today = new Date();
      String dateTodayAsString = dateFormatter.format(today);
      // make the account entry first:
      String query =
          "INSERT INTO Account(Name, Balance, MinimumBalance, OpenedDate, UserId) VALUES ('"
              + accountName + "', " + balance + ", " + minimumBalance + ", " + "TO_DATE('"
              + dateTodayAsString + "','DD/MM/YYYY')" + ", " + currentUser.getId() + ");";
      stmt.execute(query);
      // get the account number of the previously created entry (it's always the highest account
      // number):
      query = "SELECT MAX(AccountNumber) FROM Account;";
      ResultSet rs = stmt.executeQuery(query);
      rs.next();
      int newAccountNumber = rs.getInt(1);
      // .. and the make the corresponding cash account entry:
      query = "INSERT INTO CashAccount(Currency, AccountNumber) VALUES(" + currency.getValue()
          + ", " + newAccountNumber + ")";
      stmt.execute(query);
      closeConnection();
      return new CashAccount(newAccountNumber, accountName, balance, minimumBalance, today,
          currency);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Edits an already existing cash account in the database
   * 
   * @param accountNumber unique identifier of the cash account
   * @param accountName new name of the account
   * @param balance new balance of the account
   * @param currency new currency of the account
   * @return The newly edited account
   */
  static public Account editCashAccount(int accountNumber, String accountName, double balance,
      double minimumBalance, Currency currency) {
    try {
      connectToDatabase();
      // edit the account entry first:
      String query =
          "UPDATE Account SET " + "Name='" + accountName + "', " + "Balance=" + balance + ","
              + "MinimumBalance=" + minimumBalance + " WHERE AccountNumber=" + accountNumber + ";";
      stmt.execute(query);
      // then edit the cash account entry:
      query = "UPDATE CashAccount SET " + "Currency=" + currency.getValue()
          + " WHERE AccountNumber=" + accountNumber + ";";
      stmt.execute(query);
      closeConnection();
      return new CashAccount(accountNumber, accountName, balance, minimumBalance, new Date(),
          currency);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  static public void editAccount(Account acc) {
    if (acc instanceof CashAccount) {
      editCashAccount(acc.getAccountNumber(), acc.getAccountName(), acc.getBalance(),
          acc.getMinimumBalance(), ((CashAccount) acc).getCurrency());
    } else if (acc instanceof SavingsAccount) {
      editSavingsAccount(acc.getAccountNumber(), acc.getAccountName(), acc.getBalance(),
          acc.getMinimumBalance(), ((SavingsAccount) acc).getExpireDate());
    }
  }

  static private List<Account> getSavingsAccountListOfUser(Statement stmt, int userId)
      throws SQLException {
    List<Account> savingsAccountList = new ArrayList<>();
    String query =
        "SELECT Account.AccountNumber, Name, Balance, MinimumBalance, OpenedDate, ExpireDate"
            + " FROM Account INNER JOIN SavingsAccount ON Account.AccountNumber = SavingsAccount.AccountNumber"
            + " WHERE UserId=" + userId + ";";
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      Account account = new SavingsAccount(rs.getInt(1), rs.getString(2), rs.getDouble(3),
          rs.getDouble(4), rs.getDate(5), rs.getDate(6));
      savingsAccountList.add(account);
    }
    return savingsAccountList;
  }

  static private List<Account> getCashAccountListOfUser(Statement stmt, int userId)
      throws SQLException {
    List<Account> cashAccountList = new ArrayList<>();
    String query =
        "SELECT Account.AccountNumber, Name, Balance, MinimumBalance, OpenedDate, Currency"
            + " FROM Account INNER JOIN CashAccount ON Account.AccountNumber = CashAccount.AccountNumber"
            + " WHERE UserId=" + userId + ";";
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      Account account = new CashAccount(rs.getInt(1), rs.getString(2), rs.getDouble(3),
          rs.getDouble(4), rs.getDate(5), Currency.getCurrencyByValue(rs.getInt(6)));
      cashAccountList.add(account);
    }
    return cashAccountList;
  }

}
