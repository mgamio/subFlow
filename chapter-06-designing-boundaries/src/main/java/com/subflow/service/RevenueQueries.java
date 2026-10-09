package com.subflow.service;

import java.time.YearMonth;
import java.util.List;

/** Read-only questions for reports. Nothing here changes data. */
public interface RevenueQueries {
  List<RevenueByPlan> revenueByPlan(YearMonth month);
}
