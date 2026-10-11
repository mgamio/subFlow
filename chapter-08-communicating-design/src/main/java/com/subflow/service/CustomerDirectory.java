package com.subflow.service;

import com.subflow.domain.Customer;
import java.util.Optional;

public interface CustomerDirectory {
  Optional<Customer> findById(long customerId);
}
