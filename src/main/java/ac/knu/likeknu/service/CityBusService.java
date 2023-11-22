package ac.knu.likeknu.service;

import ac.knu.likeknu.controller.dto.citybus.CityBusesArrivalTimeResponse;
import ac.knu.likeknu.controller.dto.citybus.CityBusesResponse;
import ac.knu.likeknu.controller.dto.main.MainCityBusResponse;
import ac.knu.likeknu.domain.CityBus;
import ac.knu.likeknu.domain.Route;
import ac.knu.likeknu.domain.value.Campus;
import ac.knu.likeknu.domain.value.RouteType;
import ac.knu.likeknu.repository.CityBusRepository;
import ac.knu.likeknu.repository.RouteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CityBusService {

    private static final int MAX_BUSES_SIZE = 5;

    private final RouteRepository routeRepository;
    private final CityBusRepository cityBusRepository;

    public CityBusService(RouteRepository routeRepository, CityBusRepository cityBusRepository) {
        this.routeRepository = routeRepository;
        this.cityBusRepository = cityBusRepository;
    }

    /**
     * 학교에서 외부로 가는 가장 금방 도착하는 시내버스 정보
     *
     * @param campus 캠퍼스
     * @return 캠퍼스별 학교에서 나가는 가장 빠른 시내버스 목록
     */
    public List<MainCityBusResponse> earliestOutgoingCityBuses(Campus campus) {
        return routeRepository.findByCampus(campus, Sort.by(Order.asc("origin"))).stream()
                .filter(route -> route.getRouteType().equals(RouteType.OUTGOING))
                .map(route -> {
                    CityBus earliestBus = getEarliestCityBus(cityBusRepository.findByRoutesContaining(route));
                    return earliestBus == null ? MainCityBusResponse.empty(route)
                            : MainCityBusResponse.of(route, earliestBus);
                })
                .toList();
    }

    private CityBus getEarliestCityBus(List<CityBus> buses) {
        return buses.stream()
                .filter(cityBus -> cityBus.getEarliestArrivalTime() != null)
                .min(Comparator.comparing(CityBus::getEarliestArrivalTime))
                .orElse(null);
    }

    /**
     * 특정 경로의 시내버스 도착 시간 조회
     *
     * @param routeId 경로 ID
     * @return 특정 경로의 시내버스 도착 시간 목록
     */
    public List<CityBusesResponse> getCityBusesArrivalTime(Campus campus, RouteType routeType) {
        List<Route> routes = routeRepository.findByCampusAndRouteType(campus, routeType);
        return routes.stream()
                .map(route -> {
                    List<CityBusesArrivalTimeResponse> cityBusesArrivalTime = getCityBusesArrivalTime(route);
                    return CityBusesResponse.of(route, cityBusesArrivalTime);
                })
                .toList();
    }

    private List<CityBusesArrivalTimeResponse> getCityBusesArrivalTime(Route route) {
        LocalTime currentTime = LocalTime.now();
        return cityBusRepository.findByRoutesContaining(route)
                .stream()
                .flatMap(cityBus -> getCloseArrivalTimesStream(cityBus, currentTime)
                        .map(arrivalTime -> CityBusesArrivalTimeResponse.of(cityBus, arrivalTime, currentTime)))
                .sorted(Comparator.comparing(CityBusesArrivalTimeResponse::arrivalAt))
                .limit(MAX_BUSES_SIZE)
                .toList();
    }

    private Stream<LocalTime> getCloseArrivalTimesStream(CityBus cityBus, LocalTime currentTime) {
        return cityBus.getArrivalTimes().stream()
                .filter(currentTime.minusMinutes(1)::isBefore)
                .filter(currentTime.plusMinutes(30)::isAfter);
    }
}
