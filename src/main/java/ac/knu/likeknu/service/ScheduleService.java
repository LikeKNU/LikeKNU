package ac.knu.likeknu.service;

import ac.knu.likeknu.controller.dto.schedule.ScheduleListDto;
import ac.knu.likeknu.controller.dto.schedule.ScheduleResponse;
import ac.knu.likeknu.repository.AcademicCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final AcademicCalendarRepository academicCalendarRepository;

    public List<ScheduleResponse> getScheduleResponsesOverAPeriodOfTime() {
        LocalDate date = LocalDate.now().minusMonths(1);

        return IntStream.rangeClosed(0, 7)
                .mapToObj(index -> {
                    LocalDate plusDate = date.plusMonths(index);
                    return ScheduleResponse.of(
                            plusDate,
                            academicCalendarRepository.findByStartDateBetween(plusDate, plusDate.withDayOfMonth(plusDate.getDayOfMonth())).stream()
                                    .map(ScheduleListDto::of)
                                    .collect(Collectors.toList())
                    );
                }).collect(Collectors.toList());
    }
}
