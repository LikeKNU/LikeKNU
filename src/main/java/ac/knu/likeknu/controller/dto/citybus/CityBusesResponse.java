package ac.knu.likeknu.controller.dto.citybus;

import ac.knu.likeknu.domain.Route;
import lombok.Builder;

import java.util.List;

@Builder
public record CityBusesResponse(String origin, String destination, String departureStop,
                                List<CityBusesArrivalTimeResponse> buses) {

    public static CityBusesResponse of(Route route, List<CityBusesArrivalTimeResponse> buses) {
        String departureStop = route.getDepartureStop();
        return CityBusesResponse.builder()
                .origin(route.getOrigin())
                .destination(route.getDestination())
                .departureStop(departureStop)
                .buses(buses)
                .build();
    }
}
