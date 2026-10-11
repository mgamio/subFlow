package com.subflow.service;

import com.subflow.domain.CampaignRule;
import com.subflow.domain.PartnerRule;
import com.subflow.domain.PricingRule;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Builds the pricing rules from the campaign catalog, reloading it at
 * most once per refresh interval. A new campaign is live within that
 * interval, without a deployment.
 */
public class CatalogPricingRules implements Supplier<List<PricingRule>> {
  private final CampaignCatalog catalog;
  private final Clock clock;
  private final Duration refreshEvery;
  private List<PricingRule> rules;
  private Instant loadedAt;

  public CatalogPricingRules(CampaignCatalog catalog, Clock clock,
                             Duration refreshEvery) {
    this.catalog = catalog;
    this.clock = clock;
    this.refreshEvery = refreshEvery;
  }

  @Override
  public synchronized List<PricingRule> get() {
    Instant now = clock.instant();
    if (rules == null || !now.isBefore(loadedAt.plus(refreshEvery))) {
      List<PricingRule> fresh = new ArrayList<>();
      fresh.add(new PartnerRule());               // partners first
      catalog.campaigns().forEach(c -> fresh.add(new CampaignRule(c)));
      rules = List.copyOf(fresh);
      loadedAt = now;
    }
    return rules;
  }
}
