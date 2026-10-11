package examples.ch09.ordering.before;

/**
 * The first edition used Spring's @Autowired and ResponseEntity here.
 * Without them the design is the same: controller, service, repository.
 */
public class BuyersApiController {
  private final BuyersService buyersService;

  public BuyersApiController(BuyersService buyersService) {
    this.buyersService = buyersService;
  }

  public Buyer addBuyer(Buyer body) throws Exception {
    return buyersService.addBuyer(body);
  }
}
