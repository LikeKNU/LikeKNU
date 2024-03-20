package ac.knu.likeknu.controller;

import ac.knu.likeknu.controller.dto.base.ResponseDto;
import ac.knu.likeknu.controller.dto.citybus.CityBusesResponse;
import ac.knu.likeknu.controller.dto.shuttlebus.ShuttleBusesArrivalTimeResponse;
import ac.knu.likeknu.controller.dto.shuttlebus.ShuttleListResponse;
import ac.knu.likeknu.domain.value.Campus;
import ac.knu.likeknu.domain.value.RouteType;
import ac.knu.likeknu.logging.domain.value.LogType;
import ac.knu.likeknu.logging.service.LoggingService;
import ac.knu.likeknu.service.CityBusService;
import ac.knu.likeknu.service.ShuttleBusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/buses")
@RestController
public class BusController {

    private final CityBusService cityBusService;
    private final ShuttleBusService shuttleBusService;
    private final LoggingService loggingService;

    public BusController(CityBusService cityBusService, ShuttleBusService shuttleBusService, LoggingService loggingService) {
        this.cityBusService = cityBusService;
        this.shuttleBusService = shuttleBusService;
        this.loggingService = loggingService;
    }

    @GetMapping("/city-bus/{type}")
    private ResponseDto<List<CityBusesResponse>> cityBusesArrivalTime(
            @RequestParam("campus") Campus campus, @PathVariable("type") String type
    ) {
        RouteType routeType = RouteType.of(type);
        List<CityBusesResponse> cityBusesArrivalTime = cityBusService.getCityBusesArrivalTime(campus, routeType);
        return ResponseDto.of(cityBusesArrivalTime);
    }

    @GetMapping("/shuttle-bus/routes")
    public ResponseDto<List<ShuttleListResponse>> eachRouteShuttleBuses(@RequestParam("campus") Campus campus) {
        List<ShuttleListResponse> routeList = shuttleBusService.getRouteList(campus);
        return ResponseDto.of(routeList);
    }

    @GetMapping("/shuttle-bus/{shuttleId}")
    public ResponseDto<List<ShuttleBusesArrivalTimeResponse>> shuttleBusesArrivalTime(@PathVariable("shuttleId") String shuttleId) {
        loggingService.addLog(LogType.SELECT_SHUTTLE, shuttleId);
        List<ShuttleBusesArrivalTimeResponse> shuttleBusesArrivalTime = shuttleBusService.getShuttleBusesArrivalTime(shuttleId);
        return ResponseDto.of(shuttleBusesArrivalTime);
    }
}
