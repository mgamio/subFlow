package examples.ch03.srp.before;

import com.subflow.service.AccessControl;
import com.subflow.service.LateFees;
import com.subflow.service.OverdueAccount;
import java.util.List;

/** Serves two teams with one rule: Finance and Customer Success. */
public class OverdueAccountsJob {
  public static final int MAX_DAYS = 14;

  private final LateFees lateFees;
  private final AccessControl access;

  public OverdueAccountsJob(LateFees lateFees, AccessControl access) {
    this.lateFees = lateFees;
    this.access = access;
  }

  public void run(List<OverdueAccount> accounts) {
    for (OverdueAccount account : accounts) {
      if (account.daysOverdue() >= MAX_DAYS) {
        lateFees.charge(account.customerId());
        access.suspend(account.customerId());
      }
    }
  }
}
