package transaction;

import backendservices.AccountService;
import backendservices.CategoryService;
import controllers.AccountDialogController;
import controllers.HomeSceneController;
import expensetracker.JavaFXApp;
import junit.framework.TestCase;
import user.User;
import java.util.Date;
import java.util.List;

public class ExpenseCategoryTest extends TestCase {

  public void testCreateNewExpenseCategory() {
    ExpenseCategory testExpenseCategory = CategoryService.addExpenseCategory("University");
    List<ExpenseCategory> expenseCategoryList = CategoryService.getExpenseCategoryList();
    for (ExpenseCategory expCat : expenseCategoryList) {
      if (expCat.getName().equals(testExpenseCategory.getName())
          && expCat.getId() == testExpenseCategory.getId()) {
        return;
      }
    }
    fail();
  }

}


