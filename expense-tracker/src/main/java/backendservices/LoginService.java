package backendservices;

import exceptions.LoginException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import user.User;

/**
 * Acts as an interface for sending log-in calls to the backend
 * 
 * @author Marko Zunic
 */
public class LoginService extends DatabaseService {
  /**
   * Looks up the database whether there is a registered user with the given login credentials. If
   * no user is found a {@link exceptions.LoginException} is thrown.
   * 
   * @param username The username to be looked up by
   * @param password The (unencrypted) password to be looked up by
   * @return The corresponding user from the database
   */
  static public User getUserByLoginCredentials(String username, String password)
      throws LoginException {
    try {
      connectToDatabase();
      String query =
          "SELECT * FROM User WHERE Username='" + username + "' AND Password='" + password + "';";
      ResultSet rs = stmt.executeQuery(query);
      // Defensive programming: If no user or more than one user found:
      if (!rs.last() || rs.getRow() != 1) {
        throw new LoginException("Invalid login credentials");
      }
      rs.first();
      String firstName = rs.getString(1);
      String lastName = rs.getString(2);
      int id = rs.getInt(3);
      closeConnection();
      return new User(id, firstName, lastName);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
