package backendservices;

import java.sql.*;

/**
 * A generic class for connection to the database and eventually performing changes to it.
 * 
 * @author Marko Zunic
 */
public class DatabaseService implements DatabaseServiceInterface {

  private static final String DATABASE_URL = "jdbc:h2:~/expensetracker";
  private static Connection dbConnection;
  static Statement stmt;

  public static String getDatabaseUrl() {
    return DATABASE_URL;
  }

  static void connectToDatabase() throws SQLException {
    dbConnection = DriverManager.getConnection(DATABASE_URL);
    stmt =
        dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
  }

  static void closeConnection() throws SQLException {
    dbConnection.close();
    stmt.close();
  }
}
