package report;

/**
 * Implementation of factory pattern
 */

public class ChartFactory {
  public customGraph getGraph(String graphType) {
    if (graphType == null) {
      return null;
    } else if (graphType.equals("Pie")) {
      return new customPieChart();
    } else if (graphType.equals("Bar")) {
      return new customBarChart();
    }
    return null;
  }
}
