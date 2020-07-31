package report;

import javafx.scene.chart.*;
import java.util.*;

/**
 * Creates custom bar chart Idea took from:
 * https://dzone.com/articles/javafx-charts-look-pretty-good
 */

public class customBarChart implements customGraph {
  private BarChart<String, Number> graph;
  private CategoryAxis xAxis;
  private NumberAxis yAxis;

  customBarChart() {}

  @Override
  public void setGraph(BarChart<String, Number> graph) {
    this.graph = graph;
  }

  @Override
  public void setxAxis(CategoryAxis xAxis) {
    this.xAxis = xAxis;
  }

  @Override
  public void setyAxis(NumberAxis yAxis) {
    this.yAxis = yAxis;
  }

  @Override
  public void setChart(PieChart chart) {}

  @Override
  public void addPoint(String x, Number y, XYChart.Series series) {
    series.getData().add(new XYChart.Data<>(x, y));
  }

  @Override
  public void createGraph(Map summaryList) {
    Iterator<Map.Entry<Map<String, Number>, String>> i = summaryList.entrySet().iterator();
    while (i.hasNext()) {
      Map.Entry<Map<String, Number>, String> summary_full = i.next();
      String seriesName = summary_full.getValue();
      Map<String, Number> summary = summary_full.getKey();
      final XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
      for (Map.Entry<String, Number> entry : summary.entrySet()) {
        String tmpString = entry.getKey();
        Number tmpValue = entry.getValue();
        addPoint(tmpString, tmpValue, series);
        series.setName(seriesName);
      }
      graph.getData().add(series);
    }
  }
}
