package account;

public class StandardFeeCalculator implements FeeCalculator {
  public double calculateFee(double transactionAmount) {
    return 0.01 * transactionAmount;
  }
}
