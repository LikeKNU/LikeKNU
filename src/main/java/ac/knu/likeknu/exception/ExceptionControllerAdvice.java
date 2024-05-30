package ac.knu.likeknu.exception;

import ac.knu.likeknu.service.SlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    private final SlackService slackService;

    public ExceptionControllerAdvice(SlackService slackService) {
        this.slackService = slackService;
    }

    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<String> businessExceptionHandler(BusinessException exception) {
        String message = exception.getMessage();
        log.info("BusinessException: ", exception);
        slackService.sendMessage(message);
        return ResponseEntity.badRequest()
                .body(message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<String> missingRequestParameterExceptionHandler(
            MissingServletRequestParameterException exception) {
        String message = exception.getMessage();
        String parameterName = exception.getParameterName();
        log.warn("message = {}, parameterName = {}", message, parameterName);
        return ResponseEntity.badRequest()
                .body(message);
    }
}
