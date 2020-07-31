package expensetracker;

import javafx.application.Application;
import javafx.stage.Stage;
import sceneswitcher.SceneSwitcher;
import sceneswitcher.SceneType;
import user.User;



public class JavaFXApp extends Application {
  private static SceneSwitcher sceneSwitcher;
  private static User currentUser;

  public static SceneSwitcher getSceneSwitcher() {
    return sceneSwitcher;
  }

  public static User getCurrentUser() {
    return currentUser;
  }

  public static void setCurrentUser(User user) {
    currentUser = user;
  }

  @Override
  public void start(Stage stage) {
    try {
      sceneSwitcher = new SceneSwitcher(stage);
      sceneSwitcher.switchToScene(SceneType.LOGIN_SCENE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch();
  }
}
