package com.subflow.service;

import com.subflow.domain.Campaign;
import java.util.List;

/** Campaigns that marketing manages as data, not as code. */
public interface CampaignCatalog {
  List<Campaign> campaigns();
}
