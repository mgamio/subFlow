package com.subflow.service;

import com.subflow.domain.Subscription;
import java.util.List;

public interface RenewalQueries {
  List<Subscription> activeAnnualSubscriptions();
}
