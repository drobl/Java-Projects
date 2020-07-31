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
import notifications.AccountWarnings;
import notifications.IncurredFeeDecorator;
import notifications.InvalidWithdrawal;
import notifications.InvalidWithdrawalMinimum;
import sceneswitcher.SceneSwitcher;
import sceneswitcher.SceneType;
import account.CashAccount;
import transaction.*;
import user.User;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


public class ExpenseDialogController extends DialogController {
  static private boolean isDialogReadonly = false;
  private AccountWarnings notifier = new AccountWarnings();


  static void setIsDialogReadonly(boolean readonly) {
    ExpenseDialogController.isDialogReadonly = readonly;
  }

  static private Expense expenseBeingEdited = null;

  static void setExpenseBeingEdited(Expense expense) {
    expenseBeingEdited = expense;
  }

  private User currentUser = JavaFXApp.getCurrentUser();
  private List<ExpenseCategory> expenseCategoryList;

  @FXML
  private ChoiceBox<Account> accountChoiceBox;
  @FXML
  private DatePicker transactionDatePicker;
  @FXML
  private TextField transactionAmountField;
  @FXML
  private TextField transactionDescriptionField;
  @FXML
  private ChoiceBox<ExpenseCategory> expenseCategoryChoiceBox;
  @FXML
  private Button saveButton;

  public ExpenseDialogController() {}

  @FXML
  private void initialize() {
    initializeChoiceBox();
    if (isDialogReadonly) {
      disableAllInputFields();
    }
    if (expenseBeingEdited != null) {
      fillInputFieldsWithExpenseValues();
    }
  }

  private void initializeChoiceBox() {
    expenseCategoryList = CategoryService.getExpenseCategoryList();
    for (ExpenseCategory ec : expenseCategoryList) {
      expenseCategoryChoiceBox.getItems().add(ec);
    }
    accountChoiceBox.setItems(FXCollections.observableArrayList(currentUser.getAccountList()));
  }

  private void disableAllInputFields() {
    accountChoiceBox.setDisable(true);
    transactionDatePicker.setDisable(true);
    transactionDescriptionField.setDisable(true);
    expenseCategoryChoiceBox.setDisable(true);
    transactionAmountField.setDisable(true);
    saveButton.setVisible(false);
  }

  @FXML
  public void saveExpense() {
    if (validateInput()) {
      Account selectedAccount = accountChoiceBox.getValue();
      LocalDate localDate = transactionDatePicker.getValue();
      Date expenseDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
      double expenseAmount = Double.parseDouble(transactionAmountField.getText());
      String expenseDescription = transactionDescriptionField.getText();
      ExpenseCategory expenseCategory = expenseCategoryChoiceBox.getValue();
      double expenseAmountWithFee = expenseAmount;
      try {
        if (expenseBeingEdited == null) {
          if (!(selectedAccount instanceof CashAccount)) {
            expenseAmountWithFee =
                Expense.calculateTransactionAmountWithFee(selectedAccount, expenseAmount);
            new IncurredFeeDecorator(notifier, selectedAccount.getAccountName(), expenseAmount,
                expenseAmountWithFee);

          }
          if (selectedAccount.getBalance() < expenseAmountWithFee) { // check if there are enough
                                                                     // funds to cover the expense
                                                                     // with fees
            new InvalidWithdrawal(notifier, selectedAccount.getAccountName(), expenseAmountWithFee,
                selectedAccount.getBalance());
            return;
          }
          if (selectedAccount.getMinimumBalance() != 0
              && (selectedAccount.getBalance() - expenseAmountWithFee) < selectedAccount
                  .getMinimumBalance()) { // check if the expense will not put account under minimum
            new InvalidWithdrawalMinimum(notifier, selectedAccount.getAccountName(),
                expenseAmountWithFee, selectedAccount.getMinimumBalance(),
                selectedAccount.getBalance());
            return;
          } else {
            Transaction newTransaction = TransactionService.addExpense(
                selectedAccount.getAccountNumber(), expenseDate, expenseAmount,
                expenseAmountWithFee, expenseCategory.getId(), expenseDescription);
            newTransaction.performTransaction(selectedAccount);
            AccountService.editAccount(selectedAccount);
          }
        } else {
          TransactionService.editExpense(expenseBeingEdited.getTransactionNumber(),
              selectedAccount.getAccountNumber(), expenseDate, expenseAmount,
              expenseCategory.getId(), expenseDescription);
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
      if (acc.getAccountNumber() == expenseBeingEdited.getAccountNumber()) {
        accountChoiceBox.getSelectionModel().select(acc);
      }
    }
    java.sql.Date expenseLocalDate = (java.sql.Date) expenseBeingEdited.getDate();
    transactionDatePicker.setValue(expenseLocalDate.toLocalDate());
    transactionDescriptionField.setText(expenseBeingEdited.getDescription());
    for (ExpenseCategory exCat : expenseCategoryList) {
      if (exCat.getId() == expenseBeingEdited.getExpenseCategoryId()) {
        expenseCategoryChoiceBox.setValue(exCat);
      }
    }
    transactionAmountField.setText(String.valueOf(expenseBeingEdited.getAmount()));
    transactionAmountField.setDisable(true);
  }

  public boolean checkIfInputValid() {
    Account selectedAccount = accountChoiceBox.getValue();
    String amountString = transactionAmountField.getText();
    LocalDate expenseDate = transactionDatePicker.getValue();
    ExpenseCategory expenseCategory = expenseCategoryChoiceBox.getValue();
    if (selectedAccount == null) {
      validationErrorMsg = "Please select an account";
      return false;
    }
    if (!amountString.matches("\\d+(\\.\\d+)?") || Double.parseDouble(amountString) <= 0) {
      validationErrorMsg = "Amount must be a positive number";
      return false;
    }
    if (expenseDate == null) {
      validationErrorMsg = "Please select an expense date";
      return false;
    }
    if (expenseCategory == null) {
      validationErrorMsg = "Please select a category";
      return false;
    }
    double amountWithFee = Expense.calculateTransactionAmountWithFee(selectedAccount,
        Double.parseDouble(amountString));
    if (!(selectedAccount instanceof CashAccount) && selectedAccount.getBalance() < amountWithFee) {
      validationErrorMsg =
          "Expense amount plus fee cannot exceed current balance on this account \nExpense amount with fee: "
              + amountWithFee;
      return false;
    }
    if (!(selectedAccount instanceof CashAccount)
        && selectedAccount.getMinimumBalance() > selectedAccount.getBalance() - amountWithFee) {
      validationErrorMsg =
          "This expense amount plus fee puts your account balance below your set minimum balance! \nExpense amount with fee: "
              + amountWithFee + "\nTransaction Not Possible";
      return false;
    }
    return true;
  }

  private void closeDialog() {
    Stage stage = (Stage) accountChoiceBox.getScene().getWindow();
    stage.close();
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.switchToScene(SceneType.HOME_SCENE);
    expenseBeingEdited = null;
    isDialogReadonly = false;
  }

}
