package examples.ch09.ordering.before;

import java.sql.Connection;
import java.sql.DriverManager;

/** The first edition's repository. It compiles; do not run it. */
public class BuyersRepositoryImpl implements BuyersRepository {
  public Buyer addBuyer(Buyer body) throws Exception {
    String dbURL = "jdbc:oracle:thin:buyers/secret@localhost:1521:buyerDB";
    Connection conn = DriverManager.getConnection(dbURL);
    // code omitted for brevity
    return body;
  }
}
