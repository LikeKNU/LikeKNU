package ac.knu.likeknu.repository;

import ac.knu.likeknu.domain.AcademicCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AcademicCalendarRepository extends JpaRepository<AcademicCalendar, String> {

    List<AcademicCalendar> findTop3ByStartDateBetweenOrderByStartDateAsc(LocalDate start, LocalDate end);

    List<AcademicCalendar> findByStartDateBetween(LocalDate start, LocalDate end);
}
