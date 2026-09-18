package shipping.adapters;

import shipping.core.ShippingQuote;
import shipping.core.ShippingRateProvider;
import shipping.core.ShippingServiceException;
import shipping.vendors.CarrierHttpError;
import shipping.vendors.CarrierPayload;
import shipping.vendors.FastFreightCloud;

public class FastFreightAdapter
        implements ShippingRateProvider {

    private final FastFreightCloud service;

    private static final double
            EUR_TO_USD_EXCHANGE_RATE = 1.08;

    public FastFreightAdapter(
            FastFreightCloud service
    ) {
        this.service = service;
    }

    @Override
    public ShippingQuote getQuote(
            double weightKg,
            String destinationZip
    ) {

        int grams = (int) (weightKg * 1000);

        CarrierPayload payload =
                new CarrierPayload(
                        grams,
                        destinationZip.trim()
                );

        try {

            double costEur =
                    service.fetchQuote(payload);

            double costUsd =
                    Math.round(
                            costEur *
                                    EUR_TO_USD_EXCHANGE_RATE
                                    * 100.0
                    ) / 100.0;

            int deliveryDays = 2;

            return new ShippingQuote(
                    costUsd,
                    deliveryDays,
                    "FastFreightCloud"
            );

        } catch (CarrierHttpError e) {

            throw new ShippingServiceException(
                    "FastFreight error: "
                            + e.getErrorMessage(),
                    e
            );
        }
    }
}