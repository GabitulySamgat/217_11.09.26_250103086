package shipping.core;

public interface ShippingRateProvider{
    ShippingQuote getQuote(double weightKg, String destinationZip);
}