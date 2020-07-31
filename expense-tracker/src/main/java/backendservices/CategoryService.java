package backendservices;

import transaction.Expense;
import transaction.ExpenseCategory;
import transaction.IncomeCategory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryService extends DatabaseService {

  static public List<IncomeCategory> getIncomeCategoryList() {
    try {
      connectToDatabase();
      List<IncomeCategory> incomeCategoryList = new ArrayList<>();
      String query = "SELECT Id, Name FROM IncomeCategory;";
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        IncomeCategory category = new IncomeCategory(rs.getInt(1), rs.getString(2));
        incomeCategoryList.add(category);
      }
      closeConnection();
      return incomeCategoryList;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }

  }

  static public IncomeCategory addIncomeCategory(String name) {
    try {
      connectToDatabase();
      String query = "INSERT INTO IncomeCategory(Name) VALUES('" + name + "');";
      stmt.execute(query);
      // get the id of the income category:
      query = "SELECT MAX(Id) FROM IncomeCategory;";
      ResultSet rs = stmt.executeQuery(query);
      rs.next();
      int id = rs.getInt(1);
      closeConnection();
      return new IncomeCategory(id, name);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  static public IncomeCategory editIncomeCategory(int id, String name) {
    try {
      connectToDatabase();
      String query = "UPDATE IncomeCategory SET " + "Name='" + name + "' " + "WHERE Id=" + id + ";";
      stmt.execute(query);
      closeConnection();
      return new IncomeCategory(id, name);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  static public void deleteIncomeCategory(int id) {
    try {
      connectToDatabase();
      String query = "DELETE FROM IncomeCategory WHERE Id=" + id + ";";
      stmt.execute(query);
      closeConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  static public List<ExpenseCategory> getExpenseCategoryList() {
    try {
      connectToDatabase();
      List<ExpenseCategory> expenseCategoryList = new ArrayList<>();
      String query = "SELECT Id, Name FROM ExpenseCategory;";
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        ExpenseCategory category = new ExpenseCategory(rs.getInt(1), rs.getString(2));
        expenseCategoryList.add(category);
      }
      closeConnection();
      return expenseCategoryList;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  static public ExpenseCategory addExpenseCategory(String name) {
    try {
      connectToDatabase();
      String query = "INSERT INTO ExpenseCategory(Name) VALUES('" + name + "');";
      stmt.execute(query);
      // get the id of the income category:
      query = "SELECT MAX(Id) FROM ExpenseCategory;";
      ResultSet rs = stmt.executeQuery(query);
      rs.next();
      int id = rs.getInt(1);
      closeConnection();
      return new ExpenseCategory(id, name);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  static public ExpenseCategory editExpenseCategory(int id, String name) {
    try {
      connectToDatabase();
      String query =
          "UPDATE ExpenseCategory SET " + "Name='" + name + "' " + "WHERE Id=" + id + ";";
      stmt.execute(query);
      closeConnection();
      return new ExpenseCategory(id, name);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  static public void deleteExpenseCategory(int id) {
    try {
      connectToDatabase();
      String query = "DELETE FROM ExpenseCategory WHERE Id=" + id + ";";
      stmt.execute(query);
      closeConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
