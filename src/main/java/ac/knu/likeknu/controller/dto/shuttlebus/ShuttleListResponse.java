package ac.knu.likeknu.controller.dto.shuttlebus;

import ac.knu.likeknu.domain.Shuttle;
import lombok.Builder;

@Builder
public record ShuttleListResponse(String shuttleId, String shuttleName, String note) {

    private static final String ROUTE_NAME_DELIMITER = " → ";

    public static ShuttleListResponse of(Shuttle shuttle) {
        String origin = shuttle.getOrigin();
        String destination = shuttle.getDestination();
        return ShuttleListResponse.builder()
                .shuttleId(shuttle.getId())
                .shuttleName(String.join(ROUTE_NAME_DELIMITER, origin, destination))
                .note(shuttle.getNote())
                .build();
    }
}
