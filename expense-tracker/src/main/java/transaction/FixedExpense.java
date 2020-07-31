package transaction;

import java.util.Date;
import java.util.Calendar;

/**
 * Represents a special type of expense, for which some account is billed on a regular basis.
 * 
 * @author Vladana Jovanovic
 */
public class FixedExpense extends Expense {

  private FrequencyCategory frequencyCategory;
  private Date nextDueDate;

  /**
   * Creates a new Fixed Expense
   * 
   * @param transactionNumber unique identifier of the transaction
   * @param date date of the transaction
   * @param amount the amount to be widthdrawn regularly from the corresponding account
   * @param accountNumber the unique identifier of the account to withdraw the expense amount from
   * @param expenseCategoryId category of this expense
   * @param frequencyCategory represents how often should this transaction be executed (e.g. weekly,
   *        monthly, yearly)
   * @param nextDueDate the next date when the amount will be withdrawn from the corresponding
   *        account
   */
  public FixedExpense(int transactionNumber, Date date, double amount, String description,
      int accountNumber, int expenseCategoryId, FrequencyCategory frequencyCategory,
      Date nextDueDate) {
    super(transactionNumber, date, amount, description, accountNumber, expenseCategoryId);
    this.frequencyCategory = frequencyCategory;
    this.nextDueDate = nextDueDate;
  }

  public Date getNextDueDate() {
    return nextDueDate;
  }

  public FrequencyCategory getFrequencyCategory() {
    return frequencyCategory;
  }

  /**
   * Sets the due date to the next due date, depending on the frequency category. For example, if
   * this is a monthly fixed expense and the current due date is January the 20th, the due date will
   * be set to February the 20th
   */
  public void updateNextDueDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(nextDueDate);
    switch (frequencyCategory) {
      case WEEKLY:
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        break;
      case MONTHLY:
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        break;
      case YEARLY:
        calendar.add(Calendar.DAY_OF_YEAR, 365);
        break;
    }
    nextDueDate = calendar.getTime();

  }
}
