package ac.knu.likeknu.controller;

import ac.knu.likeknu.controller.dto.citybus.CityBusesArrivalTimeResponse;
import ac.knu.likeknu.controller.dto.citybus.CityBusesResponse;
import ac.knu.likeknu.controller.dto.shuttlebus.ShuttleBusesArrivalTimeResponse;
import ac.knu.likeknu.controller.dto.shuttlebus.ShuttleListResponse;
import ac.knu.likeknu.domain.CityBus;
import ac.knu.likeknu.domain.Route;
import ac.knu.likeknu.domain.Shuttle;
import ac.knu.likeknu.domain.ShuttleBus;
import ac.knu.likeknu.domain.ShuttleTime;
import ac.knu.likeknu.domain.constants.Campus;
import ac.knu.likeknu.domain.constants.RouteType;
import ac.knu.likeknu.fixture.CityBusFixture;
import ac.knu.likeknu.fixture.ShuttleBusFixture;
import ac.knu.likeknu.service.CityBusService;
import ac.knu.likeknu.service.ShuttleBusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BusController 테스트")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class BusControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CityBusService cityBusService;
    @MockBean
    ShuttleBusService shuttleBusService;

    @DisplayName("시내버스 도착 시간 조회에 성공한다")
    @Test
    void should_success_fetchCityBusArrivalTimes() throws Exception {
        Route route = CityBusFixture.createRoute();
        CityBus cityBus = CityBusFixture.createCityBus();
        LocalTime currentTime = LocalTime.now();

        given(cityBusService.getCityBusesArrivalTime(any(Campus.class), any(RouteType.class)))
                .willReturn(List.of(
                        CityBusesResponse.of(route, cityBus.getArrivalTimes()
                                .stream()
                                .map(arrivalTime -> CityBusesArrivalTimeResponse.of(cityBus, arrivalTime, currentTime))
                                .toList())
                ));

        mockMvc.perform(get("/api/buses/city-bus/{type}", RouteType.INCOMING.name().toLowerCase())
                        .queryParam("campus", Campus.SINGWAN.name()))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.body[*].origin").hasJsonPath(),
                        jsonPath("$.data.body[*].destination").hasJsonPath(),
                        jsonPath("$.data.body[*].departureStop").hasJsonPath(),
                        jsonPath("$.data.body[*].buses").hasJsonPath(),
                        jsonPath("$.data.body[*].buses[*].busNumber").hasJsonPath(),
                        jsonPath("$.data.body[*].buses[*].remainingTime").hasJsonPath(),
                        jsonPath("$.data.body[*].buses[*].busColor").hasJsonPath()
                );
    }

    @DisplayName("셔틀 목록 조회에 성공한다")
    @Test
    void should_success_fetchShuttles() throws Exception {
        Shuttle shuttle = ShuttleBusFixture.createShuttle();

        given(shuttleBusService.getRouteList(any(Campus.class)))
                .willReturn(List.of(ShuttleListResponse.of(shuttle, "next")));

        mockMvc.perform(get("/api/buses/shuttle-bus/routes")
                        .queryParam("campus", Campus.SINGWAN.name()))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.body[*].shuttleId").hasJsonPath(),
                        jsonPath("$.data.body[*].shuttleName").hasJsonPath(),
                        jsonPath("$.data.body[*].note").hasJsonPath(),
                        jsonPath("$.data.body[*].nextDepartureTime").hasJsonPath()
                );
    }

    @DisplayName("셔틀버스 시간 조회에 성공한다")
    @Test
    void should_success_fetchShuttleBusTimes() throws Exception {
        List<ShuttleTime> shuttleTimes = ShuttleBusFixture.createShuttleTimes(3, Duration.ofMinutes(5));
        ShuttleBus shuttleBus = ShuttleBusFixture.createShuttleBusWithTimes(shuttleTimes);

        given(shuttleBusService.getShuttleBusesArrivalTime(any(String.class)))
                .willReturn(List.of(ShuttleBusesArrivalTimeResponse.of(shuttleBus)));

        mockMvc.perform(get("/api/buses/shuttle-bus/{shuttleId}", "shuttle_test"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.body[*].busName").hasJsonPath(),
                        jsonPath("$.data.body[*].isRunning").hasJsonPath(),
                        jsonPath("$.data.body[*].times").hasJsonPath(),
                        jsonPath("$.data.body[*].times[*].arrivalStop").hasJsonPath(),
                        jsonPath("$.data.body[*].times[*].arrivalTime").hasJsonPath()
                );
    }
}