package examples.ch01.cleancode.after;

public interface AcmeClient {
  void login();
  void logout();
  void transferBuyerCoreData();
  void transferBuyerStatusChanges();
  void transferEvents();
}
