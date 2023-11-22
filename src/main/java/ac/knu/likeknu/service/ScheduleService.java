package ac.knu.likeknu.service;

import ac.knu.likeknu.controller.dto.schedule.ScheduleListDto;
import ac.knu.likeknu.controller.dto.schedule.ScheduleResponse;
import ac.knu.likeknu.domain.AcademicCalendar;
import ac.knu.likeknu.repository.AcademicCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final AcademicCalendarRepository academicCalendarRepository;

    public List<ScheduleResponse> getScheduleResponses() {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1).minusMonths(1);
        LocalDate endDate = startDate.plusMonths(7);
        endDate = endDate.withDayOfMonth(endDate.lengthOfMonth());

        List<ScheduleResponse> responses = new ArrayList<>();
        if (startDate.getYear() == endDate.getYear()) {
            responses.addAll(fetchScheduleResponses(startDate, endDate));
        } else {
            responses.addAll(fetchScheduleResponses(startDate, LocalDate.of(startDate.getYear(), 12, 31)));
            responses.addAll(fetchScheduleResponses(LocalDate.of(endDate.getYear(), 1, 1), endDate));
        }

        return responses;
    }

    private List<ScheduleResponse> fetchScheduleResponses(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, List<ScheduleListDto>> list = academicCalendarRepository.findByStartDateBetween(startDate, endDate).stream()
                .sorted(Comparator.comparing(AcademicCalendar::getStartDate))
                .collect(Collectors.groupingBy(
                        academicCalendar -> academicCalendar.getStartDate().withDayOfMonth(1),
                        Collectors.mapping(ScheduleListDto::of, Collectors.toList())));

        List<ScheduleResponse> response = list.entrySet().stream()
                .map(entry -> ScheduleResponse.of(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(ScheduleResponse::getLocalDate))
                .collect(Collectors.toList());

        response.stream().findFirst().ifPresent(sr -> sr.setScheduleCriterionWithYear(sr.getLocalDate()));

        return response;
    }

}
