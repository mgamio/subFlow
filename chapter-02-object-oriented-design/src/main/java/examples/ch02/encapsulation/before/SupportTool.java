package examples.ch02.encapsulation.before;

/** The "Fix account" button in SubFlow's support tool. */
public class SupportTool {

  public void fixAccount(SubscriptionRecord subscription) {
    // The customer says they cannot log in: make it work.
    subscription.setStatus("ACTIVE");
    subscription.setEndDate(null);
  }
}
