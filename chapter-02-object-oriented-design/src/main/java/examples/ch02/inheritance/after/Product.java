package examples.ch02.inheritance.after;

import com.subflow.domain.Money;

/** A product HAS nutrition data; it is not a different kind of product. */
public record Product(int id, String name, Money price,
                      Nutrients nutrients) { }  // null if unknown
