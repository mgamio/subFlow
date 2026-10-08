package com.subflow.service;

import com.subflow.domain.Customer;
import java.time.LocalDate;

public interface ReminderNotifier {
  void remindRenewal(Customer customer, LocalDate renewalDate);
}
