package report;

import javafx.scene.chart.*;
import java.util.Iterator;
import java.util.Map;

/**
 * Creates custom pie chart Idea took from:
 * https://dzone.com/articles/javafx-charts-look-pretty-good
 */

public class customPieChart implements customGraph {
  private PieChart chart;

  customPieChart() {}

  @Override
  public void setChart(PieChart chart) {
    this.chart = chart;
  }

  @Override
  public void addPoint(String x, Number y, XYChart.Series series) {

  }

  @Override
  public void setGraph(BarChart<String, Number> graph) {}

  @Override
  public void setxAxis(CategoryAxis xAxis) {}

  @Override
  public void setyAxis(NumberAxis yAxis) {}

  @Override
  public void createGraph(Map summaryList) {
    Iterator<Map.Entry<Map<String, Number>, String>> i = summaryList.entrySet().iterator();
    while (i.hasNext()) {
      Map.Entry<Map<String, Number>, String> summary_full = i.next();
      Map<String, Number> summary = summary_full.getKey();
      for (Map.Entry<String, Number> entry : summary.entrySet()) {
        String tmpString = entry.getKey();
        Number tmpValue = entry.getValue();
        PieChart.Data slice = new PieChart.Data(tmpString, (Double) tmpValue);
        chart.getData().add(slice);
      }
    }
  }
}

