package com.subflow.service;

import com.subflow.domain.Subscription;
import java.time.LocalDate;
import java.util.List;

public interface RenewalQueries {
  /**
   * Active annual subscriptions that may renew on the given date.
   * May return extra candidates; must never miss one.
   */
  List<Subscription> activeAnnualRenewingOn(LocalDate renewalDate);
}
