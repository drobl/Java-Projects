package account;

public class WeekendFeeCalculator implements FeeCalculator {
  public double calculateFee(double transactionAmount) {
    return 0.03 * transactionAmount;
  }
}
