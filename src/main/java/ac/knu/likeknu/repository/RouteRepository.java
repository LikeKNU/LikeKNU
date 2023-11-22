package ac.knu.likeknu.repository;

import ac.knu.likeknu.domain.Route;
import ac.knu.likeknu.domain.value.Campus;
import ac.knu.likeknu.domain.value.RouteType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, String> {

    List<Route> findByCampus(Campus campus, Sort sort);

    List<Route> findByCampusAndRouteType(Campus campus, RouteType routeType);
}
