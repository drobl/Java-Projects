package controllers;

import backendservices.AccountService;
import backendservices.CategoryService;
import backendservices.TransactionService;
import expensetracker.JavaFXApp;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import observer.Observable;
import observer.Observer;
import transaction.*;
import account.Account;
import sceneswitcher.SceneSwitcher;
import sceneswitcher.SceneType;
import user.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.List;

/**
 * Acts as a logical controller of the Home Scene. Responds to user interaction (e.g. button clicks)
 * and also observes all {@link account.Account} objects of the currently logged in user (acts as an
 * "observer" in our Observer Design Pattern implementation).
 */
public class HomeSceneController implements Observer {


  private User currentUser;
  private ReportSceneController report = new ReportSceneController();

  @FXML
  private Label currentUsername;
  @FXML
  private Label currentBalance;
  @FXML
  private TableView<Transaction> transactionsTable;
  @FXML
  private TableView<Transaction> fixedTransactionsTable;
  @FXML
  private TableView<Account> accountsTable;
  @FXML
  private TableView<ExpenseCategory> expenseCategoryTable;
  @FXML
  private TableView<IncomeCategory> incomeCategoryTable;

  @FXML
  private Button viewAccountButton;
  @FXML
  private Button editAccountButton;
  @FXML
  private Button deleteAccountButton;
  @FXML
  private Button editTransactionButton;
  @FXML
  private Button deleteTransactionButton;
  @FXML
  private Button viewTransactionButton;
  @FXML
  private Button viewReportButton;
  @FXML
  private HBox accountButtonsBox;
  @FXML
  private HBox transactionButtonsBox;
  @FXML
  private TableColumn<Transaction, String> transactionAmountColumn;
  @FXML
  private TableColumn<Transaction, String> fixedTransactionAmountColumn;
  @FXML
  private Button editExpenseCategoryButton;
  @FXML
  private Button editIncomeCategoryButton;
  @FXML
  private Button deleteExpenseCategoryButton;
  @FXML
  private Button deleteIncomeCategoryButton;


  public HomeSceneController() {}

  @FXML
  private void initialize() {
    currentUser = JavaFXApp.getCurrentUser();
    currentUsername.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
    loadAccountsOfCurrentUserAndObserve();
    loadTransactionsOfCurrentUser();
    fillAccountTableWithCurrentUsersAccounts();
    fillExpenseCategoryTable();
    fillIncomeCategoryTable();
    fillTransactionsTableWithCurrentUsersTransactions();
    fillFixedTransactionsTableWithItems();
    performDueFixedTransactions();
    addTableEventListeners();
    currentBalance.setText(currentUser.getBalanceSum());

    accountButtonsBox.setSpacing(10);
    accountButtonsBox.setAlignment(Pos.TOP_LEFT);
    transactionButtonsBox.setSpacing(10);
    transactionButtonsBox.setAlignment(Pos.TOP_RIGHT);
  }

  private void loadAccountsOfCurrentUserAndObserve() {
    List<Account> accountListOfUser = AccountService.getAccountListOfUser(currentUser.getId());
    accountListOfUser.forEach(account -> account.registerObserver(this));
    currentUser.setAccountList(accountListOfUser);
  }

  private void loadTransactionsOfCurrentUser() {
    List<Account> accountListOfUser = currentUser.getAccountList();
    accountListOfUser.forEach(account -> {
      int accountNumber = account.getAccountNumber();
      List<Transaction> transactionList =
          TransactionService.getTransactionListOfAccount(accountNumber);
      account.setTransactionList(transactionList);
    });
  }


  private void fillAccountTableWithCurrentUsersAccounts() {
    List<Account> accountList = currentUser.getAccountList();
    ObservableList<Account> observableAccountList = FXCollections.observableList(accountList);
    accountsTable.setItems(observableAccountList);
  }

  private void fillTransactionsTableWithCurrentUsersTransactions() {
    List<Account> accountList = currentUser.getAccountList();
    List<Transaction> transactionList = new ArrayList<>();
    for (Account account : accountList) {
      transactionList.addAll(account.getNonFixedTransactionList());
    }
    ObservableList<Transaction> observableTransactionList =
        FXCollections.observableList(transactionList);
    transactionsTable.setItems(observableTransactionList);
    // Set the cell factory of the column with a custom TableCell to modify its behavior.
    transactionAmountColumn.setCellFactory(e -> new TableCell<Transaction, String>() {
      @Override
      public void updateItem(String item, boolean empty) {
        Transaction transaction = (Transaction) this.getTableRow().getItem();
        if (transaction != null) {
          String sign = transaction instanceof Income ? "+" : "-";
          Color textColor = transaction instanceof Income ? Color.GREEN : Color.RED;
          setText(sign + transaction.getAmount());
          setTextFill(textColor);
        }
      }
    });
  }

