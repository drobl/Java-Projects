package account;

/**
 * Represents all possible account types
 * 
 * @author Marko Zunic
 */
public enum AccountType {
  SAVINGS_ACCOUNT(0, "Savings account"), CASH_ACCOUNT(1, "Cash");

  private int value;
  private String label;

  AccountType(int value, String label) {
    this.value = value;
    this.label = label;
  }

  public int getValue() {
    return value;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return label;
  }
}
