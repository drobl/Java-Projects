CREATE TABLE User(
   FirstName VARCHAR2(50),
   LastName VARCHAR2(50),
   Id bigint auto_increment,
   Username VARCHAR2(50),
   Password VARCHAR2(15),
   PRIMARY KEY(Id),
   UNIQUE(Username)
);

CREATE TABLE ExpenseCategory(
   Id bigint auto_increment,
   Name VARCHAR2(20),
   PRIMARY KEY(Id)
);

CREATE TABLE IncomeCategory(
   Id bigint auto_increment,
   Name VARCHAR2(20),
   PRIMARY KEY(Id)
);

CREATE TABLE Account(
   Name VARCHAR2,
   Balance DOUBLE,
   AccountNumber bigint auto_increment,
   MinimumBalance NUMBER,
   OpenedDate DATE,
   UserId INTEGER,
   PRIMARY KEY(AccountNumber),
   FOREIGN KEY(UserId) REFERENCES User(Id) ON DELETE CASCADE
);

CREATE TABLE SavingsAccount(
   ExpireDate DATE,
   AccountNumber INTEGER,
   PRIMARY KEY(AccountNumber),
   FOREIGN KEY(AccountNumber) REFERENCES Account ON DELETE CASCADE
);

CREATE TABLE CashAccount(
   AccountNumber INTEGER,
   Currency INTEGER,
   PRIMARY KEY(AccountNumber),
   FOREIGN KEY(AccountNumber) REFERENCES Account ON DELETE CASCADE,
   CONSTRAINT CashCurrencyPossible CHECK(Currency>=0 AND Currency<=2)
);

CREATE TABLE Transaction(
   TransactionNumber bigint auto_increment,
   AccountNumber INTEGER,
   Date DATE,
   Amount NUMBER,
   Description VARCHAR2(200),
   PRIMARY KEY(TransactionNumber),
   FOREIGN KEY(AccountNumber) REFERENCES Account(AccountNumber) ON DELETE CASCADE,
   CONSTRAINT DatePossible CHECK(EXTRACT(YEAR from Date)>1900),
   CONSTRAINT AmountPossible CHECK(Amount>-500)
);

CREATE TABLE Expense(
   TransactionNumber INTEGER,
   CategoryId INTEGER,
   PRIMARY KEY(TransactionNumber),
   FOREIGN KEY (TransactionNumber) REFERENCES Transaction ON DELETE CASCADE,
   FOREIGN KEY (CategoryId) REFERENCES ExpenseCategory(Id) ON DELETE CASCADE
);

CREATE TABLE Income(
   TransactionNumber INTEGER,
   CategoryId INTEGER,
   PRIMARY KEY(TransactionNumber),
   FOREIGN KEY (TransactionNumber) REFERENCES Transaction ON DELETE CASCADE,
   FOREIGN KEY (CategoryId) REFERENCES IncomeCategory(Id) ON DELETE CASCADE
);

CREATE TABLE FixedExpense(
   TransactionNumber INTEGER,
   CategoryId INTEGER,
   FrequencyCategory INTEGER,
   NextDueDate DATE,
   PRIMARY KEY(TransactionNumber),
   FOREIGN KEY (TransactionNumber) REFERENCES Transaction ON DELETE CASCADE,
   CONSTRAINT FrequencyCategoryPossibleExpense CHECK(FrequencyCategory>=0 AND FrequencyCategory<=2),
   FOREIGN KEY (CategoryId) REFERENCES ExpenseCategory(Id) ON DELETE CASCADE
);

CREATE TABLE FixedIncome(
   TransactionNumber INTEGER,
   CategoryId INTEGER,
   FrequencyCategory INTEGER,
   NextDueDate DATE,
   PRIMARY KEY(TransactionNumber),
   FOREIGN KEY (TransactionNumber) REFERENCES Transaction ON DELETE CASCADE,
   CONSTRAINT FrequencyCategoryPossibleIncome CHECK(FrequencyCategory>=0 AND FrequencyCategory<=2),
   FOREIGN KEY (CategoryId) REFERENCES IncomeCategory(Id) ON DELETE CASCADE
);






