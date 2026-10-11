package examples.ch09.ordering.before;

public class BuyersServiceImpl implements BuyersService {
  private BuyersRepository buyersRepository;

  public BuyersServiceImpl(BuyersRepository buyersRepository) {
    this.buyersRepository = buyersRepository;
  }

  public Buyer addBuyer(Buyer body) throws Exception {
    // mappings and transformations
    Buyer newBuyer = buyersRepository.addBuyer(body);
    return newBuyer;
  }
}
