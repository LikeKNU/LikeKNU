package ac.knu.likeknu.controller.dto.schedule;

import ac.knu.likeknu.domain.AcademicCalendar;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
public class ScheduleListDto {

    private String scheduleContents;
    private String scheduleDate;

    @Builder
    public ScheduleListDto(String scheduleContents, String scheduleDate) {
        this.scheduleContents = scheduleContents;
        this.scheduleDate = scheduleDate;
    }

    public static ScheduleListDto of(AcademicCalendar academicCalendar) {
        return ScheduleListDto.builder()
                .scheduleContents(academicCalendar.getContents())
                .scheduleDate(getScheduleDate(academicCalendar))
                .build();
    }

    private static String getScheduleDate(AcademicCalendar academicCalendar) {
        LocalDate startDate = academicCalendar.getStartDate();
        LocalDate endDate = academicCalendar.getEndDate();

        if (isSameDate(startDate, endDate))
            return parseLocalDate(startDate);

        if (isSameMonth(startDate, endDate))
            return parseLocalDate(startDate) + " ~ " + parseLocalDate(endDate);

        return parseLocalDate(startDate) + " ~ " + parseLocalDateWithMonth(endDate);
    }

    private static String parseLocalDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("dd일(EEEEE)", Locale.KOREA));
    }

    private static String parseLocalDateWithMonth(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("MM월 dd일(EEEEE)", Locale.KOREA));
    }

    private static boolean isSameDate(LocalDate date1, LocalDate date2) {
        return date1.equals(date2);
    }

    private static boolean isSameMonth(LocalDate date1, LocalDate date2) {
        return date1.getMonthValue() == date2.getMonthValue();
    }

}
