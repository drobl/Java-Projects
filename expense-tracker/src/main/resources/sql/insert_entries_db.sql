INSERT INTO User(FirstName, LastName, Id, Username, Password) VALUES ('Max', 'Mustermann', 1, 'maxmus', 'maxmus123');
INSERT INTO User(FirstName, LastName, Id, Username, Password) VALUES ('TestUser', 'Test', 69, 'testUser', 'test123');

INSERT INTO Account(Name, Balance, AccountNumber, MinimumBalance, OpenedDate, UserId) VALUES('UniCredit', 200, 1, 0, TO_DATE('02/01/2020','DD/MM/YYYY'), 1);
INSERT INTO SavingsAccount(ExpireDate, AccountNumber) VALUES( TO_DATE('05/01/2023','DD/MM/YYYY'), 1);
INSERT INTO Account(Name, Balance, AccountNumber, MinimumBalance, OpenedDate, UserId) VALUES('Cash', 2000, 2, 0, TO_DATE('06/01/2018','DD/MM/YYYY'), 1);
INSERT INTO CashAccount(Currency, AccountNumber) VALUES( 1, 2);

INSERT INTO IncomeCategory(Id, Name) VALUES (0, 'Salary');
INSERT INTO IncomeCategory(Id, Name) VALUES (1, 'Gift');
INSERT INTO ExpenseCategory(Id, Name) VALUES (2, 'Clothes');
INSERT INTO ExpenseCategory(Id, Name) VALUES (3, 'Rent');
INSERT INTO ExpenseCategory(Id, Name) VALUES (4, 'Unilities');
INSERT INTO ExpenseCategory(Id, Name) VALUES (5, 'Travel');




INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(1, 1, TO_DATE('01/01/2014','DD/MM/YYYY'), 200, 'Utilities');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(2, 1, TO_DATE('02/01/2015','DD/MM/YYYY'), 300, 'London Tickets');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(3, 1, TO_DATE('07/01/2016','DD/MM/YYYY'), 8, 'Spotify');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(4, 1, TO_DATE('08/01/2017','DD/MM/YYYY'), 400, 'McDonalds salary');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(5, 1, TO_DATE('03/01/2018','DD/MM/YYYY'), 800, 'Salary');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(6, 2, TO_DATE('06/01/2019','DD/MM/YYYY'), 100, 'Bank robbery');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(7, 1, TO_DATE('01/01/2020','DD/MM/YYYY'), 200, 'Utilities');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(8, 1, TO_DATE('02/02/2019','DD/MM/YYYY'), 300, 'Paris Tickets');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(9, 2, TO_DATE('07/03/2019','DD/MM/YYYY'), 20, 'Internet');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(10, 2, TO_DATE('08/04/2019','DD/MM/YYYY'), 400, 'McDonalds salary');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(11, 1, TO_DATE('03/01/2020','DD/MM/YYYY'), 800, 'Salary');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(12, 2, TO_DATE('06/06/2019','DD/MM/YYYY'), 100, 'Bank robbery');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(13, 1, TO_DATE('05/11/2019','DD/MM/YYYY'), 200, 'Utilities');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(14, 1, TO_DATE('02/04/2019','DD/MM/YYYY'), 300, 'London Tickets');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(15, 1, TO_DATE('05/05/2019','DD/MM/YYYY'), 10, 'Insurance');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(16, 1, TO_DATE('08/07/2019','DD/MM/YYYY'), 400, 'McDonalds salary');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(17, 1, TO_DATE('03/01/2020','DD/MM/YYYY'), 800, 'Salary');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(18, 2, TO_DATE('06/01/2020','DD/MM/YYYY'), 100, 'Bank robbery');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(19, 1, TO_DATE('01/10/2019','DD/MM/YYYY'), 200, 'Utilities');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(20, 1, TO_DATE('02/09/2019','DD/MM/YYYY'), 20, 'Cinema');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(21, 2, TO_DATE('07/03/2019','DD/MM/YYYY'), 368, 'University');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(22, 2, TO_DATE('08/04/2019','DD/MM/YYYY'), 400, 'McDonalds salary');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(23, 1, TO_DATE('03/10/2019','DD/MM/YYYY'), 800, 'Salary');
INSERT INTO Transaction(TransactionNumber, AccountNumber, Date, Amount, Description) VALUES(24, 2, TO_DATE('19/11/2019','DD/MM/YYYY'), 100, 'Gift from Grandma');

INSERT INTO Expense(TransactionNumber,CategoryId) VALUES(1, 2);
INSERT INTO Expense(TransactionNumber,CategoryId) VALUES(2, 5);
INSERT INTO FixedExpense(TransactionNumber, CategoryId, FrequencyCategory, NextDueDate) VALUES(3, 2, 0, TO_DATE('09/02/2020','DD/MM/YYYY'));
INSERT INTO Income(TransactionNumber,CategoryId) VALUES(4, 0);
INSERT INTO FixedIncome(TransactionNumber, CategoryId, FrequencyCategory, NextDueDate) VALUES(5, 0, 0, TO_DATE('06/01/2020','DD/MM/YYYY'));
INSERT INTO Income(TransactionNumber,CategoryId) VALUES(6, 1);
INSERT INTO Expense(TransactionNumber,CategoryId) VALUES(7, 4);
INSERT INTO Expense(TransactionNumber,CategoryId) VALUES(8, 3);
INSERT INTO Expense(TransactionNumber, CategoryId) VALUES(9, 2);
INSERT INTO Income(TransactionNumber,CategoryId) VALUES(10, 0);
INSERT INTO Income(TransactionNumber, CategoryId) VALUES(11, 0);
INSERT INTO Income(TransactionNumber,CategoryId) VALUES(12, 1);
INSERT INTO Expense(TransactionNumber,CategoryId) VALUES(13, 4);
INSERT INTO Expense(TransactionNumber,CategoryId) VALUES(14, 3);
INSERT INTO Expense(TransactionNumber, CategoryId) VALUES(15, 2);
INSERT INTO Income(TransactionNumber,CategoryId) VALUES(16, 0);
INSERT INTO Income(TransactionNumber, CategoryId) VALUES(17, 0);
INSERT INTO Income(TransactionNumber,CategoryId) VALUES(18, 1);
INSERT INTO Expense(TransactionNumber,CategoryId) VALUES(19, 4);
INSERT INTO Expense(TransactionNumber,CategoryId) VALUES(20, 5);
INSERT INTO Expense(TransactionNumber, CategoryId) VALUES(21, 2);
INSERT INTO Income(TransactionNumber,CategoryId) VALUES(22, 0);
INSERT INTO Income(TransactionNumber, CategoryId) VALUES(23, 0);
INSERT INTO Income(TransactionNumber,CategoryId) VALUES(24, 1);