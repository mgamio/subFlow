package com.subflow.service;

import com.subflow.domain.Money;
import com.subflow.domain.Plan;

/** One row of Finance's monthly revenue report. */
public record RevenueByPlan(Plan plan, long payments, Money revenue) { }
