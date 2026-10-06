package examples.ch01.dry.before;

/** The first version of SubFlow stored the plan as a String. */
public class Subscription {
  private final String plan;
  private final boolean annual;

  public Subscription(String plan, boolean annual) {
    this.plan = plan;
    this.annual = annual;
  }

  public String getPlan() { return plan; }
  public boolean isAnnual() { return annual; }
}
