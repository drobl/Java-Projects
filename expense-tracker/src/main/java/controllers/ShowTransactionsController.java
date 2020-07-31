package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

// DUMMY CONTROLLER, CURRENTLY ONLY FOR TESTING
public class ShowTransactionsController implements Initializable {

  @FXML
  private Label label1;

  public SimpleStringProperty textToDisplay = new SimpleStringProperty("");

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // The text that the label displays is bound to the string property value
    label1.textProperty().bind(textToDisplay);
  }


}
