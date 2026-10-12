package examples.ch10.impact;

import com.subflow.domain.Money;

/** What a year of payment-provider outages costs, in business units. */
public record OutageImpact(long failedCheckouts,
                           long lostSubscriptions,
                           Money lostRevenue) {
}
