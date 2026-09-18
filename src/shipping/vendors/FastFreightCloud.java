package shipping.vendors;

public class FastFreightCloud{
    public double fetchQuote(CarrierPayload payload) 
    {
        if (payload.getWeightGrams() <= 0){
            throw new CarrierHttpError(400, "Weight must be > 0 grams");
        }
        String zip = payload.getPostalCodeStr();
        if (zip.length() != 5 ||
                !zip.matches("\\d+")){
            throw new CarrierHttpError(422, "Invalid postal code: " + zip);
        }
        return Math.round((4.50 + payload.getWeightGrams() * 0.003) * 100.0) / 100.0;
    }
}

