package shipping.core;

public class ShippingQuote{
    private final double costUsd;
    private final int deliveryDays;
    private final String carrierName;

    public ShippingQuote(double costUsd, int deliveryDays, String carrierName){
        this.costUsd = costUsd;
        this.deliveryDays = deliveryDays;
        this.carrierName = carrierName;
    }

    public double getCostUsd(){
        return costUsd;
    }

    public int getDeliveryDays(){
        return deliveryDays;
    }

    public String getCarrierName(){
        return carrierName;
    }

    @Override
    public String toString(){
        return "ShippingQuote{" +
                "costUsd=" + costUsd +
                ", deliveryDays=" + deliveryDays +
                ", carrierName='" + carrierName + '\'' + '}';
    }
}