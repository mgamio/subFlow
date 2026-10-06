package examples.ch01.cleancode.after;

import java.time.LocalDate;

public record OrderSearch(
    int buyerId,
    int supplierId,
    LocalDate orderDateFrom,
    LocalDate orderDateTo,
    String sortBy,
    SortOrder sortOrder,
    int offset,
    int limit) { }
