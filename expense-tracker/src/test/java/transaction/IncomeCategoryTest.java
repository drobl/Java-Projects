package transaction;

import backendservices.CategoryService;
import junit.framework.TestCase;
import java.util.List;

public class IncomeCategoryTest extends TestCase {

  public void testCreateNewIncomeCategory() {
    IncomeCategory testIncomeCategory = CategoryService.addIncomeCategory("Grandma");
    List<IncomeCategory> incomeCategoryList = CategoryService.getIncomeCategoryList();
    for (IncomeCategory incCat : incomeCategoryList) {
      if (incCat.getName().equals(testIncomeCategory.getName())
          && incCat.getId() == testIncomeCategory.getId()) {
        return;
      }
    }
    fail();
  }
}


