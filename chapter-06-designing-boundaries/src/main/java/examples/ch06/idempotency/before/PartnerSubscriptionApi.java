package examples.ch06.idempotency.before;

import com.subflow.api.ApiResponse;
import com.subflow.api.CreateSubscriptionRequest;
import com.subflow.api.SubscriptionResponse;
import com.subflow.domain.BillingCycle;
import com.subflow.domain.Plan;
import com.subflow.service.SignupService;

/** Every call creates a subscription, including the partner's retries. */
public class PartnerSubscriptionApi {
  private final SignupService signups;

  public PartnerSubscriptionApi(SignupService signups) {
    this.signups = signups;
  }

  public ApiResponse<SubscriptionResponse> create(CreateSubscriptionRequest request) {
    return ApiResponse.created(SubscriptionResponse.from(signups.signUp(
        request.customerId(), Plan.valueOf(request.plan()),
        BillingCycle.valueOf(request.cycle()))));
  }
}
