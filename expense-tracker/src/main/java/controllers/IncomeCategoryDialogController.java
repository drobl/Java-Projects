package controllers;

import backendservices.CategoryService;
import expensetracker.JavaFXApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sceneswitcher.SceneSwitcher;
import sceneswitcher.SceneType;
import transaction.IncomeCategory;

public class IncomeCategoryDialogController extends DialogController {

  static private boolean isDialogReadonly = false;
  static private IncomeCategory incomeCategoryBeingEdited = null;


  @FXML
  private TextField categoryNameField;

  static void setIsDialogReadonly(boolean readonly) {
    IncomeCategoryDialogController.isDialogReadonly = readonly;
  }

  static void setIncomeCategoryBeingEdited(IncomeCategory incomeCategory) {
    incomeCategoryBeingEdited = incomeCategory;
  }

  @FXML
  private void initialize() {
    if (incomeCategoryBeingEdited != null) {
      categoryNameField.setText(incomeCategoryBeingEdited.getName());
    }
  }


  public boolean checkIfInputValid() {
    String categoryName = categoryNameField.getText();

    if (categoryName == null || categoryName.trim().length() == 0) {
      validationErrorMsg = "Category name must not be null";
      return false;
    }
    return true;
  }

  @FXML
  public void saveIncomeCategory() {
    if (validateInput()) {
      String categoryName = categoryNameField.getText();
      try {
        boolean editMode = incomeCategoryBeingEdited != null;
        if (editMode) {
          CategoryService.editIncomeCategory(incomeCategoryBeingEdited.getId(), categoryName);
        } else {
          CategoryService.addIncomeCategory(categoryName);
        }
        closeDialog();
      } catch (IllegalArgumentException e) {
        System.out.print(e.getMessage());
        e.printStackTrace();
      }
    }
  }

  private void closeDialog() {
    Stage stage = (Stage) categoryNameField.getScene().getWindow();
    stage.close();
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.switchToScene(SceneType.HOME_SCENE);
    incomeCategoryBeingEdited = null;
    isDialogReadonly = false;
  }

}
