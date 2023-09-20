package ac.knu.likeknu.controller.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseDto<T> {
    private final LocalDateTime timestamp;
    private final String message;
    private final ResponseData<T> data;

    protected ResponseDto(LocalDateTime timestamp, String message, ResponseData<T> data) {
        this.timestamp = timestamp;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDto<T> of(String message) {
        return new ResponseDto<>(LocalDateTime.now(), message, null);
    }

    public static <T> ResponseDto<T> of(T body) {
        return new ResponseDto<>(LocalDateTime.now(), null, new DefaultResponseData<>(body));
    }

    public static <T> ResponseDto<T> of(T body, String message) {
        return new ResponseDto<>(LocalDateTime.now(), message, new DefaultResponseData<>(body));
    }
}