  private void fillFixedTransactionsTableWithItems() {
    List<Account> accountList = currentUser.getAccountList();
    List<Transaction> fixedTransactionList = new ArrayList<>();
    for (Account account : accountList) {
      fixedTransactionList.addAll(account.getFixedTransactionList());
    }
    ObservableList<Transaction> observableTransactionList =
        FXCollections.observableList(fixedTransactionList);
    fixedTransactionsTable.setItems(observableTransactionList);
    fixedTransactionAmountColumn.setCellFactory(e -> new TableCell<Transaction, String>() {
      @Override
      public void updateItem(String item, boolean empty) {
        Transaction transaction = (Transaction) this.getTableRow().getItem();
        if (transaction != null) {
          String sign = transaction instanceof Income ? "+" : "-";
          Color textColor = transaction instanceof Income ? Color.GREEN : Color.RED;
          setText(sign + transaction.getAmount());
          setTextFill(textColor);
        }
      }
    });
  }

  private void fillExpenseCategoryTable() {
    List<ExpenseCategory> categoryList = CategoryService.getExpenseCategoryList();
    ObservableList<ExpenseCategory> observableExpenseCategoryList =
        FXCollections.observableList(categoryList);
    expenseCategoryTable.setItems(observableExpenseCategoryList);
  }

  private void fillIncomeCategoryTable() {
    List<IncomeCategory> categoryList = CategoryService.getIncomeCategoryList();
    ObservableList<IncomeCategory> observableIncomeCategoryList =
        FXCollections.observableList(categoryList);
    incomeCategoryTable.setItems(observableIncomeCategoryList);
  }


  private void performDueFixedTransactions() {
    Date today = new Date();
    List<Account> accountList = currentUser.getAccountList();
    for (Account account : accountList) {
      for (Transaction fixedTransaction : account.getFixedTransactionList()) {
        Date nextDueDate;
        if (fixedTransaction instanceof FixedIncome) {
          nextDueDate = ((FixedIncome) fixedTransaction).getNextDueDate();
        } else {
          nextDueDate = ((FixedExpense) fixedTransaction).getNextDueDate();
        }
        if (nextDueDate.before(today)) {
          if (fixedTransaction instanceof FixedIncome) {
            ((FixedIncome) fixedTransaction).updateNextDueDate();
          } else {
            ((FixedExpense) fixedTransaction).updateNextDueDate();
          }
          fixedTransaction.performTransaction(account);
          TransactionService.updateDueDate(fixedTransaction);
          AccountService.editAccount(account);
        }
      }
    }
  }

  private void addTableEventListeners() {
    // add event listener -> when clicking on account table row:
    accountsTable.setOnMousePressed((EventHandler<Event>) event -> {
      viewAccountButton.setDisable(false);
      editAccountButton.setDisable(false);
      deleteAccountButton.setDisable(false);
    });
    // add event listener -> when clicking on transaction table row:
    transactionsTable.setOnMousePressed((EventHandler<Event>) event -> {
      viewTransactionButton.setDisable(false);
      editTransactionButton.setDisable(false);
      deleteTransactionButton.setDisable(false);
    });

    incomeCategoryTable.setOnMousePressed((EventHandler<Event>) event -> {
      editIncomeCategoryButton.setDisable(false);
      deleteIncomeCategoryButton.setDisable(false);
    });

    expenseCategoryTable.setOnMousePressed((EventHandler<Event>) event -> {
      editExpenseCategoryButton.setDisable(false);
      deleteExpenseCategoryButton.setDisable(false);
    });
  }

  /**
   * Switches to the scene where incomes or expenses are viewed.
   */
  public void openTransactionDialogView(ActionEvent actionEvent) {
    Transaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    if (selectedTransaction instanceof Income) {
      IncomeDialogController.setIsDialogReadonly(true);
      IncomeDialogController.setIncomeBeingEdited((Income) selectedTransaction);
      sceneSwitcher.openSceneAsDialog(SceneType.INCOME_DIALOG, 500, 500);
    } else if (selectedTransaction instanceof Expense) {
      ExpenseDialogController.setIsDialogReadonly(true);
      ExpenseDialogController.setExpenseBeingEdited((Expense) selectedTransaction);
      sceneSwitcher.openSceneAsDialog(SceneType.EXPENSE_DIALOG, 500, 500);
    }
  }

