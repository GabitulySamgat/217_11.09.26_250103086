import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
interface IModernCalendar {
    LocalDate getCurrentDate();
}

class LegacyClock {
    // Returns UNIX epoch timestamp in SECONDS
    public long getEpochSeconds() {
        return 1700000000L;
    }
}

class ClockAdapter implements IModernCalendar{
    private LegacyClock clock;

    public ClockAdapter(LegacyClock clock){
        this.clock = clock;
    }

    @Override
    public LocalDate getCurrentDate(){
        long epochSeconds = clock.getEpochSeconds();
        Instant instant = Instant.ofEpochSecond(epochSeconds);
        return instant.atZone(ZoneOffset.UTC).toLocalDate();
    }
}

public class Task04Adapter {
    public static void main(String[] args) {
        LegacyClock legacy = new LegacyClock();
        IModernCalendar adapter = new ClockAdapter(legacy);

        System.out.println(adapter.getCurrentDate());
    }
}
