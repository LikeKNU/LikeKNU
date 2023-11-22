package ac.knu.likeknu.controller;

import ac.knu.likeknu.controller.dto.citybus.CityBusesArrivalTimeResponse;
import ac.knu.likeknu.controller.dto.citybus.CityBusesResponse;
import ac.knu.likeknu.domain.CityBus;
import ac.knu.likeknu.domain.Route;
import ac.knu.likeknu.domain.value.Campus;
import ac.knu.likeknu.domain.value.RouteType;
import ac.knu.likeknu.service.CityBusService;
import ac.knu.likeknu.service.ShuttleBusService;
import ac.knu.likeknu.utils.TestInstanceFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("버스 컨트롤러 테스트")
@WebMvcTest(controllers = BusController.class)
class BusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityBusService cityBusService;
    @MockBean
    private ShuttleBusService shuttleBusService;

    @DisplayName("캠퍼스별 시내버스 도착시간 조회 API 요청에 성공한다.")
    @Test
    void fetchEachRouteCityBusesSuccess() throws Exception {
        // given
        Route route1 = TestInstanceFactory
                .createRoute("Stop A", "Stop B", "A", "B");
        Route route2 = TestInstanceFactory
                .createRoute("Stop A", "Stop C", "A", "C");
        Route route3 = TestInstanceFactory
                .createRoute("Stop D", "Stop A", "A", "B");

        CityBus cityBus1 = TestInstanceFactory.createCityBus("100");
        CityBus cityBus2 = TestInstanceFactory.createCityBus("110");
        CityBus cityBus3 = TestInstanceFactory.createCityBus("120");
        CityBus cityBus4 = TestInstanceFactory.createCityBus("130");

        LocalTime arrivalTime1 = cityBus1.getArrivalTimes().get(0);
        LocalTime arrivalTime2 = cityBus2.getArrivalTimes().get(0);
        LocalTime arrivalTime3 = cityBus3.getArrivalTimes().get(0);
        LocalTime arrivalTime4 = cityBus4.getArrivalTimes().get(0);
        CityBusesArrivalTimeResponse cityBusesArrivalTime1 = CityBusesArrivalTimeResponse.of(cityBus1, arrivalTime1, LocalTime.now());
        CityBusesArrivalTimeResponse cityBusesArrivalTime2 = CityBusesArrivalTimeResponse.of(cityBus2, arrivalTime2, LocalTime.now());
        CityBusesArrivalTimeResponse cityBusesArrivalTime3 = CityBusesArrivalTimeResponse.of(cityBus3, arrivalTime3, LocalTime.now());
        CityBusesArrivalTimeResponse cityBusesArrivalTime4 = CityBusesArrivalTimeResponse.of(cityBus4, arrivalTime4, LocalTime.now());

        // when
        when(cityBusService.getCityBusesArrivalTime(eq(Campus.CHEONAN), eq(RouteType.INCOMING)))
                .thenReturn(List.of(
                        CityBusesResponse.of(route1, List.of(cityBusesArrivalTime1)),
                        CityBusesResponse.of(route2, List.of(cityBusesArrivalTime3, cityBusesArrivalTime4))
                ));

        when(cityBusService.getCityBusesArrivalTime(eq(Campus.CHEONAN), eq(RouteType.OUTGOING)))
                .thenReturn(List.of(
                        CityBusesResponse.of(route3, List.of(cityBusesArrivalTime2))
                ));

        ResultActions incomingResultActions = mockMvc.perform(get("/api/buses/city-bus/incoming")
                .queryParam("campus", "CHEONAN"));
        ResultActions outgoingResultActions = mockMvc.perform(get("/api/buses/city-bus/outgoing")
                .queryParam("campus", "CHEONAN"));

        // then
        incomingResultActions.andExpectAll(
                status().isOk(),
                jsonPath("$.data.body.[0].origin").value(route1.getOrigin()),
                jsonPath("$.data.body.[0].destination").value(route1.getDestination()),
                jsonPath("$.data.body.[0].departureStop").value(route1.getDepartureStop()),
                jsonPath("$.data.body.[0].buses.[0].busNumber").value(cityBus1.getBusNumber())
        ).andDo(print());

        outgoingResultActions.andExpectAll(
                status().isOk(),
                jsonPath("$.data.body.[0].origin").value(route3.getOrigin()),
                jsonPath("$.data.body.[0].destination").value(route3.getDestination()),
                jsonPath("$.data.body.[0].departureStop").value(route3.getDepartureStop()),
                jsonPath("$.data.body.[0].buses.[0].busNumber").value(cityBus2.getBusNumber())
        ).andDo(print());
    }

    @DisplayName("존재하지 않는 캠퍼스의 경우 시내버스 경로 목록 API 요청에 실패한다.")
    @Test
    void fetchEachRouteCityBusesFailCampusNotFound() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/buses/city-bus/routes")
                .queryParam("campus", "invalid_campus"));

        // then
        resultActions.andExpectAll(
                status().isBadRequest()
        ).andDo(print());
    }
}