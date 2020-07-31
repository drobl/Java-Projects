package report;

import javafx.scene.chart.*;
import java.util.Map;

/**
 * Custom chart interface. Idea took from: https://dzone.com/articles/javafx-charts-look-pretty-good
 */
public interface customGraph {
  void addPoint(final String x, final Number y, final XYChart.Series<String, Number> series);

  void createGraph(Map<Map<String, Number>, String> summary);

  void setGraph(BarChart<String, Number> graph);

  void setxAxis(CategoryAxis xAxis);

  void setyAxis(NumberAxis yAxis);

  void setChart(PieChart chart);
}
