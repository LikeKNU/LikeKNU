package ac.knu.likeknu.repository;

import ac.knu.likeknu.domain.Shuttle;
import ac.knu.likeknu.domain.value.Campus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShuttleRepository extends JpaRepository<Shuttle, String> {

    List<Shuttle> findByCampusesContains(Campus campus);
}
