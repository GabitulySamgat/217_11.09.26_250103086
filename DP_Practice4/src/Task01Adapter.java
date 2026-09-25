import java.math.BigDecimal;

interface IPaymentGateway {
    void processPayment(int customerId, BigDecimal amountInDollars);
}

class LegacyBillingSystem {
    public void chargeCustomerInCents(int customerId, long amountInCents) {
        System.out.println("Billed customer " + customerId + ": " + amountInCents + " cents");
    }
}

class PaymentGatewayAdapter implements IPaymentGateway{
    private LegacyBillingSystem LBS;

    public PaymentGatewayAdapter(LegacyBillingSystem LBS){
        this.LBS = LBS;
    }

    @Override
    public void processPayment(int customerId, BigDecimal amountInDollars){
         if (amountInDollars == null || amountInDollars.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Amount cannot be null or negative");
        }
        long amountInCents = amountInDollars.multiply(BigDecimal.valueOf(100)).longValueExact();
        LBS.chargeCustomerInCents(customerId, amountInCents);
    }
}

public class Task01Adapter{
    public static void main(String[] args) {
        LegacyBillingSystem legacy = new LegacyBillingSystem();
        IPaymentGateway payment = new PaymentGatewayAdapter(legacy);

        payment.processPayment(123, new BigDecimal("10.50"));
    }
}