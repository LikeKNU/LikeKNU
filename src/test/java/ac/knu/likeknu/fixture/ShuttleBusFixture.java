package ac.knu.likeknu.fixture;

import ac.knu.likeknu.domain.Shuttle;
import ac.knu.likeknu.domain.ShuttleBus;
import ac.knu.likeknu.domain.ShuttleTime;
import ac.knu.likeknu.domain.constants.ShuttleType;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

public class ShuttleBusFixture {

    public static final String ORIGIN = "origin";
    public static final String DESTINATION = "destination";
    public static final EnumSet<DayOfWeek> OPERATING_DAYS = EnumSet.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);
    public static final String NOTE = "note";
    public static final String BUS_NAME = "bus_name";

    public static Shuttle createShuttle() {
        return Shuttle.builder()
                .origin(ORIGIN)
                .destination(DESTINATION)
                .operatingDays(OPERATING_DAYS)
                .shuttleType(ShuttleType.SCHOOL_BUS)
                .note(NOTE)
                .build();
    }

    public static ShuttleBus createShuttleBus() {
        return ShuttleBus.builder()
                .busName(BUS_NAME)
                .build();
    }

    public static ShuttleBus createShuttleBusWithTimes(List<ShuttleTime> shuttleTimes) {
        return ShuttleBus.builder()
                .busName(BUS_NAME)
                .shuttleTimes(shuttleTimes)
                .build();
    }

    public static List<ShuttleTime> createShuttleTimes(int size, Duration duration) {
        LocalTime time = LocalTime.now();
        List<ShuttleTime> shuttleTimes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ShuttleTime shuttleTime = new ShuttleTime(i, String.valueOf(i), time);
            shuttleTimes.add(shuttleTime);
            time = time.plus(duration);
        }
        return shuttleTimes;
    }
}
