package examples.ch03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.subflow.domain.Money;
import com.subflow.service.OverdueAccount;
import examples.ch03.lsp.before.CardHubGateway;
import examples.ch03.lsp.before.PaymentGateway;
import examples.ch03.lsp.shapes.before.Rectangle;
import examples.ch03.lsp.shapes.before.Square;
import examples.ch03.ocp.ratelimit.after.FileApiPlanSource;
import examples.ch03.ocp.ratelimit.after.RateLimiter;
import examples.ch03.srp.before.OverdueAccountsJob;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BeforeExamplesTest {

  @Test
  void oneJobCannotGiveTheTwoTeamsDifferentRules() {
    List<Long> charged = new ArrayList<>();
    List<Long> suspended = new ArrayList<>();
    new OverdueAccountsJob(charged::add, suspended::add)
        .run(List.of(new OverdueAccount(2, 8)));
    assertTrue(suspended.isEmpty());   // Customer Success wanted 7 days
  }

  @Test
  void squareBreaksTheRectangleContract() {
    Rectangle rectangle = new Square();
    rectangle.setWidth(5);
    rectangle.setHeight(10);
    assertEquals(100, rectangle.getArea());   // a Rectangle promised 50
  }

  @Test
  void cardHubGatewayThrowsWhereTheContractPromisedARefund() {
    PaymentGateway gateway = new CardHubGateway();
    var payment = gateway.charge(7, Money.usd("19.99"));
    assertThrows(UnsupportedOperationException.class,
        () -> gateway.refund(payment));
  }

  @Test
  void rateLimiterReadsPlansFromAnySource(@TempDir Path dir) throws Exception {
    Path file = Files.writeString(dir.resolve("plans.txt"), "A: 100\nB: 1000\n");
    RateLimiter limiter = new RateLimiter(new FileApiPlanSource(file));
    assertTrue(limiter.allow("A", 99));
    assertEquals(false, limiter.allow("A", 100));
  }
}
