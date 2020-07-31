package transaction;

/**
 * Represents how often a fixed transaction is performed (weekly, monthly or yearly)
 * 
 * @author Vladana Jovanovic
 */
public enum FrequencyCategory {
  YEARLY(0, "Year"), MONTHLY(1, "Month"), WEEKLY(2, "Week");

  private int value;
  private String label;

  FrequencyCategory(int value, String label) {
    this.value = value;
    this.label = label;
  }

  public static FrequencyCategory getFrequencyCategoryByValue(int value) {
    for (FrequencyCategory freCat : FrequencyCategory.values()) {
      if (freCat.getValue() == value) {
        return freCat;
      }
    }
    return null;
  }

  public int getValue() {
    return value;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return label;
  }

}
