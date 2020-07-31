package user;

import account.Account;
import notifications.AccountWarnings;
import notifications.LimitWarningDecorator;
import notifications.NearZeroWarningDecorator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * This class defines an User. It contains a list of all accounts linked to an individual user and
 * provides methods for obtaining information of said list of accounts and the users themselves.
 *
 * <b>Note:</b>This class implements the container interface and contains a private AccountIterator
 * class for the Iterator Design Pattern.
 * 
 * @author Daniel Robles
 */
public class User {
  private String firstName;
  private String lastName;
  private int id;
  private List<Account> accountList = new ArrayList<>();
  private String pattern = "###,##0.00";
  private AccountWarnings notifier;
  List<String> alreadyNotified = new ArrayList<String>();
  DecimalFormat decimalFormat = new DecimalFormat(pattern);


  public User(int id, String firstName, String lastName) {
    super();
    if (firstName != null && !firstName.isEmpty())
      this.firstName = firstName;
    if (lastName != null && !lastName.isEmpty())
      this.lastName = lastName;
    this.id = id;
    notifier = new notifications.AccountWarnings();
  }

  /**
   * Returns the current balance across all accounts from a user. ^ Uses the custom List Iterator
   */
  public String getBalanceSum() {
    double availableMoneySum = 0;
    if (!accountList.isEmpty()) {
      ListIterator<Account> acctIterator = new ListIterator<Account>(accountList);
      while (acctIterator.hasNext()) {
        Account tempAccount = (Account) acctIterator.next();
        if (tempAccount.getMinimumBalance() > 0
            && tempAccount.getBalance() <= tempAccount.getMinimumBalance() + 50) {
          sendWarning(tempAccount, "LimitWarning");
        }
        if (tempAccount.getBalance() > 0 && tempAccount.getBalance() <= 50) {
          sendWarning(tempAccount, "NearZeroWarning");
        }
        availableMoneySum += tempAccount.getBalance();
      }
    }
    return decimalFormat.format(availableMoneySum);
  }

  /**
   * Used to create the warnings and to keep track if the user has already been notified of a
   * specific warning on a specific account, so that they are not notified twice
   * 
   * @param tempAccount - to retrieve the necessary information for the notification
   * @param warningType - a string of the warning type, can be either "LimitWarning" or
   *        "NearZeroWarning"
   */
  private void sendWarning(Account tempAccount, String warningType) {
    String key = Integer.toString(tempAccount.getAccountNumber()) + warningType;
    if (alreadyNotified.contains(key)) {
      return;
    } else {
      switch (warningType) {
        case "LimitWarning":
          new LimitWarningDecorator(notifier, tempAccount.getAccountName(),
              tempAccount.getMinimumBalance(), tempAccount.getBalance());
          break;
        case "NearZeroWarning":
          new NearZeroWarningDecorator(notifier, tempAccount.getAccountName(),
              tempAccount.getBalance());
          break;
        default:
          return;
      }
      alreadyNotified.add(key);

    }

  }


  public void removeAccount(Account account) {
    this.accountList.remove(account);
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getId() {
    return id;
  }

  public List<Account> getAccountList() {
    return accountList;
  }

  public void setAccountList(List<Account> accountList) {
    this.accountList = accountList;
  }

  public int getAccountCount() {
    return accountList.size();
  }


}