  /**
   * Switches to the scene where transactions are edited. The transaction selected in the table is
   * the one that will be edited.
   */
  public void openTransactionDialogEdit(ActionEvent actionEvent) {
    Transaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    if (selectedTransaction instanceof Income) {
      IncomeDialogController.setIsDialogReadonly(false);
      IncomeDialogController.setIncomeBeingEdited((Income) selectedTransaction);
      sceneSwitcher.openSceneAsDialog(SceneType.INCOME_DIALOG, 500, 500);
    } else if (selectedTransaction instanceof Expense) {
      ExpenseDialogController.setIsDialogReadonly(false);
      ExpenseDialogController.setExpenseBeingEdited((Expense) selectedTransaction);
      sceneSwitcher.openSceneAsDialog(SceneType.EXPENSE_DIALOG, 500, 500);
    }
  }

  /**
   * Switches to the scene where expenses are added.
   */
  public void openExpenseDialogNew() {
    ExpenseDialogController.setIsDialogReadonly(false);
    ExpenseDialogController.setExpenseBeingEdited(null);
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.openSceneAsDialog(SceneType.EXPENSE_DIALOG, 500, 500);
  }

  /**
   * Switches to the scene where incomes are added.
   */
  public void openIncomeDialogNew(ActionEvent actionEvent) {
    IncomeDialogController.setIsDialogReadonly(false);
    IncomeDialogController.setIncomeBeingEdited(null);
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.openSceneAsDialog(SceneType.INCOME_DIALOG, 500, 500);
  }

  /**
   * Opens the account dialog in read-only mode, with the account selected in the table of the home
   * scene the one being shown.
   */
  public void openAccountDialogView() {
    Account selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
    AccountDialogController.setAccountBeingEdited(selectedAccount);
    AccountDialogController.setIsDialogReadonly(true);
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.openSceneAsDialog(SceneType.ACCOUNT_DIALOG, 500, 500);
  }

  /**
   * Opens the account dialog where a new dialog may be creat
   */
  public void openAccountDialogNew() {
    AccountDialogController.setAccountBeingEdited(null);
    AccountDialogController.setIsDialogReadonly(false);
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.openSceneAsDialog(SceneType.ACCOUNT_DIALOG, 500, 500);
  }


  /**
   * Opens the account dialog in edit mode, with the account selected in the table of the home scene
   * the one being edited.
   */
  public void openAccountDialogEdit(ActionEvent actionEvent) {
    Account selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
    AccountDialogController.setAccountBeingEdited(selectedAccount);
    AccountDialogController.setIsDialogReadonly(false);
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.openSceneAsDialog(SceneType.ACCOUNT_DIALOG, 500, 500);
  }

  /**
   * Opens Report view
   */
  public void openReportView() {
    // report.showReport();
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.openSceneAsDialog(SceneType.SHOW_REPORT, 1300, 800);
  }

  /**
   * Asks the user to confirm whether the account selected in the table should be deleted and does
   * if the user confirms it
   */
  public void confirmDeleteAccount(ActionEvent actionEvent) {
    Alert alert = new Alert(AlertType.CONFIRMATION, "Do you really want to delete this account?",
        ButtonType.YES, ButtonType.NO);
    alert.setHeaderText("Warning");
    alert.setTitle("Delete account?");
    alert.showAndWait();
    if (alert.getResult() == ButtonType.YES) {
      Account selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
      AccountService.deleteAccount(selectedAccount.getAccountNumber());
      currentUser.removeAccount(selectedAccount);
    }
    fillAccountTableWithCurrentUsersAccounts();
    fillTransactionsTableWithCurrentUsersTransactions();
  }

  /**
   * Opens the dialog where a new expense category can be created
   */
  public void openExpenseCategoryDialogNew(ActionEvent actionEvent) {
    ExpenseCategoryDialogController.setIsDialogReadonly(false);
    ExpenseCategoryDialogController.setExpenseCategoryBeingEdited(null);
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.openSceneAsDialog(SceneType.EXPENSE_CATEGORY_DIALOG, 500, 500);
  }

