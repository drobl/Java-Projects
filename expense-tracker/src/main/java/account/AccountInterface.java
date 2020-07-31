package account;

/**
 * Interface used by Account. Part of the factory creation pattern Accounts should be able to
 * deposit and withdraw money
 * 
 * @author Daniel Robles
 */
public interface AccountInterface {
  void withdraw(double amount);

  void deposit(double amount);
}
