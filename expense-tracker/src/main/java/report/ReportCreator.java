package report;

import account.Account;
import backendservices.CategoryService;
import backendservices.TransactionService;
import transaction.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Creates reports based on selected date ranges and grouping periods (yearly, monthly or daily)
 */
public class ReportCreator implements Consumer<Account> {
    @SuppressWarnings("deprecation")

    private List<Transaction> allTransactions = new ArrayList<>();
    private List<Transaction> transactions;
    private Map<Map<String, Number>, String> incExpSummary = new LinkedHashMap<>();
    private Map<Map<String, Number>, String> incSummary = new LinkedHashMap<>();
    private Map<Map<String, Number>, String> expSummary = new LinkedHashMap<>();
    private Map<Map<String, Number>, String> incPieSummary = new LinkedHashMap<>();
    private Map<Map<String, Number>, String> expPieSummary = new LinkedHashMap<>();
    private Set<Integer> years;
    private Set<Integer> months;
    private Set<Integer> days;

    @Override
    public Consumer<Account> andThen(Consumer<? super Account> after) {
        return null;
    }

    @Override
    public void accept(Account account) {
        try {
            this.allTransactions
                    .addAll(TransactionService.getTransactionListOfAccount(account.getAccountNumber()));
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
    }

    /**
     * Selects transactions which were made in the given date range
     *
     * @param start start date
     * @param end   end date
     */
    public void chooseTransactionsBetweenDates(Date start, Date end) {
        transactions = new ArrayList<>();
        final Date s;
        final Date e;
        if(!checkInputDates(start, end)){
            e = start;
            s = start;
        }
        else {
            e = end;
            s = start;
        }
        transactions =
                allTransactions.stream().filter(transaction -> transaction.getDate().compareTo(s) >= 0)
                        .filter(transaction -> transaction.getDate().compareTo(e) < 0)
                        .collect(Collectors.toList());
        List<Date> dates = transactions.stream().map(Transaction::getDate).collect(Collectors.toList());
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        this.years = dates.stream().map(Date::getYear).collect(Collectors.toSet());
        this.months = dates.stream().map(Date::getMonth).collect(Collectors.toSet());
        this.days = dates.stream().map(Date::getDay).collect(Collectors.toSet());
    }

    /**
     * Check if start date is before end date
     * @param start start date
     * @param end end date
     * @return start < end
     */
    public boolean checkInputDates(Date start, Date end){
        return (start.compareTo(end)<0);
    }

    public List<Transaction> getTransactions(){
        return this.transactions;
    }

    public List<Transaction> getAllTransactions(){ return this.allTransactions; }

    /**
     * Creates pie chart for income transactions. Takes all income transactions from chosen period and
     * separates them by category
     */
    public void createIncomePieChart() {
        List<IncomeCategory> incomeCategories = CategoryService.getIncomeCategoryList();
        List<Transaction> tmpTransactionList = transactions;
        for (IncomeCategory incomeCategory : incomeCategories) {
            double catSum = 0;
            for (Transaction t : tmpTransactionList) {
                double amount = t.getAmount();
                if (t instanceof Income) {
                    if (((Income) t).getIncomeCategoryId() == incomeCategory.getId()) {
                        catSum += amount;
                    }
                }
            }
            Map<String, Number> tmpSummary = new LinkedHashMap<>();
            tmpSummary.put(incomeCategory.getName(), catSum);
            incPieSummary.put(tmpSummary, "Income Pie Chart");
        }
    }

    /**
     * Creates pie chart for Expense transactions. Takes all expense transactions from chosen period
     * and separates them by category
     */
    public void createExpensesPieChart() {
        List<ExpenseCategory> expenseCategories = CategoryService.getExpenseCategoryList();
        List<Transaction> tmpTransactionList = transactions;
        for (ExpenseCategory expenseCategory : expenseCategories) {
            double catSum = 0;
            for (Transaction t : tmpTransactionList) {
                double amount = t.getAmount();
                if (t instanceof Expense) {
                    if (((Expense) t).getExpenseCategoryId() == expenseCategory.getId()) {
                        catSum += amount;
                    }
                }
            }
            Map<String, Number> tmpSummary = new LinkedHashMap<>();
            tmpSummary.put(expenseCategory.getName(), catSum);
            expPieSummary.put(tmpSummary, "Expense Pie Chart");
        }
    }

    /**
     * Creates bar chart for income transactions from given date range with grouping by given periods
     * (yearly, monthly, daily).
     *
     * @param period grouping period
     */
    public void createIncomeReport(ReportPeriod period) {
        List<IncomeCategory> incomeCategories = CategoryService.getIncomeCategoryList();
        switch (period) {
            case YEARLY:
                for (int y : years) {
                    Map<String, Number> tmpSummary = new LinkedHashMap<>();
                    List<Transaction> tmpTransactionList = transactions.stream()
                            .filter(t -> t.getDate().getYear() == y).collect(Collectors.toList());
                    for (IncomeCategory incomeCategory : incomeCategories) {
                        double catSum = 0;
                        for (Transaction t : tmpTransactionList) {
                            double amount = t.getAmount();
                            if (t instanceof Income) {
                                if (((Income) t).getIncomeCategoryId() == incomeCategory.getId()) {
                                    catSum += amount;
                                }
                            }
                        }
                        tmpSummary.put(incomeCategory.getName(), catSum);
                    }
                    String periodName = Integer.toString(y + 1900);
                    incSummary.put(tmpSummary, periodName);
                }
                break;
            case MONTHLY:
                for (int y : years) {
                    List<Transaction> tmpTransactionList = transactions.stream()
                            .filter(t -> t.getDate().getYear() == y).collect(Collectors.toList());
                    for (int m : months) {
                        Map<String, Number> tmpSummary = new LinkedHashMap<>();
                        List<Transaction> monthlyTransactions = tmpTransactionList.stream()
                                .filter(t -> t.getDate().getMonth() == m).collect(Collectors.toList());
                        for (IncomeCategory incomeCategory : incomeCategories) {
                            double catSum = 0;
                            for (Transaction t : monthlyTransactions) {
                                double amount = t.getAmount();
                                if (t instanceof Income) {
                                    if (((Income) t).getIncomeCategoryId() == incomeCategory.getId()) {
                                        catSum += amount;
                                    }
                                }
                            }
                            tmpSummary.put(incomeCategory.getName(), catSum);
                        }
                        String periodName = Integer.toString(y + 1900) + "-" + Integer.toString(m + 1);
                        incSummary.put(tmpSummary, periodName);
                    }
                }
                break;
            case DAILY:
                for (int y : years) {
                    List<Transaction> tmpTransactionList = transactions.stream()
                            .filter(t -> t.getDate().getYear() == y).collect(Collectors.toList());
                    for (int m : months) {
                        List<Transaction> monthlyTransactions = tmpTransactionList.stream()
                                .filter(t -> t.getDate().getMonth() == m).collect(Collectors.toList());
                        for (int d : days) {
                            List<Transaction> dailyTransactions = monthlyTransactions.stream()
                                    .filter(t -> t.getDate().getDay() == d).collect(Collectors.toList());
                            Map<String, Number> tmpSummary = new LinkedHashMap<>();
                            for (IncomeCategory incomeCategory : incomeCategories) {
                                double catSum = 0;
                                for (Transaction t : dailyTransactions) {
                                    double amount = t.getAmount();
                                    if (t instanceof Income) {
                                        if (((Income) t).getIncomeCategoryId() == incomeCategory.getId()) {
                                            catSum += amount;
                                        }
                                    }
                                }
                                tmpSummary.put(incomeCategory.getName(), catSum);
                            }
                            String periodName = Integer.toString(y + 1900) + "-" + Integer.toString(m + 1) + "-"
                                    + Integer.toString(d + 1);
                            incSummary.put(tmpSummary, periodName);
                        }

                    }
                }
                break;
        }
    }

    /**
     * Creates bar chart for expense transactions from given date range with grouping by given periods
     * (yearly, monthly, daily).
     *
     * @param period grouping period
     */
    public void createExpensesReport(ReportPeriod period) {
        List<ExpenseCategory> expenseCategories = CategoryService.getExpenseCategoryList();
        switch (period) {
            case YEARLY:
                for (int y : years) {
                    Map<String, Number> tmpSummary = new LinkedHashMap<>();
                    List<Transaction> tmpTransactionList = transactions.stream()
                            .filter(t -> t.getDate().getYear() == y).collect(Collectors.toList());
                    for (ExpenseCategory expenseCategory : expenseCategories) {
                        double catSum = 0;
                        for (Transaction t : tmpTransactionList) {
                            double amount = t.getAmount();
                            if (t instanceof Expense) {
                                if (((Expense) t).getExpenseCategoryId() == expenseCategory.getId()) {
                                    catSum += amount;
                                }
                            }
                        }
                        tmpSummary.put(expenseCategory.getName(), catSum);
                    }
                    String periodName = Integer.toString(y + 1900);
                    expSummary.put(tmpSummary, periodName);
                }
                break;
            case MONTHLY:
                for (int y : years) {
                    List<Transaction> tmpTransactionList = transactions.stream()
                            .filter(t -> t.getDate().getYear() == y).collect(Collectors.toList());
                    for (int m : months) {
                        Map<String, Number> tmpSummary = new LinkedHashMap<>();
                        List<Transaction> monthlyTransactions = tmpTransactionList.stream()
                                .filter(t -> t.getDate().getMonth() == m).collect(Collectors.toList());
                        for (ExpenseCategory expenseCategory : expenseCategories) {
                            double catSum = 0;
                            for (Transaction t : monthlyTransactions) {
                                double amount = t.getAmount();
                                if (t instanceof Expense) {
                                    if (((Expense) t).getExpenseCategoryId() == expenseCategory.getId()) {
                                        catSum += amount;
                                    }
                                }
                            }
                            tmpSummary.put(expenseCategory.getName(), catSum);
                        }
                        String periodName = Integer.toString(y + 1900) + "-" + Integer.toString(m + 1);
                        expSummary.put(tmpSummary, periodName);
                    }
                }
                break;
            case DAILY:
                for (int y : years) {
                    List<Transaction> tmpTransactionList = transactions.stream()
                            .filter(t -> t.getDate().getYear() == y).collect(Collectors.toList());
                    for (int m : months) {
                        List<Transaction> monthlyTransactions = tmpTransactionList.stream()
                                .filter(t -> t.getDate().getMonth() == m).collect(Collectors.toList());
                        for (int d : days) {
                            List<Transaction> dailyTransactions = monthlyTransactions.stream()
                                    .filter(t -> t.getDate().getDay() == d).collect(Collectors.toList());
                            Map<String, Number> tmpSummary = new LinkedHashMap<>();
                            try {
                                for (ExpenseCategory expenseCategory : expenseCategories) {
                                    double catSum = 0;
                                    for (Transaction t : dailyTransactions) {
                                        double amount = t.getAmount();
                                        if (t instanceof Expense) {
                                            if (((Expense) t).getExpenseCategoryId() == expenseCategory.getId()) {
                                                catSum += amount;
                                            }
                                        }
                                    }
                                    tmpSummary.put(expenseCategory.getName(), catSum);
                                }
                                String periodName = Integer.toString(y + 1900) + "-" + Integer.toString(m + 1) + "-"
                                        + Integer.toString(d + 1);
                                expSummary.put(tmpSummary, periodName);
                            } catch (NullPointerException e) {
                                e.getStackTrace();
                            }

                        }
                    }
                }
        }
    }

    /**
     * Creates bar chart vor all incomes and expenses from given data range grouped by periods
     * (yearly, monthly, daily)
     *
     * @param period grouping periods
     */
    public void createSimpleReport(ReportPeriod period) {
        switch (period) {
            case YEARLY:
                Map<String, Number> tmpSummary = new LinkedHashMap<>();
                for (int y : years) {
                    double expSum = 0;
                    List<Transaction> tmpTransactionList = transactions.stream()
                            .filter(t -> t.getDate().getYear() == y).collect(Collectors.toList());
                    for (Transaction t : tmpTransactionList) {
                        double amount = t.getAmount();
                        if (t instanceof Expense) {
                            expSum += amount;
                        }
                    }
                    String periodName = Integer.toString(y + 1900);
                    tmpSummary.put(periodName, expSum);
                }
                incExpSummary.put(tmpSummary, "Expenses");
                tmpSummary = new LinkedHashMap<>();
                for (int y : years) {
                    double expSum = 0;
                    List<Transaction> tmpTransactionList = transactions.stream()
                            .filter(t -> t.getDate().getYear() == y).collect(Collectors.toList());
                    for (Transaction t : tmpTransactionList) {
                        double amount = t.getAmount();
                        if (t instanceof Income) {
                            expSum += amount;
                        }
                    }
                    String periodName = Integer.toString(y + 1900);
                    tmpSummary.put(periodName, expSum);
                }
                incExpSummary.put(tmpSummary, "Income");
                break;

            case MONTHLY:
                tmpSummary = new LinkedHashMap<>();
                for (int y : years) {
                    List<Transaction> tmpTransactionList = transactions.stream()
                            .filter(t -> t.getDate().getYear() == y).collect(Collectors.toList());
                    for (int m : months) {
                        double expSum = 0;
                        List<Transaction> monthlyTransactionsList = tmpTransactionList.stream()
                                .filter(t -> t.getDate().getMonth() == m).collect(Collectors.toList());
                        for (Transaction t : monthlyTransactionsList) {
                            double amount = t.getAmount();
                            if (t instanceof Expense) {
                                expSum += amount;
                            }
                        }
                        String periodName = Integer.toString(y + 1900) + "-" + Integer.toString(m + 1);
                        tmpSummary.put(periodName, expSum);
                    }
                }
                incExpSummary.put(tmpSummary, "Expenses");

                tmpSummary = new LinkedHashMap<>();
                for (int y : years) {
                    List<Transaction> tmpTransactionList = transactions.stream()
                            .filter(t -> t.getDate().getYear() == y).collect(Collectors.toList());
                    for (int m : months) {
                        double expSum = 0;
                        List<Transaction> monthlyTransactionsList = tmpTransactionList.stream()
                                .filter(t -> t.getDate().getMonth() == m).collect(Collectors.toList());
                        for (Transaction t : monthlyTransactionsList) {
                            double amount = t.getAmount();
                            if (t instanceof Income) {
                                expSum += amount;
                            }
                        }
                        String periodName = Integer.toString(y + 1900) + "-" + Integer.toString(m + 1);
                        tmpSummary.put(periodName, expSum);
                    }
                }
                incExpSummary.put(tmpSummary, "Income");
                break;
            case DAILY:
                tmpSummary = new LinkedHashMap<>();
                for (int y : years) {
                    List<Transaction> tmpTransactionList = transactions.stream()
                            .filter(t -> t.getDate().getYear() == y).collect(Collectors.toList());
                    for (int m : months) {
                        List<Transaction> monthlyTransactionsList = tmpTransactionList.stream()
                                .filter(t -> t.getDate().getMonth() == m).collect(Collectors.toList());
                        for (int d : days) {
                            List<Transaction> dailyTransactionsList = monthlyTransactionsList.stream()
                                    .filter(t -> t.getDate().getDay() == d).collect(Collectors.toList());
                            double expSum = 0;
                            for (Transaction t : dailyTransactionsList) {
                                double amount = t.getAmount();
                                if (t instanceof Expense) {
                                    expSum += amount;
                                }
                            }
                            String periodName = Integer.toString(y + 1900) + "-" + Integer.toString(m + 1) + "-"
                                    + Integer.toString(d + 1);
                            tmpSummary.put(periodName, expSum);
                        }
                    }
                }
                incExpSummary.put(tmpSummary, "Expenses");

                tmpSummary = new LinkedHashMap<>();
                for (int y : years) {
                    List<Transaction> tmpTransactionList = transactions.stream()
                            .filter(t -> t.getDate().getYear() == y).collect(Collectors.toList());
                    for (int m : months) {
                        List<Transaction> monthlyTransactionsList = tmpTransactionList.stream()
                                .filter(t -> t.getDate().getMonth() == m).collect(Collectors.toList());
                        for (int d : days) {
                            List<Transaction> dailyTransactionsList = monthlyTransactionsList.stream()
                                    .filter(t -> t.getDate().getDay() == d).collect(Collectors.toList());
                            double expSum = 0;
                            for (Transaction t : dailyTransactionsList) {
                                double amount = t.getAmount();
                                if (t instanceof Income) {
                                    expSum += amount;
                                }
                            }
                            String periodName = Integer.toString(y + 1900) + "-" + Integer.toString(m + 1) + "-"
                                    + Integer.toString(d + 1);
                            tmpSummary.put(periodName, expSum);
                        }
                    }
                }
                incExpSummary.put(tmpSummary, "Income");
                break;
        }
    }

    /**
     * Get data for bar chart for all Income/Expenses
     *
     * @return summary for bar chart
     */

    public Map<Map<String, Number>, String> getIncExpSummary() {
        return this.incExpSummary;
    }

    /**
     * Get data for bar chart for Income transactions
     *
     * @return summary for bar chart
     */

    public Map<Map<String, Number>, String> getIncSummary() {
        return this.incSummary;
    }

    /**
     * Get data for bar chart for Expense transactions
     *
     * @return summary for bar chart
     */
    public Map<Map<String, Number>, String> getExpSummary() {
        return this.expSummary;
    }

    /**
     * Get summary for Income pie chart
     *
     * @return summary for pie chart
     */
    public Map<Map<String, Number>, String> getIncPieSummary() {
        return this.incPieSummary;
    }

    /**
     * Ger summary for Expense pie chart
     *
     * @return summary for pie chart
     */
    public Map<Map<String, Number>, String> getExpPieSummary() {
        return this.expPieSummary;
    }
}
