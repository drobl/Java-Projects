package backendservices;

import java.util.Calendar;
import transaction.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class TransactionService extends DatabaseService {

  private final static Logger LOGGER = Logger.getLogger("Transaction Service");

  static public List<Transaction> getTransactionListOfAccount(int accountNumber) {
    try {
      connectToDatabase();
      List<Transaction> transactionList = new ArrayList<>();
      // get expense list:
      transactionList.addAll(getExpenseListOfAccount(accountNumber));
      // get income list
      transactionList.addAll(getIncomeListOfAccount(accountNumber));
      // get fixed expense list:
      transactionList.addAll(getFixedExpenseListOfAccount(accountNumber));
      // get fixed income list
      transactionList.addAll(getFixedIncomeListOfAccount(accountNumber));

      closeConnection();
      return transactionList;

    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  static public Transaction addExpense(int accountNumber, Date expenseDate, double amount,
      double amountWithFee, int expenseCategoryId, String description) {
    try {
      connectToDatabase();
      SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
      String expenseDateAsString = dateFormatter.format(expenseDate);
      // make transaction entry first:
      String query = "INSERT INTO Transaction(AccountNumber, Date, Amount, Description) VALUES("
          + accountNumber + ", " + "TO_DATE('" + expenseDateAsString + "','DD/MM/YYYY'), "
          + amountWithFee + ", " + "'" + description + "'" + ");";
      stmt.execute(query);
      // get the transaction number of the previously created entry (it's always the highest
      // transaction number):
      query = "SELECT MAX(TransactionNumber) FROM Transaction;";
      ResultSet rs = stmt.executeQuery(query);
      rs.next();
      int newTransactionNumber = rs.getInt(1);
      // .. and the make the corresponding expense entry:
      query = "INSERT INTO Expense(TransactionNumber, CategoryId) VALUES (" + newTransactionNumber
          + "," + expenseCategoryId + ");";
      stmt.execute(query);
      return new Expense(newTransactionNumber, expenseDate, amount, description, accountNumber,
          expenseCategoryId);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  static public Transaction editExpense(int transactionNumber, int accountNumber, Date expenseDate,
      double amount, int expenseCategoryId, String description) {
    try {
      connectToDatabase();
      SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
      String expenseDateString = dateFormatter.format(expenseDate);
      String query = "UPDATE Transaction SET " + "AccountNumber=" + accountNumber + ", "
          + "Date= TO_DATE('" + expenseDateString + "', 'DD/MM/YYYY'), " + "Description='"
          + description + "' " + "WHERE TransactionNumber=" + transactionNumber + ";";
      stmt.execute(query);
      query = "UPDATE Expense SET " + "CategoryId= " + expenseCategoryId
          + "WHERE TransactionNumber =" + transactionNumber + ";";
      stmt.execute(query);
      closeConnection();
      return new Expense(transactionNumber, expenseDate, amount, description, accountNumber,
          expenseCategoryId);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  static public Transaction addIncome(int accountNumber, Date incomeDate, double amount,
      int incomeCategoryId, String description) {
    try {
      connectToDatabase();
      SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
      String expenseDateAsString = dateFormatter.format(incomeDate);
      // make transaction entry first:
      String query = "INSERT INTO Transaction(AccountNumber, Date, Amount, Description) VALUES("
          + accountNumber + ", " + "TO_DATE('" + expenseDateAsString + "','DD/MM/YYYY'), " + amount
          + ", " + "'" + description + "'" + ");";
      stmt.execute(query);
      // get the transaction number of the previously created entry (it's always the highest
      // transaction number):
      query = "SELECT MAX(TransactionNumber) FROM Transaction;";
      ResultSet rs = stmt.executeQuery(query);
      rs.next();
      int newTransactionNumber = rs.getInt(1);
      // .. and the make the corresponding expense entry:
      System.out.print(incomeCategoryId);
      query = "INSERT INTO Income(TransactionNumber, CategoryId) VALUES (" + newTransactionNumber
          + "," + incomeCategoryId + ");";
      stmt.execute(query);
      return new Income(newTransactionNumber, incomeDate, amount, description, accountNumber,
          incomeCategoryId);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  static public Transaction editIncome(int transactionNumber, int accountNumber, Date expenseDate,
      double amount, int incomeCategoryId, String description) {
    try {
      connectToDatabase();
      SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
      String expenseDateString = dateFormatter.format(expenseDate);
      String query = "UPDATE Transaction SET " + "AccountNumber=" + accountNumber + ", "
          + "Date= TO_DATE('" + expenseDateString + "', 'DD/MM/YYYY'), " + "Description='"
          + description + "' " + "WHERE TransactionNumber=" + transactionNumber + ";";
      stmt.execute(query);
      query = "UPDATE Income SET " + "CategoryId= " + incomeCategoryId + "WHERE TransactionNumber ="
          + transactionNumber + ";";
      stmt.execute(query);
      closeConnection();
      return new Income(transactionNumber, expenseDate, amount, description, accountNumber,
          incomeCategoryId);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Deletes a transaction from the database
   * 
   * @param transactionNumber The number of the transaction to be deleted
   */
  static public void deleteTransaction(int transactionNumber) {
    try {
      connectToDatabase();
      String query = "DELETE FROM Transaction WHERE TransactionNumber=" + transactionNumber + ";";
      stmt.execute(query);
      closeConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  static public void updateDueDate(Transaction transaction) {
    try {
      Date newNextDueDate = null;
      if (transaction instanceof FixedIncome) {
        newNextDueDate = ((FixedIncome) transaction).getNextDueDate();
      } else if (transaction instanceof FixedExpense) {
        newNextDueDate = ((FixedExpense) transaction).getNextDueDate();
      } else {
        return;
      }
      connectToDatabase();
      SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
      String newDueDateString = dateFormatter.format(newNextDueDate);
      String query =
          "UPDATE " + (transaction instanceof FixedIncome ? "FixedIncome" : "FixedExpense")
              + " SET " + "NextDueDate=TO_DATE('" + newDueDateString + "', 'DD/MM/YYYY') "
              + "WHERE TransactionNumber=" + transaction.getTransactionNumber() + ";";
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  static private List<Transaction> getExpenseListOfAccount(int accountNumber) throws SQLException {
    List<Transaction> expenseList = new ArrayList<>();
    String query =
        "SELECT Transaction.TransactionNumber, Date, Amount, Description, CategoryId FROM Transaction INNER JOIN Expense ON Transaction.TransactionNumber = Expense.TransactionNumber"
            + " WHERE AccountNumber=" + accountNumber + ";";
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      Transaction transaction = new Expense(rs.getInt(1), rs.getDate(2), rs.getDouble(3),
          rs.getString(4), accountNumber, rs.getInt(5));
      expenseList.add(transaction);
    }
    return expenseList;
  }

  static private List<Transaction> getFixedExpenseListOfAccount(int accountNumber)
      throws SQLException {
    List<Transaction> fixedExpenseList = new ArrayList<>();
    String query =
        "SELECT Transaction.TransactionNumber, Date, Amount, Description, CategoryId, FrequencyCategory, NextDueDate "
            + "FROM Transaction INNER JOIN FixedExpense ON Transaction.TransactionNumber = FixedExpense.TransactionNumber"
            + " WHERE AccountNumber=" + accountNumber + ";";
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      Transaction transaction = new FixedExpense(rs.getInt(1), rs.getDate(2), rs.getDouble(3),
          rs.getString(4), accountNumber, rs.getInt(5),
          FrequencyCategory.getFrequencyCategoryByValue(rs.getInt(6)), rs.getDate(7));
      fixedExpenseList.add(transaction);
    }
    return fixedExpenseList;
  }

  static private List<Transaction> getIncomeListOfAccount(int accountNumber) throws SQLException {
    List<Transaction> incomeList = new ArrayList<>();
    String query =
        "SELECT Transaction.TransactionNumber, Date, Amount, Description, CategoryId FROM Transaction INNER JOIN Income ON Transaction.TransactionNumber = Income.TransactionNumber"
            + " WHERE AccountNumber=" + accountNumber + ";";
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      Transaction transaction = new Income(rs.getInt(1), rs.getDate(2), rs.getDouble(3),
          rs.getString(4), accountNumber, rs.getInt(5));
      incomeList.add(transaction);
    }
    return incomeList;
  }

  static private List<Transaction> getFixedIncomeListOfAccount(int accountNumber)
      throws SQLException {
    List<Transaction> fixedIncomeList = new ArrayList<>();
    String query =
        "SELECT Transaction.TransactionNumber, Date, Amount, Description, CategoryId, FrequencyCategory, NextDueDate "
            + "FROM Transaction INNER JOIN FixedIncome ON Transaction.TransactionNumber = FixedIncome.TransactionNumber"
            + " WHERE AccountNumber=" + accountNumber + ";";
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      Transaction transaction = new FixedIncome(rs.getInt(1), rs.getDate(2), rs.getDouble(3),
          rs.getString(4), accountNumber, rs.getInt(5),
          FrequencyCategory.getFrequencyCategoryByValue(rs.getInt(6)), rs.getDate(7));
      fixedIncomeList.add(transaction);
    }
    return fixedIncomeList;
  }

}
