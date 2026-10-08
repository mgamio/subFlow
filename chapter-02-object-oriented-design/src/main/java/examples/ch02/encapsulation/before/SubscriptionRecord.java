package examples.ch02.encapsulation.before;

import java.time.LocalDate;

/** Private fields, but a getter and a setter for each one. */
public class SubscriptionRecord {
  private long id;
  private String status;
  private LocalDate startDate;
  private LocalDate endDate;

  public long getId() { return id; }
  public void setId(long id) { this.id = id; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public LocalDate getStartDate() { return startDate; }
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() { return endDate; }
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }
}
