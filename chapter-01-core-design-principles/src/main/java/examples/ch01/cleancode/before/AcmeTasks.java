package examples.ch01.cleancode.before;

public class AcmeTasks {

  /** The name says nothing, and logout is skipped if a transfer fails. */
  public void performTask(String process) {
    ACMEWebServiceClient.login();
    if (process.equals("core")) {
      ACMEWebServiceClient.transfer_buyersCoreData_to_ACME();
    }
    if (process.equals("status")) {
      ACMEWebServiceClient.transfer_buyersStatusChanges_to_ACME();
    }
    if (process.equals("events")) {
      ACMEWebServiceClient.transfer_events_to_ACME();
    }
    ACMEWebServiceClient.logout();
  }

  /** Stand-in for the partner's client library. */
  static class ACMEWebServiceClient {
    static void login() { }
    static void logout() { }
    static void transfer_buyersCoreData_to_ACME() { }
    static void transfer_buyersStatusChanges_to_ACME() { }
    static void transfer_events_to_ACME() { }
  }
}
