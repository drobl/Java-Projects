package controllers;

import backendservices.CategoryService;
import expensetracker.JavaFXApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sceneswitcher.SceneSwitcher;
import sceneswitcher.SceneType;
import transaction.ExpenseCategory;
import transaction.IncomeCategory;


public class ExpenseCategoryDialogController extends DialogController {

  static private boolean isDialogReadonly = false;
  static private ExpenseCategory expenseCategoryBeingEdited = null;

  @FXML
  private TextField categoryNameField;

  static void setIsDialogReadonly(boolean readonly) {
    ExpenseCategoryDialogController.isDialogReadonly = readonly;
  }

  static void setExpenseCategoryBeingEdited(ExpenseCategory expenseCategory) {
    expenseCategoryBeingEdited = expenseCategory;
  }

  @FXML
  private void initialize() {
    if (expenseCategoryBeingEdited != null) {
      categoryNameField.setText(expenseCategoryBeingEdited.getName());
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
  public void saveExpenseCategory() {
    if (validateInput()) {
      String categoryName = categoryNameField.getText();
      try {
        boolean editMode = expenseCategoryBeingEdited != null;
        if (editMode) {
          CategoryService.editExpenseCategory(expenseCategoryBeingEdited.getId(), categoryName);
        } else {
          CategoryService.addExpenseCategory(categoryName);
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
    expenseCategoryBeingEdited = null;
    isDialogReadonly = false;
  }

}
