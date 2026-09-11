import java.time.LocalTime;

public class CombatLogger {
    // PHASE 2: Feature C - in-memory CSV sink, alongside the existing console output
    private static StringBuilder csvLog = new StringBuilder("timestamp,attacker,defender,damageType,netDamage\n");

    public static void logToCsv(String attacker, String defender, DamageType damageType, int netDamage) {
        String timestamp = LocalTime.now().toString();
        csvLog.append(timestamp).append(",")
              .append(attacker).append(",")
              .append(defender).append(",")
              .append(damageType).append(",")
              .append(netDamage).append("\n");
    }

    public static String getCsvLog() {
        return csvLog.toString();
    }
}
