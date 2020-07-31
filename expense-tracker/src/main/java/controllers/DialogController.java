package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Generic controller for FXML scenes which are actually dialogs. Provides template methods for
 * validation.
 */
public abstract class DialogController {

  @FXML
  protected Label errorMessage;

  protected String validationErrorMsg;

  // Template method pattern implementation
  protected boolean validateInput() {
    try {
      boolean isInputValid = checkIfInputValid();
      if (!isInputValid) {
        displayValidationErrorMsg();
      }
      return isInputValid;
    } catch (Exception e) {
      e.printStackTrace();
      validationErrorMsg = "An unexpected error occurred";
      return false;
    }
  }

  public abstract boolean checkIfInputValid();

  protected void displayValidationErrorMsg() {
    errorMessage.setVisible(true);
    errorMessage.setText(validationErrorMsg);
  }
}
