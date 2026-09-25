

class FahrenheitSensor {
    // Returns raw readings like "98.6 F" or "32.0 F"
    public String readRawTemperature() {
        return "77.0 F";
    }
}

interface ICelsiusSensor {
    double getTemperatureInCelsius();
}

class TemperatureSensorAdapter implements ICelsiusSensor{
     private FahrenheitSensor FS;

     public TemperatureSensorAdapter(FahrenheitSensor FS){
        this.FS = FS;
     }

     @Override
     public double getTemperatureInCelsius(){
        String rawTemperature = FS.readRawTemperature();
        String value = rawTemperature.replace(" F","");
        double fahrenheit = Double.parseDouble(value);
        double celsius = (fahrenheit - 32) * (5.0 / 9.0);
        return Math.round(celsius * 100.0) / 100.0;
     }
}

public class Task02Adapter {
    public static void main(String[] args) {
        FahrenheitSensor sensor = new FahrenheitSensor();
        ICelsiusSensor adapter = new TemperatureSensorAdapter(sensor);
        System.out.println(adapter.getTemperatureInCelsius());
    }
}