  /**
   * Opens the dialog where the expense category selected in the table can be edited
   */
  public void openExpenseCategoryDialogEdit(ActionEvent actionEvent) {
    ExpenseCategory expenseCategory = expenseCategoryTable.getSelectionModel().getSelectedItem();
    ExpenseCategoryDialogController.setIsDialogReadonly(false);
    ExpenseCategoryDialogController.setExpenseCategoryBeingEdited(expenseCategory);
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.openSceneAsDialog(SceneType.EXPENSE_CATEGORY_DIALOG, 500, 500);
  }

  /**
   * Opens the dialog where a new income category can be created
   */
  public void openIncomeCategoryDialogNew(ActionEvent actionEvent) {
    IncomeCategoryDialogController.setIsDialogReadonly(false);
    IncomeCategoryDialogController.setIncomeCategoryBeingEdited(null);
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.openSceneAsDialog(SceneType.INCOME_CATEGORY_DIALOG, 500, 500);
  }


  /**
   * Opens the dialog where the income category selected in the table can be edited
   */
  public void openIncomeCategoryDialogEdit(ActionEvent actionEvent) {
    IncomeCategory incomeCategory = incomeCategoryTable.getSelectionModel().getSelectedItem();
    IncomeCategoryDialogController.setIsDialogReadonly(false);
    IncomeCategoryDialogController.setIncomeCategoryBeingEdited(incomeCategory);
    SceneSwitcher sceneSwitcher = JavaFXApp.getSceneSwitcher();
    sceneSwitcher.openSceneAsDialog(SceneType.INCOME_CATEGORY_DIALOG, 500, 500);
  }

  /**
   * Asks the user to confirm whether the income category selected in the table should be deleted
   * and does so if the user confirms it
   */
  public void confirmDeleteIncomeCategory() {
    Alert alert = new Alert(AlertType.CONFIRMATION,
        "Do you really want to delete this category? This will also delete related transactions.",
        ButtonType.YES, ButtonType.NO);
    alert.setHeaderText("Warning");
    alert.setTitle("Delete category?");
    alert.showAndWait();
    if (alert.getResult() == ButtonType.YES) {
      IncomeCategory selectedCategory = incomeCategoryTable.getSelectionModel().getSelectedItem();
      CategoryService.deleteIncomeCategory(selectedCategory.getId());
    }
    fillIncomeCategoryTable();
    loadTransactionsOfCurrentUser();
    fillTransactionsTableWithCurrentUsersTransactions();
  }

  /**
   * Asks the user to confirm whether the expense category selected in the table should be deleted
   * and does so if the user confirms it
   */
  public void confirmDeleteExpenseCategory() {
    Alert alert = new Alert(AlertType.CONFIRMATION,
        "Do you really want to delete this category? This will also delete related transactions",
        ButtonType.YES, ButtonType.NO);
    alert.setHeaderText("Warning");
    alert.setTitle("Delete category?");
    alert.showAndWait();
    if (alert.getResult() == ButtonType.YES) {
      ExpenseCategory expenseCategory = expenseCategoryTable.getSelectionModel().getSelectedItem();
      CategoryService.deleteExpenseCategory(expenseCategory.getId());
    }
    fillExpenseCategoryTable();
    loadTransactionsOfCurrentUser();
    fillTransactionsTableWithCurrentUsersTransactions();

  }

  /**
   * Asks the user to confirm whether the transaction selected in the table should be deleted and
   * does if the user confirms it
   */
  public void confirmDeleteTransaction(ActionEvent actionEvent) {
    Alert alert = new Alert(AlertType.CONFIRMATION,
        "Do you really want to delete this transaction?", ButtonType.YES, ButtonType.NO);
    alert.setHeaderText("Warning");
    alert.setTitle("Delete transaction?");
    alert.showAndWait();
    if (alert.getResult() == ButtonType.YES) {
      Transaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();
      TransactionService.deleteTransaction(selectedTransaction.getTransactionNumber());
      for (Account account : currentUser.getAccountList()) {
        if (account.getAccountNumber() == selectedTransaction.getAccountNumber()) {
          selectedTransaction.undoTransaction(account);
        }
      }
    }
    fillTransactionsTableWithCurrentUsersTransactions();
  }

  public void update(Observable o) {
    currentBalance.setText(currentUser.getBalanceSum());
  }
}

