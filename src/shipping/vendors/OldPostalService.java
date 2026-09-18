package shipping.vendors;
import java.util.HashMap;
import java.util.Map;

public class OldPostalService{
    public Map<String, Object> computeFare(double weightOz, int zipCode) 
    {
        if (weightOz <= 0){
            throw new IllegalArgumentException("Weight must be strictly positive");
        }
        if (zipCode == 0 || zipCode > 99999){
            throw new OldPostalSocketTimeout("Gateway timeout for ZIP: " + zipCode);
        }
        int feeCents = (int) (500 + weightOz * 25);
        int transitHours = 48 + (int) (weightOz / 50) * 12;

        Map<String, Object> data = new HashMap<>();
        data.put("fee_cents", feeCents);
        data.put("transit_hours", transitHours);
        Map<String, Object> result = new HashMap<>();

        result.put("status", 200);
        result.put("data", data);
        return result;
    }
}