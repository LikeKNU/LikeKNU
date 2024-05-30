package ac.knu.likeknu.fixture;

import ac.knu.likeknu.domain.CityBus;
import ac.knu.likeknu.domain.Route;
import ac.knu.likeknu.domain.constants.Campus;
import ac.knu.likeknu.domain.constants.RouteType;

import java.time.LocalTime;
import java.util.List;

public final class CityBusFixture {

    public static Route createRoute() {
        return Route.builder()
                .routeType(RouteType.INCOMING)
                .departureStop("종합버스터미널")
                .arrivalStop("공주대학교")
                .origin("터미널")
                .destination("공주대")
                .campus(Campus.SINGWAN)
                .build();
    }

    public static CityBus createCityBus() {
        return CityBus.builder()
                .busNumber("123-1")
                .busName("123-1 공주대 방면")
                .busColor("95C53C")
                .busStop("종합버스터미널")
                .arrivalTimes(List.of(LocalTime.now().plusMinutes(5)))
                .build();
    }
}
