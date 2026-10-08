package examples.ch02.polymorphism.before;

import java.math.BigDecimal;

public class CustomerAccount {
  private final boolean partner;
  private final BigDecimal partnerDiscount;

  public CustomerAccount(boolean partner, BigDecimal partnerDiscount) {
    this.partner = partner;
    this.partnerDiscount = partnerDiscount;
  }

  public boolean isPartner() { return partner; }
  public BigDecimal getPartnerDiscount() { return partnerDiscount; }
}
