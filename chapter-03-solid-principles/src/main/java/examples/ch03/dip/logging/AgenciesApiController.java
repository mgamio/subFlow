package examples.ch03.dip.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Depends on the SLF4J facade, not on a logging implementation. */
public class AgenciesApiController {
  private static final Logger logger =
      LoggerFactory.getLogger(AgenciesApiController.class);

  public void listAgencies() {
    logger.info("Listing agencies");
  }
}
