package expensetracker;

import backendservices.DatabaseService;
import org.h2.tools.RunScript;
import java.io.InputStreamReader;
import java.sql.*;

// wrapper main class - needed to enable correct JAR packaging of the application
// the packaging bug happens because the main class must not extend the JavaFX Application class
public class App {
  public static void main(String[] args) {
    try {
      createDatabaseIfDoesntExist();
    } catch (Exception e) {
      e.printStackTrace();
    }
    JavaFXApp.main(args);
  }

  private static void createDatabaseIfDoesntExist() throws Exception {
    Class.forName("org.h2.Driver");
    try {
      Connection dbConnection =
          DriverManager.getConnection("jdbc:h2:~/expensetracker;ifexists=true");
      System.out.println("h2 database already exists locally. Using existing database...");
      dbConnection.close();
    } catch (SQLException e) {
      System.out.println("h2 database doesn't exist locally. Creating database...");
      createDatabase();
    }
  }

  private static void createDatabase() {
    try {
      String databaseURL = DatabaseService.getDatabaseUrl();
      Connection dbConnection = DriverManager.getConnection(databaseURL);
      // execute create_tables_db.sql and insert_entries_db.sql, where tables and sample entries are
      // created:
      InputStreamReader isr =
          new InputStreamReader(App.class.getResourceAsStream("/sql/create_tables_db.sql"));
      RunScript.execute(dbConnection, isr);
      isr = new InputStreamReader(App.class.getResourceAsStream("/sql/insert_entries_db.sql"));
      RunScript.execute(dbConnection, isr);
      isr.close();
      dbConnection.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
