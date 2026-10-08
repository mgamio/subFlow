package examples.ch05.abstraction.before;

import com.subflow.domain.Subscription;
import java.time.LocalDate;

public record CancellationRequest(Subscription subscription, LocalDate today) { }
