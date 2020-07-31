package controllers;

import account.Account;
import account.AccountType;
import account.CashAccount;
import account.Currency;
import account.CurrencyConverter;
import account.SavingsAccount;
import backendservices.AccountService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import notifications.AccountWarnings;
import notifications.ConversionAlert;
import notifications.NearZeroWarningDecorator;
import user.User;
import expensetracker.JavaFXApp;
import sceneswitcher.SceneSwitcher;
import sceneswitcher.SceneType;

public class AccountDialogController extends DialogController {

  static private boolean isDialogReadonly = false;
  static private Account accountBeingEdited = null;

  static void setIsDialogReadonly(boolean readonly) {
    AccountDialogController.isDialogReadonly = readonly;
  }

  static public void setAccountBeingEdited(Account account) {
    accountBeingEdited = account;
  }

  private AccountWarnings notifier = new notifications.AccountWarnings();
  private User currentUser = JavaFXApp.getCurrentUser();

  @FXML
  private TextField accountNumberField;
  @FXML
  private ChoiceBox<AccountType> accountTypeChoiceBox;
  @FXML
  private TextField accountNameField;
  @FXML
  private TextField accountBalanceField;
  @FXML
  private Label minimumBalanceLabel;
  @FXML
  private TextField minimumBalanceField;
  @FXML
  private Label expiryDateLabel;
  @FXML
  private DatePicker expiryDatePicker;
  @FXML
  private Label currencyLabel;
  @FXML
  private ChoiceBox<Currency> currencyChoiceBox;
  @FXML
  private Button saveButton;

  public AccountDialogController() {}

  @FXML
  private void initialize() {
    initializeChoiceBox();
    if (accountBeingEdited != null) {
      fillInputFieldsWithAccountsValues();
    }
    if (isDialogReadonly) {
      disableAllInputFields();
    }
  }

  private void initializeChoiceBox() {
    accountTypeChoiceBox.setItems(FXCollections.observableArrayList(AccountType.values()));
    currencyChoiceBox.setItems(FXCollections.observableArrayList((Currency.values())));
    // expiry date fields should be visible if user is adding/editing a savings account:
    // same thing for currency, if it's a cash account
    ChangeListener<AccountType> choiceBoxChangeListener =
        (observable, oldSelection, newSelection) -> {
          boolean isSavingsAccount = newSelection == AccountType.SAVINGS_ACCOUNT;
          boolean isCashAccount = newSelection == AccountType.CASH_ACCOUNT;
          expiryDateLabel.setVisible(isSavingsAccount);
          expiryDatePicker.setVisible(isSavingsAccount);
          currencyLabel.setVisible(isCashAccount);
          currencyChoiceBox.setVisible(isCashAccount);
        };
    accountTypeChoiceBox.getSelectionModel().selectedItemProperty()
        .addListener(choiceBoxChangeListener);
  }

  private void fillInputFieldsWithAccountsValues() {
    if (accountBeingEdited != null) {
      accountNumberField.setText(String.valueOf((accountBeingEdited.getAccountNumber())));
      accountBalanceField.setText(String.valueOf(accountBeingEdited.getBalance()));
      accountNameField.setText(accountBeingEdited.getAccountName());
      minimumBalanceField.setText(String.valueOf(accountBeingEdited.getMinimumBalance()));
      if (accountBeingEdited instanceof SavingsAccount) {
        accountTypeChoiceBox.getSelectionModel().select(AccountType.SAVINGS_ACCOUNT);
        expiryDateLabel.setVisible(true);
        expiryDatePicker.setVisible(true);
        SavingsAccount savingsAccount = (SavingsAccount) accountBeingEdited;
        java.sql.Date expiryLocalDate = (java.sql.Date) savingsAccount.getExpireDate();
        expiryDatePicker.setValue(expiryLocalDate.toLocalDate());
      }
      if (accountBeingEdited instanceof CashAccount) {
        accountTypeChoiceBox.getSelectionModel().select(AccountType.CASH_ACCOUNT);
        CashAccount cashAccount = (CashAccount) accountBeingEdited;
        currencyChoiceBox.getSelectionModel().select(cashAccount.getCurrency());
      }
      // account type cannot be changed in edit mode:
      accountTypeChoiceBox.setDisable(true);
    }
  }

