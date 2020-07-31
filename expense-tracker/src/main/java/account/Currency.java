package account;

/**
 * Represents all possible currencies. The default currency for accounts is EUR, but cash accounts
 * may use other currencies.
 * 
 * @author Marko Zunic
 */
public enum Currency {
  USD(0, "USD", '$'), EUR(1, "EUR", '€'), GBP(2, "GBP", '£');

  private int value;
  private String label;
  private char symbol;

  Currency(int value, String label, char symbol) {
    this.value = value;
    this.label = label;
    this.symbol = symbol;
  }

  public int getValue() {
    return value;
  }

  public char getSymbol() {
    return symbol;
  }

  public static Currency getCurrencyByValue(int value) {
    for (Currency curr : Currency.values()) {
      if (curr.getValue() == value) {
        return curr;
      }
    }
    return null;
  }
}
