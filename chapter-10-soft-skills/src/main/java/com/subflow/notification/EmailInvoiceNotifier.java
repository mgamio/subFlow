package com.subflow.notification;

import com.subflow.domain.Invoice;
import com.subflow.service.InvoiceNotifier;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;

public class EmailInvoiceNotifier implements InvoiceNotifier {
  private final Session session;

  public EmailInvoiceNotifier(Session session) {
    this.session = session;
  }

  @Override
  public void send(Invoice invoice) {
    try {
      MimeMessage message = new MimeMessage(session);
      message.setRecipients(Message.RecipientType.TO,
          invoice.customerEmail());
      message.setSubject("Your SubFlow invoice");
      message.setText("Your invoice total is " + invoice.total() + ".");
      Transport.send(message);
    } catch (MessagingException e) {
      throw new IllegalStateException("Could not send invoice", e);
    }
  }
}
