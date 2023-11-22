package ac.knu.likeknu.service;

import ac.knu.likeknu.controller.dto.main.MainAnnouncementsResponse;
import ac.knu.likeknu.controller.dto.main.MainMenuResponse;
import ac.knu.likeknu.controller.dto.main.MainScheduleResponse;
import ac.knu.likeknu.domain.AcademicCalendar;
import ac.knu.likeknu.domain.Announcement;
import ac.knu.likeknu.domain.Cafeteria;
import ac.knu.likeknu.domain.value.Campus;
import ac.knu.likeknu.domain.value.Category;
import ac.knu.likeknu.domain.value.MealType;
import ac.knu.likeknu.repository.AcademicCalendarRepository;
import ac.knu.likeknu.repository.AnnouncementRepository;
import ac.knu.likeknu.repository.CafeteriaRepository;
import ac.knu.likeknu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MainService {

    private final AnnouncementRepository announcementRepository;
    private final MenuRepository menuRepository;
    private final CafeteriaRepository cafeteriaRepository;
    private final AcademicCalendarRepository academicCalendarRepository;

    public List<MainAnnouncementsResponse> getAnnouncementsResponse(Campus campus) {
        List<Campus> campusList = List.of(Campus.ALL, campus);

        List<Announcement> getAnnouncements =
                announcementRepository
                        .findTop4ByCampusInAndCategoryOrderByAnnouncementDateDesc(campusList, Category.STUDENT_NEWS);

        return getAnnouncements.stream()
                .map(MainAnnouncementsResponse::of)
                .collect(Collectors.toList());
    }

    public List<MainMenuResponse> getMenuResponse(Campus campus) {
        List<Cafeteria> cafeterias = cafeteriaRepository.findByCampus(campus);
        return cafeterias.stream()
                .sorted(Comparator.comparing(cafeteria -> cafeteria.getCafeteriaName().getSequence()))
                .map(cafeteria -> menuRepository.findByMenuDateAndCafeteriaAndMealType(LocalDate.now(), cafeteria, MealType.now())
                        .map(menu -> MainMenuResponse.of(cafeteria, menu.getMenus()))
                        .orElse(MainMenuResponse.empty(cafeteria)))
                .collect(Collectors.toList());
    }

    public List<MainScheduleResponse> getScheduleResponse() {
        List<AcademicCalendar> calendarList = academicCalendarRepository.findTop3ByStartDateBetweenOrderByStartDateAsc(
                LocalDate.now(), LocalDate.now().plusWeeks(4)
        );

        return calendarList.stream()
                .map(MainScheduleResponse::of)
                .collect(Collectors.toList());
    }
}
