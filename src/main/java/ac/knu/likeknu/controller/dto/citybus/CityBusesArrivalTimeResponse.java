package ac.knu.likeknu.controller.dto.citybus;

import ac.knu.likeknu.domain.CityBus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalTime;

@Builder
public record CityBusesArrivalTimeResponse(String busNumber, String remainingTime, String busColor,
                                           @JsonIgnore LocalTime arrivalAt) {

    public static CityBusesArrivalTimeResponse of(CityBus cityBus, LocalTime arrivalTime, LocalTime currentTime) {
        long remainingTime = Duration.between(currentTime, arrivalTime).toMinutes();
        String remainingStatus = remainingTime <= 1 ? "곧 도착" : remainingTime + "분";
        return CityBusesArrivalTimeResponse.builder()
                .busNumber(cityBus.getBusNumber())
                .remainingTime(remainingStatus)
                .busColor(cityBus.getBusColor())
                .arrivalAt(arrivalTime)
                .build();
    }
}
