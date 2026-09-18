package shipping;

import shipping.adapters.FastFreightAdapter;
import shipping.adapters.OldPostalAdapter;
import shipping.core.OrderDispatcher;
import shipping.core.ShippingQuote;
import shipping.core.ShippingServiceException;
import shipping.vendors.FastFreightCloud;
import shipping.vendors.OldPostalService;

import java.util.Map;

public class Main {

    public static void main(String[] args) {

        OldPostalAdapter oldPostalAdapter =
                new OldPostalAdapter(
                        new OldPostalService()
                );

        FastFreightAdapter fastFreightAdapter =
                new FastFreightAdapter(
                        new FastFreightCloud()
                );

        System.out.println("=== Old Postal ===");

        ShippingQuote quote1 =
                oldPostalAdapter.getQuote(
                        2.0,
                        "90210"
                );

        System.out.println(quote1);

        System.out.println("\n=== Fast Freight ===");

        ShippingQuote quote2 =
                fastFreightAdapter.getQuote(
                        2.5,
                        "90210"
                );

        System.out.println(quote2);

        System.out.println("\n=== Dispatcher ===");

        OrderDispatcher dispatcher1 =
                new OrderDispatcher(
                        oldPostalAdapter
                );

        Map<String, Object> result1 =
                dispatcher1.dispatchOrder(
                        "ORD-101",
                        2.0,
                        "90210"
                );

        System.out.println(result1);

        OrderDispatcher dispatcher2 =
                new OrderDispatcher(
                        fastFreightAdapter
                );

        Map<String, Object> result2 =
                dispatcher2.dispatchOrder(
                        "ORD-202",
                        2.5,
                        "90210"
                );

        System.out.println(result2);

        System.out.println("\n=== Exception Test ===");

        try {

            oldPostalAdapter.getQuote(
                    1.0,
                    "ABCDE"
            );

        } catch (ShippingServiceException e) {

            System.out.println(
                    "Caught: " + e.getMessage()
            );
        }

        try {

            fastFreightAdapter.getQuote(
                    1.0,
                    "123"
            );

        } catch (ShippingServiceException e) {

            System.out.println(
                    "Caught: " + e.getMessage()
            );
        }
    }
}