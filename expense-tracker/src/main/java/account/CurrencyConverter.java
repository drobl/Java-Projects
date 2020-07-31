package account;

/**
 * This class is a converter from a given currency to euro and in future versions, other currencies
 * to euro and vice versa.
 * 
 * @author Daniel Robles
 */

public class CurrencyConverter {
  static double USDtoEuro = .90;
  static double GBPtoEuro = 1.17;

  public CurrencyConverter() {

  }

  public static double convertToEuro(Currency fromCurrency, double amount) {
    switch (fromCurrency) {
      case USD:
        return amount * 0.90;
      case EUR:
        return amount;
      case GBP:
        return amount * 1.17;
      default:
        return amount;
    }
  }

  public static double getUSDtoEURConversionRate() {
    return USDtoEuro;
  }

  public static double getGBPtoEURConversionRate() {
    return GBPtoEuro;
  }
}
