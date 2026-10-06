package examples.ch01.cleancode.after;

public class AcmeSync {
  private final AcmeClient acmeClient;

  public AcmeSync(AcmeClient acmeClient) {
    this.acmeClient = acmeClient;
  }

  public void sendToAcme(AcmeExport export) {
    acmeClient.login();
    try {
      switch (export) {
        case BUYER_CORE_DATA ->
            acmeClient.transferBuyerCoreData();
        case BUYER_STATUS_CHANGES ->
            acmeClient.transferBuyerStatusChanges();
        case EVENTS -> acmeClient.transferEvents();
      }
    } finally {
      acmeClient.logout();
    }
  }
}
