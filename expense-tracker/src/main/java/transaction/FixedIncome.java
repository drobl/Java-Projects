package transaction;

import java.util.Date;
import java.util.Calendar;

/**
 * Represents a special type of income that some account recieves on a regular basis.a
 * 
 * @author Vladana Jovanovic
 */
public class FixedIncome extends Income {

  private FrequencyCategory frequencyCategory;
  private Date nextDueDate;

  /**
   * Creates a new Fixed Income
   * 
   * @param transactionNumber unique identifier of the transaction
   * @param date date of the transaction
   * @param amount the amount to be deposited to the corresponding account
   * @param accountNumber the unique identifier of the account to deposit the amount to
   * @param incomeCategoryId category of this income
   * @param frequencyCategory represents how often should this transaction be executed (e.g. weekly,
   *        monthly, yearly)
   * @param nextDueDate the next date when the amount will be deposited to the corresponding account
   */
  public FixedIncome(int transactionNumber, Date date, double amount, String description,
      int accountNumber, int incomeCategoryId, FrequencyCategory frequencyCategory,
      Date nextDueDate) {
    super(transactionNumber, date, amount, description, accountNumber, incomeCategoryId);
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
