package com.payfast.sdk;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A SIMULATED vendor SDK. It stands in for a real payment provider's
 * library, keeps charges in memory, and never touches the network.
 */
public class PayFastClient {
  private final String apiKey;
  private final Clock clock;
  private final Map<String, PayFastCharge> charges = new LinkedHashMap<>();
  private final Map<String, PayFastCharge> byIdempotencyKey = new HashMap<>();
  private int sequence;

  public PayFastClient(String apiKey, Clock clock) {
    if (apiKey == null || apiKey.isBlank()) {
      throw new PayFastException("Missing API key");
    }
    this.apiKey = apiKey;
    this.clock = clock;
  }

  public PayFastCharge createCharge(PayFastChargeRequest request) {
    if (request.amountInMinorUnits() <= 0) {
      throw new PayFastException("Amount must be positive");
    }
    String key = request.idempotencyKey();
    if (key != null && byIdempotencyKey.containsKey(key)) {
      return byIdempotencyKey.get(key);
    }
    PayFastCharge charge = new PayFastCharge("pf_" + (++sequence),
        request.customerReference(), request.amountInMinorUnits(),
        request.currency(), LocalDate.now(clock));
    charges.put(charge.id(), charge);
    if (key != null) {
      byIdempotencyKey.put(key, charge);
    }
    return charge;
  }

  public PayFastRefund createRefund(String chargeId, long amountInMinorUnits) {
    if (!charges.containsKey(chargeId)) {
      throw new PayFastException("Unknown charge " + chargeId);
    }
    return new PayFastRefund("re_" + (++sequence), chargeId);
  }

  public List<PayFastCharge> listCharges(LocalDate from, LocalDate to) {
    List<PayFastCharge> result = new ArrayList<>();
    for (PayFastCharge charge : charges.values()) {
      if (!charge.date().isBefore(from) && !charge.date().isAfter(to)) {
        result.add(charge);
      }
    }
    return result;
  }
}
