package account;

import observer.Observable;
import transaction.FixedExpense;
import transaction.FixedIncome;
import transaction.Transaction;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import notifications.AccountWarnings;
import notifications.BalanceBelowLimitDecorator;
import notifications.InvalidWithdrawal;
import notifications.InvalidWithdrawalMinimum;

/**
 * Defines an abstract Account class. Every Account should have a name, balance, limit and opened
 * date.
 * 
 * @author Natalia Tretiakova
 */
public abstract class Account extends Observable implements AccountInterface {
  private int accountNumber;
  private String accountName;
  protected double balance;
  private double minimumBalance;
  private Date openedDate;
  private List<Transaction> transactionList = new ArrayList<>();
  // Strategy pattern: can dynamically change strategy for calculating the fee
  private FeeCalculator feeCalculator = new StandardFeeCalculator();
  private AccountWarnings notifier = new AccountWarnings();
  private final static Logger LOGGER = Logger.getLogger("AccountClass");
  private String typeName = "Account";



  /**
   * Creates an Account with opened date == today.
   * 
   * @param accountName name of Account, can be chosen arbitrary
   * @param balance the amount of money on the account, can not be less than limit
   * @param minimumBalance the lower edge of money amount
   */
  public Account(int accountNumber, String accountName, double balance, double minimumBalance) {
    this(accountNumber, accountName, balance, minimumBalance, new Date());
  }

  /**
   * Creates an Account with any date in the past
   * 
   * @param accountNumber a unique identifier of an account, automatically assigned in the database
   * @param accountName name of the account, can be chosen arbitrary
   * @param balance the amount of money on the account, cannot be less than given minimum
   * @param minimumBalance the minimum balance threshold which must not be subceeded
   * @param openedDate date when account was opened
   */
  public Account(int accountNumber, String accountName, double balance, double minimumBalance,
      Date openedDate) {
    LOGGER.getLogger("AccountClass").setLevel(Level.OFF);

    if (balance < minimumBalance) {
      new BalanceBelowLimitDecorator(notifier, accountName, minimumBalance, balance);
      return;
    }
    this.accountNumber = accountNumber;
    this.accountName = accountName;
    this.balance = balance;
    this.minimumBalance = minimumBalance;
    Date today = new Date();
    if (openedDate.compareTo(today) > 0) {
      this.openedDate = today;
      throw new IllegalArgumentException("Opened date cannot be in future, set to today");
    } else {
      this.openedDate = openedDate;
    }
    LOGGER.info("New account created: " + accountName + " Balance: " + balance + " MinBal: "
        + minimumBalance);
  }

  /**
   * Withdraws money by subtracting the given amount and the corresponding fee from the current
   * balance. Withdrawal is not possible if the minimum balance would be subceeded.
   * 
   * @param amount The amount to be withdrawn (excluding the fee).
   */
  public void withdraw(double amount) {
    if (this.balance - amount < this.minimumBalance) {
      new InvalidWithdrawalMinimum(notifier, this.accountName, amount, this.minimumBalance,
          this.balance);
      // throw new IllegalArgumentException(
      // "Withdrawal not possible. Balance minimum would be subceeded by this withdrawal.");
      return;
    }
    double totalWithdrawalAmount;
    if (this.getAccountType().equals("CashAccount")) {
      totalWithdrawalAmount = amount;
    } else {
      totalWithdrawalAmount = amount + calculateFee(amount);
    }
    this.balance -= totalWithdrawalAmount;
    LOGGER.info("Witdrawal from: " + accountName + " Amount: " + amount + " NewBal: " + balance);
    notifyObservers();
  }

  /**
   * Calculates the total cost of the transaction with fee, used to validate expense form
   * 
   * @param amount The amount to be withdrawn (excluding the fee).
   */
  public double calculateTransactionAmount(double amount) {
    LOGGER.info("Transaction fee: " + calculateFee(amount) + " on Amount: " + amount + " NewBal: "
        + balance);
    return amount + calculateFee(amount);
  }

  /**
   * Deposits money by adding the given amount to the current balance. Does not apply any additional
   * fee calculation.
   * 
   * @param amount The total amount to be deposited.
   */
  public void deposit(double amount) {
    this.balance += amount;
    notifyObservers();
  }

  /**
   * Calculates the fee for a given transaction amount (expense). Fee calculation strategies may be
   * changed dynamically by setting a corresponding {@link FeeCalculator}.
   * 
   * @param transactionAmount The amount of money being transacted to calculate the fee for.
   * @return fee amount
   */
  public double calculateFee(double transactionAmount) {
    double fee = 0;
    if (feeCalculator != null) {
      fee = feeCalculator.calculateFee(transactionAmount);
    }
    LOGGER.info(
        "Transaction fee: " + fee + " on Amount: " + transactionAmount + " NewBal: " + balance);
    return fee;
  }

  /**
   * Add new Transaction to the list of Transactions
   * 
   * @param transaction Transaction to add
   */
  public void addTransaction(Transaction transaction) {
    transactionList.add(transaction);
  }

  public void removeTransaction(Transaction transaction) {
    transactionList.remove(transaction);
  }

  public double getBalance() {
    return balance;
  }

  public double getMinimumBalance() {
    return minimumBalance;
  }

  public List<Transaction> getTransactionList() {
    return this.transactionList;
  }


  public List<Transaction> getNonFixedTransactionList() {
    List<Transaction> nonFixedTransactions = new ArrayList<>();
    for (Transaction transaction : transactionList) {
      if (!(transaction instanceof FixedIncome) && !(transaction instanceof FixedExpense)) {
        nonFixedTransactions.add(transaction);
      }
    }
    return nonFixedTransactions;
  }

  public List<Transaction> getFixedTransactionList() {
    List<Transaction> result = new ArrayList<>();
    for (Transaction transaction : transactionList) {
      if (transaction instanceof FixedExpense || transaction instanceof FixedIncome) {
        result.add(transaction);
      }
    }
    return result;
  }

  public void setTransactionList(List<Transaction> transactionList) {
    this.transactionList = transactionList;
  }

  public String getAccountName() {
    return this.accountName;
  }

  // Strategy pattern: can change fee calculation strategy on runtime
  public void setFeeCalculator(FeeCalculator feeCalculator) {
    this.feeCalculator = feeCalculator;
  }

  public FeeCalculator getFeeCalculator() {
    return feeCalculator;
  }

  public int getAccountNumber() {
    return accountNumber;
  }

  public String getBalanceFormatted() {
    String pattern = "###,##0.00";
    DecimalFormat decimalFormat = new DecimalFormat(pattern);
    return "€" + decimalFormat.format(balance);
  }

  @Override
  public String toString() {
    return accountName;
  }

  public String getAccountType() {
    return typeName;
  }
}
