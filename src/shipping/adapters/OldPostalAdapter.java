package shipping.adapters;
import java.util.Map;
import shipping.core.ShippingQuote;
import shipping.core.ShippingRateProvider;
import shipping.core.ShippingServiceException;
import shipping.vendors.OldPostalService;
import shipping.vendors.OldPostalSocketTimeout;

public class OldPostalAdapter implements ShippingRateProvider {
    private final OldPostalService service;
    public OldPostalAdapter(OldPostalService service) {
        this.service = service;
    }

    @Override
    public ShippingQuote getQuote(double weightKg, String destinationZip) 
    {
        int zipInt;
        try{
            zipInt = Integer.parseInt(destinationZip.trim());
        } 
        catch (NumberFormatException e){
            throw new ShippingServiceException("Invalid ZIP code: " + destinationZip, e);
        }
        double weightOz = weightKg * 35.274;

        try{
            Map<String, Object> result = service.computeFare(weightOz, zipInt);

            Map<String, Object> data = (Map<String, Object>) result.get("data");
            int feeCents = (Integer) data.get("fee_cents");
            int transitHours = (Integer) data.get("transit_hours");
            double costUsd = Math.round((feeCents / 100.0) * 100.0) / 100.0;
            int deliveryDays = Math.max(1, (int) Math.ceil(transitHours / 24.0));

            return new ShippingQuote(
                    costUsd,
                    deliveryDays,
                    "OldPostalService"
            );

        } 
        catch (OldPostalSocketTimeout | IllegalArgumentException e){
                throw new ShippingServiceException("Legacy postal error: " + e.getMessage(), e);
        }
    }
}