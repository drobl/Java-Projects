package controllers;

import backendservices.AccountService;
import backendservices.CategoryService;
import backendservices.TransactionService;
import exceptions.TransactionException;
import expensetracker.JavaFXApp;
import account.Account;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sceneswitcher.SceneSwitcher;
import sceneswitcher.SceneType;
import transaction.*;
import user.User;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


public class IncomeDialogController extends DialogController {
  static private boolean isDialogReadonly = false;

  static void setIsDialogReadonly(boolean readonly) {
    IncomeDialogController.isDialogReadonly = readonly;
  }

  static private Income incomeBeingEdited = null;

  static void setIncomeBeingEdited(Income income) {
    incomeBeingEdited = income;
  }

  private User currentUser = JavaFXApp.getCurrentUser();
  private List<IncomeCategory> incomeCategoryList;

  @FXML
  private ChoiceBox<Account> accountChoiceBox;
  @FXML
  private DatePicker transactionDatePicker;
  @FXML
  private TextField transactionAmountField;
  @FXML
  private TextField transactionDescriptionField;
  @FXML
  private ChoiceBox<IncomeCategory> incomeCategoryChoiceBox;
  @FXML
  private Button saveButton;

  public IncomeDialogController() {}

  @FXML
  private void initialize() {
    initializeChoiceBox();
    if (isDialogReadonly) {
      disableAllInputFields();
    }
    if (incomeBeingEdited != null) {
      fillInputFieldsWithExpenseValues();
    }
  }

  private void initializeChoiceBox() {
    incomeCategoryList = CategoryService.getIncomeCategoryList();
    for (IncomeCategory ec : incomeCategoryList) {
      incomeCategoryChoiceBox.getItems().add(ec);
    }
    accountChoiceBox.setItems(FXCollections.observableArrayList(currentUser.getAccountList()));
  }

  private void disableAllInputFields() {
    accountChoiceBox.setDisable(true);
    transactionDatePicker.setDisable(true);
    transactionDescriptionField.setDisable(true);
    incomeCategoryChoiceBox.setDisable(true);
    transactionAmountField.setDisable(true);
    saveButton.setVisible(false);
  }

  @FXML
  public void saveIncome() {
    if (validateInput()) {
      Account selectedAccount = accountChoiceBox.getValue();
      LocalDate localDate = transactionDatePicker.getValue();
      Date expenseDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
      double incomeAmount = Double.parseDouble(transactionAmountField.getText());
      String incomeDescription = transactionDescriptionField.getText();
      IncomeCategory incomeCategory = incomeCategoryChoiceBox.getValue();
      try {
        if (incomeBeingEdited == null) {
          Transaction newTransaction =
              TransactionService.addIncome(selectedAccount.getAccountNumber(), expenseDate,
                  incomeAmount, incomeCategory.getId(), incomeDescription);
          newTransaction.performTransaction(selectedAccount);
          AccountService.editAccount(selectedAccount);
        } else {
          TransactionService.editIncome(incomeBeingEdited.getTransactionNumber(),
              selectedAccount.getAccountNumber(), expenseDate, incomeAmount, incomeCategory.getId(),
              incomeDescription);
        }
      } catch (TransactionException e) {
        System.out.print(e.getMessage());
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
      closeDialog();
    }
  }

  private void fillInputFieldsWithExpenseValues() {
    for (Account acc : currentUser.getAccountList()) {
      if (acc.getAccountNumber() == incomeBeingEdited.getAccountNumber()) {
        accountChoiceBox.getSelectionModel().select(acc);
      }
    }
    java.sql.Date expenseLocalDate = (java.sql.Date) incomeBeingEdited.getDate();
    transactionDatePicker.setValue(expenseLocalDate.toLocalDate());
    transactionDescriptionField.setText(incomeBeingEdited.getDescription());
    for (IncomeCategory inCat : incomeCategoryList) {
      if (inCat.getId() == incomeBeingEdited.getIncomeCategoryId()) {
        incomeCategoryChoiceBox.setValue(inCat);
      }
    }
    transactionAmountField.setText(String.valueOf(incomeBeingEdited.getAmount()));
    transactionAmountField.setDisable(true);
  }

  public boolean checkIfInputValid() {
    Account selectedAccount = accountChoiceBox.getValue();
    String amountString = transactionAmountField.getText();
    LocalDate expenseDate = transactionDatePicker.getValue();
    IncomeCategory incomeCategory = incomeCategoryChoiceBox.getValue();
    if (selectedAccount == null) {
      validationErrorMsg = "Please select an account";
      return false;
    }
    if (!amountString.matches("\\d+(\\.\\d+)?") || Double.parseDouble(amountString) <= 0) {
      validationErrorMsg = "Amount must be a positive number";
      return false;
    }
    if (expenseDate == null) {
      validationErrorMsg = "Please select an income date";
      return false;
    }
    if (incomeCategory == null) {
      validationErrorMsg = "Please select a category";
      return false;
    }
    return true;
  }

  private void closeDialog() {
    Stage stage = (Stage) accountChoiceBox.getScene().getWindow();
    stage.close();
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.switchToScene(SceneType.HOME_SCENE);
    incomeBeingEdited = null;
    isDialogReadonly = false;
  }

}
