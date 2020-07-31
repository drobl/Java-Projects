package account;

/**
 * A fee calculator used for calculating fees for some given transaction amount
 * 
 * @author Marko Zunic
 */
public interface FeeCalculator {

  /**
   * Calculates the fee for the given transaction amount.
   * 
   * @param transactionAmount the amount to calculate the fee for
   * @return the calculated fee
   */
  double calculateFee(double transactionAmount);
}
