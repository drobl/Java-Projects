package sceneswitcher;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

/**
 * Used for switching the currently displayed scene of the app
 */
public class SceneSwitcher {
  /**
   * The stage whose scenes get switched
   */
  private Stage stage;
  private static final int SCENE_WIDTH = 900;
  private static final int SCENE_HEIGHT = 610;

  public SceneSwitcher(Stage stage) {
    this.stage = stage;
  }

  /**
   * Loads the FXML file of the provided scene type and sets it as the current scene of the stage
   * 
   * @param sceneType the {@link SceneType} whose FXML file path will be loaded
   */
  public void switchToScene(SceneType sceneType) {
    String sceneFileName = sceneType.getFilePath();
    URL fxmlFileURL = getClass().getClassLoader().getResource(sceneFileName);
    try {
      if (fxmlFileURL != null) {
        Parent root = FXMLLoader.load(fxmlFileURL);
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        stage.setTitle(sceneType.getTitle());
        stage.setScene(scene);
        stage.show();
      } else {
        System.out.println("Failed to load FXML file.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the FXML file of the provided scene type and display it in a new separate stage (separate
   * window).
   * 
   * @param sceneType the {@link SceneType} whose FXML file path will be loaded
   */
  public void openSceneAsDialog(SceneType sceneType, int dialogWidth, int dialogHeight) {
    String sceneFileName = sceneType.getFilePath();
    URL fxmlFileURL = getClass().getClassLoader().getResource(sceneFileName);
    try {
      if (fxmlFileURL != null) {
        Parent root = FXMLLoader.load(fxmlFileURL);
        Stage separateStage = new Stage();
        Scene scene = new Scene(root, dialogWidth, dialogHeight);
        separateStage.setTitle(sceneType.getTitle());
        separateStage.setScene(scene);
        separateStage.show();
      } else {
        System.out.println("Failed to load FXML file.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
