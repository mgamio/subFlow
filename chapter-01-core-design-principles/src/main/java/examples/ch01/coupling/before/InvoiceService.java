package examples.ch01.coupling.before;

import com.subflow.domain.Customer;
import com.subflow.domain.PricingPolicy;
import com.subflow.domain.Subscription;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

/** Problematic on purpose: pricing, storage and email in one class. */
public class InvoiceService {
  private final PricingPolicy pricing = new PricingPolicy();

  public void issueInvoice(Subscription subscription,
                           Customer customer)
      throws Exception {
    BigDecimal amount = pricing.priceFor(
        subscription.plan(), subscription.cycle());

    // store the invoice
    try (Connection db = DriverManager.getConnection(
        "jdbc:mysql://prod-db:3306/subflow",
        "app", "s3cret")) {
      PreparedStatement insert = db.prepareStatement(
          "INSERT INTO invoices (subscription_id, amount)"
          + " VALUES (?, ?)");
      insert.setLong(1, subscription.id());
      insert.setBigDecimal(2, amount);
      insert.executeUpdate();
    }

    // email the customer
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.subflow.com");
    Session session = Session.getInstance(props);
    MimeMessage message = new MimeMessage(session);
    message.setRecipients(Message.RecipientType.TO,
        customer.email());
    message.setSubject("Your SubFlow invoice");
    message.setText("Dear " + customer.name()
        + ", your invoice amount is " + amount + ".");
    Transport.send(message);
  }
}
