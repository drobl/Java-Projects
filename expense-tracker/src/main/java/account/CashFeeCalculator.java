package account;

public class CashFeeCalculator implements FeeCalculator {
  public double calculateFee(double transactionAmount) {
    // There is no fee for withdrawing cash, but that could be changed here in future editions of
    // this app
    return 0;
  }
}