  private void disableAllInputFields() {
    accountTypeChoiceBox.setDisable(true);
    accountNameField.setDisable(true);
    accountBalanceField.setDisable(true);
    minimumBalanceField.setDisable(true);
    expiryDatePicker.setDisable(true);
    saveButton.setVisible(false);
  }

  /**
   * Reads all the values typed in by the user in the fields and saves a new account in the database
   * with the according values
   */
  @FXML
  public void saveAccount() {
    if (validateInput()) {
      String accountName = accountNameField.getText();
      double accountBalance = Double.parseDouble(accountBalanceField.getText());
      double minimumBalance = Double.parseDouble(minimumBalanceField.getText());
      AccountType selectedAccountType = accountTypeChoiceBox.getValue();
      LocalDate localDate = expiryDatePicker.getValue();
      Currency selectedCurrency = currencyChoiceBox.getValue();
      try {
        boolean editMode = accountBeingEdited != null;
        System.out.println("Accounttype " + selectedAccountType.getLabel());
        switch (selectedAccountType) {
          case SAVINGS_ACCOUNT:
            Date expiryDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (editMode) {
              AccountService.editSavingsAccount(accountBeingEdited.getAccountNumber(), accountName,
                  accountBalance, minimumBalance, expiryDate);
            } else {
              AccountService.addSavingsAccount(accountName, accountBalance, minimumBalance,
                  expiryDate);
            }
            break;
          case CASH_ACCOUNT:
            double balanceInEuros =
                CurrencyConverter.convertToEuro(selectedCurrency, accountBalance);
            if (editMode) {
              AccountService.editCashAccount(accountBeingEdited.getAccountNumber(), accountName,
                  balanceInEuros, minimumBalance, Currency.EUR);
            } else {
              // System.out.println("AccDialogController -> Gotchya");
              AccountService.addCashAccount(accountName, balanceInEuros, minimumBalance,
                  Currency.EUR);
            }
            break;
        }
        closeDialog();
      } catch (IllegalArgumentException e) {
        System.out.print(e.getMessage());
        e.printStackTrace();
      }
    }
  }

  public boolean checkIfInputValid() {
    String accountName = accountNameField.getText();
    String accountBalanceString = accountBalanceField.getText();
    String minimumBalanceString = minimumBalanceField.getText();
    AccountType selectedAccountType = accountTypeChoiceBox.getValue();
    if (accountName == null || accountName.trim().length() == 0) {
      validationErrorMsg = "Account name must not be null";
      return false;
    }
    if (!accountBalanceString.matches("-?\\d+(\\.\\d+)?")) {
      validationErrorMsg = "Initial balance must be a numeric value";
      return false;
    }
    if (!minimumBalanceString.matches("-?\\d+(\\.\\d+)?")) {
      validationErrorMsg = "Minimum balance must be a numeric value";
      return false;
    }
    if (Double.parseDouble(minimumBalanceString) > Double.parseDouble(accountBalanceString)) {
      validationErrorMsg = "Initial balance cannot be lower than minimum balance";
      return false;
    }
    if (selectedAccountType == null) {
      validationErrorMsg = "Account type must be selected";
      return false;
    }
    if (selectedAccountType == AccountType.SAVINGS_ACCOUNT) {
      LocalDate expiryDate = expiryDatePicker.getValue();
      if (expiryDate == null) {
        validationErrorMsg = "Expiry date must be selected";
        return false;
      }
      if (expiryDate.isBefore(LocalDate.now())) {
        validationErrorMsg = "Expiry date cannot be in the past";
        return false;
      }
    } else if (selectedAccountType == AccountType.CASH_ACCOUNT) {
      if (currencyChoiceBox.getValue() == null) {
        validationErrorMsg = "Please select a currency";
        return false;
      }
      if (Double.parseDouble(minimumBalanceString) < 0) {
        validationErrorMsg = "Cash accounts cannot have a negative minimum balance";
        return false;
      }
    }
    if (currencyChoiceBox.getValue() != Currency.EUR) {
      new ConversionAlert(notifier, accountName, currencyChoiceBox.getValue(),
          Double.parseDouble(accountBalanceString));
    }
    return true;
  }

  private void closeDialog() {
    Stage stage = (Stage) accountNumberField.getScene().getWindow();
    stage.close();
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.switchToScene(SceneType.HOME_SCENE);
    accountBeingEdited = null;
    isDialogReadonly = false;
  }

}
