package transaction;

import account.Account;

public interface TransactionInterface {
  void performTransaction(Account account);

  void undoTransaction(Account account);

}
