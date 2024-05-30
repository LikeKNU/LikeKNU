package ac.knu.likeknu.fixture;

import ac.knu.likeknu.domain.AcademicCalendar;

import java.time.LocalDate;

public final class AcademicCalendarFixture {

    public static AcademicCalendar createAcademicCalendar() {
        LocalDate currentDate = LocalDate.now();
        return AcademicCalendar.builder()
                .contents("like-knu")
                .startDate(currentDate)
                .endDate(currentDate.plusDays(3))
                .build();
    }
}
