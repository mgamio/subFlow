package examples.ch06.pagination.before;

import com.subflow.service.InvoiceSummary;
import java.util.List;

/** "Skip page * size rows": simple, until rows are added between requests. */
public final class OffsetInvoicePages {
  private OffsetInvoicePages() { }

  /** Newest first, as the first version of the invoice page showed them. */
  public static List<InvoiceSummary> page(List<InvoiceSummary> newestFirst,
                                          int page, int size) {
    int from = Math.min(page * size, newestFirst.size());
    int to = Math.min(from + size, newestFirst.size());
    return newestFirst.subList(from, to);
  }
}
