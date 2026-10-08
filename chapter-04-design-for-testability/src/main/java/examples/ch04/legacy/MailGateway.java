package examples.ch04.legacy;

/** Stands in for a static mail helper that sends real email. */
public final class MailGateway {
  private MailGateway() { }

  public static void send(String to, String text) {
    throw new IllegalStateException("No mail server in this environment");
  }
}
