package account;

public class SavingsFeeCalculator implements FeeCalculator {
  public double calculateFee(double transactionAmount) {
    // a savings account should discourage the user from spending money, which is why the
    // user is "punished" with a slightly higher fee
    return 0.07 * transactionAmount;
  }
}
