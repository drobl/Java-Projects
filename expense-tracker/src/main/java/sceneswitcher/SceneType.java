package sceneswitcher;

/**
 * Represents a scene (view) in the application. Its' main purpose is to indicate to what scene
 * should the {@link sceneswitcher.SceneSwitcher } switch. For every possible scene in the app,
 * there should be a corresponding enum value here.
 */
public enum SceneType {
  LOGIN_SCENE("Login", "fxml/loginScene.fxml"), HOME_SCENE("Home",
      "fxml/homeScene.fxml"), ACCOUNT_DIALOG("Add Account",
          "fxml/accountDialog.fxml"), EXPENSE_CATEGORY_DIALOG("Add expense category",
              "fxml/expenseCategoryDialog.fxml"), INCOME_CATEGORY_DIALOG("Add income category",
                  "fxml/incomeCategoryDialog.fxml"), INCOME_DIALOG("Add Income",
                      "fxml/incomeDialog.fxml"), EXPENSE_DIALOG("Add Expense",
                          "fxml/expenseDialog.fxml"), ADD_USER_SCENE("Add User",
                              "fxml/addUserScene.fxml"), SHOW_TRANSACTIONS_SCENE(
                                  "Show Transactions",
                                  "fxml/showTransactionsScene.fxml"), SHOW_REPORT("Show Report",
                                      "fxml/reportScene.fxml");

  private String title;
  private String filePath;

  SceneType(String title, String filePath) {
    this.title = title;
    this.filePath = filePath;
  }

  /**
   * Returns the text that is displayed in the uppermost bar of the app when the user is in that
   * scene.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the path of the corresponding FXML file of the scene. The path is relative to the
   * "resources" folder and ends with the file extension ".fxml"
   * 
   * @see <a href=
   *      "https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/doc-files/introduction_to_fxml.html">FXML</a>
   */
  public String getFilePath() {
    return filePath;
  }

}
