package controllers;

import account.Account;
import backendservices.AccountService;
import expensetracker.JavaFXApp;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import report.*;
import user.User;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller for report scene
 */

public class ReportSceneController {
  private List<Account> accountList = new ArrayList<>();
  private ReportPeriod reportPeriod;

  @FXML
  private NumberAxis yAxisIncExp;
  @FXML
  private CategoryAxis xAxisIncExp;
  @FXML
  private NumberAxis yAxisInc;
  @FXML
  private CategoryAxis xAxisInc;
  @FXML
  private NumberAxis yAxisExp;
  @FXML
  private CategoryAxis xAxisExp;
  @FXML
  private BarChart<String, Number> incomeExpensesBarChart;
  @FXML
  private BarChart<String, Number> incomeBarChart;
  @FXML
  private BarChart<String, Number> expensesBarChart;
  @FXML
  private PieChart incomePieChart;
  @FXML
  private PieChart expensesPieChart;
  @FXML
  private ChoiceBox chooseMode;
  @FXML
  private DatePicker startDate;
  @FXML
  private DatePicker endDate;
  @FXML
  private Label invalidDate;


  public ReportSceneController() {}

  // this function taken from https://stackoverflow.com/q/36968122
  private static LocalDate LOCAL_DATE(String dateString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDate localDate = LocalDate.parse(dateString, formatter);
    return localDate;
  }

  @FXML
  private void initialize() {
    User currentUser = JavaFXApp.getCurrentUser();
    chooseMode.setItems(FXCollections.observableArrayList(ReportPeriod.values()));
    chooseMode.setValue(ReportPeriod.YEARLY);
    startDate.setValue(LOCAL_DATE("01-01-2016"));
    endDate.setValue(LocalDate.now());
    accountList = AccountService.getAccountListOfUser(currentUser.getId());
    showReport();
  }

  public void setReportPeriodMode() {
    reportPeriod = (ReportPeriod) chooseMode.getValue();
    switch (reportPeriod) {
      case YEARLY:
        startDate.setValue(LOCAL_DATE("01-01-2016"));
        break;
      case MONTHLY:
        startDate.setValue(LOCAL_DATE("01-01-2019"));
        break;
      case DAILY:
        startDate.setValue(LOCAL_DATE("01-01-2020"));
        break;
    }
  }

  public void showReport() {
    ReportCreator reportCreator = new ReportCreator();
    ChartFactory chartFactory = new ChartFactory();
    for (Account a : accountList) {
      reportCreator.accept(a);
    }
    Date startReportDate =
        Date.from(startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date endReportDate =
        Date.from(endDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    // check if dates range is correct
    if (endReportDate.compareTo(startReportDate) <= 0) {
      invalidDate.setVisible(true);
      setReportPeriodMode();
      endDate.setValue(LocalDate.now());
      return;
    } else if (startReportDate.compareTo(Calendar.getInstance().getTime()) >= 0) {
      invalidDate.setVisible(true);
      setReportPeriodMode();
      startDate.setValue(LocalDate.now());
      return;
    } else {
      invalidDate.setVisible(false);
    }
    reportCreator.chooseTransactionsBetweenDates(startReportDate, endReportDate);

    incomeExpensesBarChart.getData().clear();
    xAxisIncExp.getCategories().clear();
    customGraph incExpBarChart = chartFactory.getGraph("Bar");
    incExpBarChart.setGraph(incomeExpensesBarChart);
    incExpBarChart.setxAxis(xAxisIncExp);
    incExpBarChart.setyAxis(yAxisIncExp);
    Map<Map<String, Number>, String> incExpSummary = reportCreator.getIncExpSummary();
    reportCreator.createSimpleReport(reportPeriod);
    incExpBarChart.createGraph(incExpSummary);

    incomeBarChart.getData().clear();
    xAxisInc.getCategories().clear();
    customGraph incBarChart = chartFactory.getGraph("Bar");
    incBarChart.setGraph(incomeBarChart);
    incBarChart.setxAxis(xAxisInc);
    incBarChart.setyAxis(yAxisInc);
    Map<Map<String, Number>, String> incSummary = reportCreator.getIncSummary();
    reportCreator.createIncomeReport(reportPeriod);
    incBarChart.createGraph(incSummary);

    expensesBarChart.getData().clear();
    xAxisExp.getCategories().clear();
    customGraph expBarChart = chartFactory.getGraph("Bar");
    expBarChart.setGraph(expensesBarChart);
    expBarChart.setxAxis(xAxisExp);
    expBarChart.setyAxis(yAxisExp);
    Map<Map<String, Number>, String> expSummary = reportCreator.getExpSummary();
    reportCreator.createExpensesReport(reportPeriod);
    expBarChart.createGraph(expSummary);

    incomePieChart.getData().clear();
    customGraph incPieChart = chartFactory.getGraph("Pie");
    incPieChart.setChart(incomePieChart);
    Map<Map<String, Number>, String> incPieSummary = reportCreator.getIncPieSummary();
    reportCreator.createIncomePieChart();
    incPieChart.createGraph(incPieSummary);

    expensesPieChart.getData().clear();
    customGraph expPieChart = chartFactory.getGraph("Pie");
    expPieChart.setChart(expensesPieChart);
    Map<Map<String, Number>, String> expPieSummary = reportCreator.getExpPieSummary();
    reportCreator.createExpensesPieChart();
    expPieChart.createGraph(expPieSummary);
  }
}
