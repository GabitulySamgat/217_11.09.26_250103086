package shipping.vendors;

public class CarrierPayload{
    private final int weightGrams;
    private final String postalCodeStr;

    public CarrierPayload(int weightGrams, String postalCodeStr){
        this.weightGrams = weightGrams;
        this.postalCodeStr = postalCodeStr;
    }

    public int getWeightGrams(){
        return weightGrams;
    }

    public String getPostalCodeStr(){
        return postalCodeStr;
    }
}