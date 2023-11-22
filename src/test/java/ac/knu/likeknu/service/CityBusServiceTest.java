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
import ac.knu.likeknu.utils.TestInstanceFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@DisplayName("시내 버스 서비스 테스트")
@ExtendWith(value = MockitoExtension.class)
class CityBusServiceTest {

    @InjectMocks
    private CityBusService cityBusService;

    @Mock
    private RouteRepository routeRepository;
    @Mock
    private CityBusRepository cityBusRepository;

    @DisplayName("각 경로마다 가장 빨리 도착하는 버스 정보를 조회할 수 있다.")
    @Test
    void earliestOutgoingCityBusesSuccess() throws Exception {
        // given
        Route route1 = TestInstanceFactory.createRoute("정류장 A", "정류장 B");
        Route route2 = TestInstanceFactory.createRoute("정류장 A", "정류장 C");
        Route route3 = TestInstanceFactory.createRoute("정류장 D", "정류장 C");

        CityBus cityBus1 = TestInstanceFactory.createCityBus("100");
        CityBus cityBus2 = TestInstanceFactory.createCityBus("110");
        CityBus cityBus3 = TestInstanceFactory.createCityBus("120");
        CityBus cityBus4 = TestInstanceFactory.createCityBus("130");
        CityBus cityBus5 = TestInstanceFactory.createCityBus("140");

        when(routeRepository.findByCampus(eq(Campus.CHEONAN), any(Sort.class)))
                .thenReturn(List.of(route1, route2, route3));
        when(cityBusRepository.findByRoutesContaining(eq(route1)))
                .thenReturn(List.of(cityBus1, cityBus3));
        when(cityBusRepository.findByRoutesContaining(eq(route2)))
                .thenReturn(List.of(cityBus2, cityBus4));
        when(cityBusRepository.findByRoutesContaining(eq(route3)))
                .thenReturn(List.of(cityBus1, cityBus4, cityBus5));

        // when
        List<MainCityBusResponse> earliestCityBuses = cityBusService.earliestOutgoingCityBuses(Campus.CHEONAN);

        // then
        MainCityBusResponse mainCityBusResponse = earliestCityBuses.get(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        assertAll(
                () -> assertThatList(earliestCityBuses).size().isEqualTo(3),
                () -> assertThat(mainCityBusResponse.arrivalTime()).isEqualTo(LocalTime.now().format(dateTimeFormatter)),
                () -> assertThat(mainCityBusResponse.remainingTime()).isEqualTo("곧 도착"),
                () -> assertThat(mainCityBusResponse.busNumber()).isEqualTo("110")
        );
    }

    @DisplayName("캠퍼스별 시내버스 경로 목록을 조회할 수 있다.")
    @Test
    void getRouteListSuccess() throws Exception {
        // given
        Route route1 = TestInstanceFactory
                .createRoute("Stop A", "Stop B", "A", "B");
        Route route2 = TestInstanceFactory
                .createRoute("Stop A", "Stop C", "A", "C");
        Route route3 = TestInstanceFactory
                .createRoute("Stop D", "Stop A", "D", "B");

        CityBus cityBus1 = TestInstanceFactory.createCityBus("100");
        CityBus cityBus2 = TestInstanceFactory.createCityBus("110");
        CityBus cityBus3 = TestInstanceFactory.createCityBus("120");

        // when
        when(routeRepository.findByCampusAndRouteType(eq(Campus.CHEONAN), eq(RouteType.INCOMING)))
                .thenReturn(List.of(route1, route3));
        when(routeRepository.findByCampusAndRouteType(eq(Campus.CHEONAN), eq(RouteType.OUTGOING)))
                .thenReturn(List.of(route2));
        when(cityBusRepository.findByRoutesContaining(any(Route.class)))
                .thenReturn(List.of(cityBus1, cityBus2, cityBus3));

        List<CityBusesResponse> cityBusesArrivalTimeIncoming =
                cityBusService.getCityBusesArrivalTime(Campus.CHEONAN, RouteType.INCOMING);
        List<CityBusesResponse> cityBusesArrivalTimeOutgoing =
                cityBusService.getCityBusesArrivalTime(Campus.CHEONAN, RouteType.OUTGOING);

        LocalTime currentTime = LocalTime.now();

        // then
        CityBusesResponse cityBusesResponse1 = cityBusesArrivalTimeIncoming.get(0);
        List<CityBusesArrivalTimeResponse> buses1 = cityBusesResponse1.buses();
        CityBusesArrivalTimeResponse cityBusesArrivalTimeResponse1 = buses1.get(0);

        CityBusesResponse cityBusesResponse2 = cityBusesArrivalTimeIncoming.get(0);
        List<CityBusesArrivalTimeResponse> buses2 = cityBusesResponse1.buses();
        CityBusesArrivalTimeResponse cityBusesArrivalTimeResponse2 = buses1.get(0);

        assertAll(
                () -> assertThatList(cityBusesArrivalTimeIncoming).isNotEmpty(),
                () -> assertThatList(cityBusesArrivalTimeIncoming).hasSize(2),
                () -> assertThatList(buses1).hasSize(5),
                () -> assertThat(cityBusesResponse1.origin()).isEqualTo(route1.getOrigin()),
                () -> assertThat(cityBusesArrivalTimeResponse1.busNumber()).isEqualTo(cityBus1.getBusNumber())
        );

        assertAll(
                () -> assertThatList(cityBusesArrivalTimeOutgoing).isNotEmpty(),
                () -> assertThatList(cityBusesArrivalTimeOutgoing).hasSize(1),
                () -> assertThatList(buses2).hasSize(5),
                () -> assertThat(cityBusesResponse2.origin()).isEqualTo(route2.getOrigin()),
                () -> assertThat(cityBusesArrivalTimeResponse2.busNumber()).isEqualTo(cityBus1.getBusNumber())
        );
    }
}
