package examples.ch02.abstraction;

/** The address, modeled for one purpose: delivering goods. */
public record DeliveryAddress(
    String company, String contact,
    String street, String district, String city,
    String zip, String poBox, String countryCode,
    String email, String phone) { }
