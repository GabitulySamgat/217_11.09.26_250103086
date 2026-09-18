package shipping.core;
import java.util.HashMap;
import java.util.Map;

public class OrderDispatcher {

    private final ShippingRateProvider provider;

    public OrderDispatcher(
            ShippingRateProvider provider
    ) {
        this.provider = provider;
    }

    public Map<String, Object> dispatchOrder(
            String orderId,
            double weightKg,
            String destinationZip
    ) {

        ShippingQuote quote =
                provider.getQuote(
                        weightKg,
                        destinationZip
                );

        Map<String, Object> result =
                new HashMap<>();

        result.put("order_id", orderId);
        result.put("carrier", quote.getCarrierName());
        result.put("shipping_fee", quote.getCostUsd());
        result.put("eta_days", quote.getDeliveryDays());
        result.put("status", "DISPATCH_READY");

        return result;
    }
}