package shipping.vendors;

public class CarrierHttpError extends RuntimeException{
    private final int statusCode;
    private final String errorMessage;

    public CarrierHttpError(int statusCode, String message){
        super("HTTP " + statusCode + ": " + message);
        this.statusCode = statusCode;
        this.errorMessage = message;
    }
    
    public int getStatusCode(){
        return statusCode;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
