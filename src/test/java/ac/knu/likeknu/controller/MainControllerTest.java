package ac.knu.likeknu.controller;

import ac.knu.likeknu.controller.dto.main.MainAnnouncementsResponse;
import ac.knu.likeknu.controller.dto.main.MainCityBusResponse;
import ac.knu.likeknu.controller.dto.main.MainMenuResponse;
import ac.knu.likeknu.controller.dto.main.MainScheduleResponse;
import ac.knu.likeknu.controller.dto.menu.MenuListDto;
import ac.knu.likeknu.domain.CityBus;
import ac.knu.likeknu.domain.Route;
import ac.knu.likeknu.domain.value.Campus;
import ac.knu.likeknu.service.CityBusService;
import ac.knu.likeknu.service.MainService;
import ac.knu.likeknu.utils.TestInstanceFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@DisplayName("메인 컨트롤러 테스트")
@WebMvcTest(controllers = MainController.class)
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityBusService cityBusService;
    @MockBean
    private MainService mainService;

    @DisplayName("캠퍼스별 시내 버스 도착 정보 조회 API 요청에 성공한다.")
    @Test
    void getCityBusArrivalTimesSuccess() throws Exception {
        // given
        Route route1 = TestInstanceFactory.createRoute("Stop A", "Stop B", "A", "B");
        Route route2 = TestInstanceFactory.createRoute("Stop A", "Stop C", "A", "C");
        Route route3 = TestInstanceFactory.createRoute("Stop A", "Stop D", "A", "D");

        CityBus cityBus1 = TestInstanceFactory.createCityBus("100");
        CityBus cityBus2 = TestInstanceFactory.createCityBus("110");

        List<MainCityBusResponse> cheonanCityBuses = List.of(
                MainCityBusResponse.of(route1, cityBus1), MainCityBusResponse.of(route2, cityBus2), MainCityBusResponse.empty(route3)
        );

        List<MainCityBusResponse> singwanCityBuses = List.of(
                MainCityBusResponse.empty(route2), MainCityBusResponse.of(route3, cityBus2)
        );

        // when
        when(cityBusService.earliestOutgoingCityBuses(eq(Campus.CHEONAN)))
                .thenReturn(cheonanCityBuses);
        when(cityBusService.earliestOutgoingCityBuses(eq(Campus.SINGWAN)))
                .thenReturn(singwanCityBuses);

        ResultActions cheonanResultActions = mockMvc.perform(get("/api/main/buses")
                .param("campus", Campus.CHEONAN.name()));
        ResultActions singwanResultActions = mockMvc.perform(get("/api/main/buses")
                .param("campus", Campus.SINGWAN.name()));

        // then
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        cheonanResultActions.andExpectAll(
                status().isOk(),
                jsonPath("$.data.body.[0].routeId").value(route1.getId()),
                jsonPath("$.data.body.[0].busNumber").value(cityBus1.getBusNumber()),
                jsonPath("$.data.body.[0].remainingTime").value("곧 도착"),
                jsonPath("$.data.body.[0].arrivalTime").value(LocalTime.now().format(dateTimeFormatter))
        ).andDo(print());
        singwanResultActions.andExpectAll(
                status().isOk(),
                jsonPath("$.data.body.[0].routeId").value(route2.getId()),
                jsonPath("$.data.body.[0].busNumber").isEmpty(),
                jsonPath("$.data.body.[0].remainingTime").isEmpty(),
                jsonPath("$.data.body.[0].arrivalTime").isEmpty()
        ).andDo(print());
    }

    @DisplayName("campus 파라미터가 ALL이면 예외가 발생한다.")
    @Test
    void getCityBusArrivalTimesFailInvalidCampus() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/main/buses")
                .param("campus", Campus.ALL.name()));

        // then
        resultActions.andExpectAll(
                status().isBadRequest(),
                content().string("Invalid campus")
        ).andDo(print());
    }

    @DisplayName("공지사항 조회 API 요청에 성공한다.")
    @Test
    void getAnnouncements() throws Exception {
        //given
        MainAnnouncementsResponse announcementsResponse1 = new MainAnnouncementsResponse("Test1", "TestTitle1", "TestUrl1");
        MainAnnouncementsResponse announcementsResponse2 = new MainAnnouncementsResponse("Test2", "TestTitle2", "TestUrl2");
        MainAnnouncementsResponse announcementsResponse3 = new MainAnnouncementsResponse("Test3", "TestTitle3", "TestUrl3");

        List<MainAnnouncementsResponse> announcements = List.of(announcementsResponse1, announcementsResponse2, announcementsResponse3);

        //when, then
        when(mainService.getAnnouncementsResponse(Campus.CHEONAN)).thenReturn(announcements);

        mockMvc.perform(
                get("/api/main/announcements")
                        .param("campus", Campus.CHEONAN.name())
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.data.body.[0].announcementId").value(announcementsResponse1.getAnnouncementId()),
                jsonPath("$.data.body.[1].announcementTitle").value(announcementsResponse2.getAnnouncementTitle()),
                jsonPath("$.data.body.[2].announcementUrl").value(announcementsResponse3.getAnnouncementUrl())
        ).andDo(print());

    }

    @DisplayName("캠퍼스별 식단 정보 조회 API 요청에 성공한다.")
    @Test
    void getMenuResponsesAndSuccess() throws Exception {
        //given
        List<MenuListDto> menus = List.of(
                new MenuListDto(1, "testMenu1"),
                new MenuListDto(2, "testMenu2")
        );

        MainMenuResponse menuResponse1 = new MainMenuResponse("cafeteriaId1", "학생식당", "아침", menus);
        MainMenuResponse menuResponse2 = new MainMenuResponse("cafeteriaId2", "생활관식당", "점심", menus);
        MainMenuResponse menuResponse3 = new MainMenuResponse("cafeteriaId2", "생활관식당", "저녁", menus);
        MainMenuResponse menuResponse4 = new MainMenuResponse("cafeteriaId3", "직원식당", "아침", menus);

        List<MainMenuResponse> menuResponses1 = new ArrayList<>(List.of(menuResponse1, menuResponse2, menuResponse4));
        List<MainMenuResponse> menuResponses2 = new ArrayList<>(List.of(menuResponse1, menuResponse3, menuResponse4));

        //when
        when(mainService.getMenuResponse(eq(Campus.CHEONAN))).thenReturn(menuResponses1);
        when(mainService.getMenuResponse(eq(Campus.SINGWAN))).thenReturn(menuResponses2);

        ResultActions resultActions1 = mockMvc.perform(
                get("/api/main/menu")
                        .param("campus", Campus.CHEONAN.name())
        );

        ResultActions resultActions2 = mockMvc.perform(
                get("/api/main/menu")
                        .param("campus", Campus.SINGWAN.name())
        );

        //then
        resultActions1.andExpectAll(
                status().isOk(),
                jsonPath("$.data.body.[0].cafeteriaId").value(menuResponse1.getCafeteriaId()),
                jsonPath("$.data.body.[1].cafeteriaName").value(menuResponse2.getCafeteriaName()),
                jsonPath("$.data.body.[0].menus.[0].menuId").value(menuResponse1.getMenus().get(0).getMenuId()),
                jsonPath("$.data.body.[2].menus.[1].menuName").value(menuResponse4.getMenus().get(1).getMenuName())
        ).andDo(print());

        resultActions2.andExpectAll(
                status().isOk(),
                jsonPath("$.data.body.[0].cafeteriaId").value(menuResponse1.getCafeteriaId()),
                jsonPath("$.data.body.[1].cafeteriaName").value(menuResponse3.getCafeteriaName()),
                jsonPath("$.data.body.[0].menus.[0].menuId").value(menuResponse1.getMenus().get(0).getMenuId()),
                jsonPath("$.data.body.[2].menus.[1].menuName").value(menuResponse4.getMenus().get(1).getMenuName())
        ).andDo(print());

    }

    @DisplayName("학사일정 정보 조회 API 요청에 성공한다.")
    @Test
    void getAcademicCalendar() throws Exception {
        //given
        MainScheduleResponse calendar1 = new MainScheduleResponse("Test1", "TestContents1", "10/01 ~ 10/05", true);
        MainScheduleResponse calendar2 = new MainScheduleResponse("Test2", "TestContents2", "10/05", false);
        MainScheduleResponse calendar3 = new MainScheduleResponse("Test3", "TestContents3", "10/14", false);

        List<MainScheduleResponse> list = List.of(calendar1, calendar2, calendar3);

        //when
        when(mainService.getScheduleResponse()).thenReturn(list);

        ResultActions resultActions = mockMvc.perform(get("/api/main/schedule"));

        //then
        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("$.data.body.[0].scheduleId").value(calendar1.getScheduleId()),
                jsonPath("$.data.body.[1].scheduleContents").value(calendar2.getScheduleContents()),
                jsonPath("$.data.body.[2].scheduleDate").value(calendar3.getScheduleDate())
        ).andDo(print());

    }
}