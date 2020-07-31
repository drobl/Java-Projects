package controllers;

import backendservices.LoginService;
import exceptions.LoginException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import user.User;
// =======
import expensetracker.JavaFXApp;
import sceneswitcher.SceneSwitcher;
import sceneswitcher.SceneType;

/**
 * Acts as a logical controller of the Login Scene. Responds to user interaction (e.g. button
 * clicks) and logs the user in if the correct credentials are entered
 * 
 * @author Marko Zunic
 */
public class LoginSceneController {

  // FXML will automatically bind these variables to their corresponding GUI components from
  // loginScene.fxml
  // note that the name of the variable must be equal to the id of the component it references
  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Label invalidCredentials;

  public LoginSceneController() {}

  @FXML
  private void initialize() {}

  /**
   * Obtains username and password typed in by user and checks in database whether the credentials
   * are correct If that is not the case, an error message is shown
   */
  @FXML
  public void tryLogin() {
    String username = usernameField.getText();
    String password = passwordField.getText();
    try {
      // TODO: set this user as the current user somehow and go to dashboard
      User user = LoginService.getUserByLoginCredentials(username, password);
      JavaFXApp.setCurrentUser(user);
      invalidCredentials.setVisible(false);
      SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
      sceneSwitcher.switchToScene(SceneType.HOME_SCENE);
    } catch (LoginException e) {
      invalidCredentials.setVisible(true);
      System.out.print(e.getMessage());
      e.printStackTrace();
    }
    System.out.println("Logging in as " + username + "...");
    System.out.println("Welcome back " + username);

  }


}
