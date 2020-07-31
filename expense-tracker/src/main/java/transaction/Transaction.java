package transaction;

import account.Account;
import exceptions.TransactionException;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Represents generally any kind of money exchange (be it withdrawal or deposit).
 * 
 * @author Vladana Jovanovic
 */
public abstract class Transaction implements TransactionInterface {
  private int transactionNumber;
  private Date date;
  private double amount;
  private String description;
  private int accountNumber;


  /**
   * @param date Date when the transaction was performed.
   * @param amount The amount of money exchanged. May not be less-or-equal than 0.
   * @param description An arbitrary textual description of the transaction.
   * @param accountNumber unique identifier of the corresponding account
   */
  public Transaction(int transactionNumber, Date date, double amount, String description,
      int accountNumber) {
    if (amount <= 0)
      throw new TransactionException("Error: Amount < 0");
    this.transactionNumber = transactionNumber;
    this.date = date;
    this.amount = amount;
    this.description = description;
    this.accountNumber = accountNumber;
  }

  /**
   * Performs the transaction on the given account by changing its balance.
   * 
   * @param account the account to perform the transaction on
   */
  abstract public void performTransaction(Account account);

  /**
   * Undoes the transaction, by restoring the balance of the given account
   * 
   * @param account the account to undo the transaction on
   */
  abstract public void undoTransaction(Account account);


  // ---------- GET AND SET METHODS ---------------
  public int getTransactionNumber() {
    return transactionNumber;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getAccountNumber() {
    return accountNumber;
  }
}
