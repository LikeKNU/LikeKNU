package ac.knu.likeknu.domain.value;

import ac.knu.likeknu.exception.BusinessException;
import ac.knu.likeknu.exception.ErrorMessage;

import java.util.Arrays;

public enum RouteType {

    OUTGOING, INCOMING;

    public static RouteType of(String type) {
        return Arrays.stream(values())
                .filter(routeType -> isSame(type, routeType))
                .findAny()
                .orElseThrow(() -> new BusinessException(ErrorMessage.INVALID_ROUTE_TYPE));
    }

    private static boolean isSame(String type, RouteType routeType) {
        String name = routeType.name();
        String lowerCase = name.toLowerCase();
        return lowerCase.equals(type);
    }
}
