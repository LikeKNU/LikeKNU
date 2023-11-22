package ac.knu.likeknu.controller;

import ac.knu.likeknu.controller.dto.base.PageDto;
import ac.knu.likeknu.controller.dto.base.PageResponseDto;
import ac.knu.likeknu.controller.dto.notification.NotificationListResponse;
import ac.knu.likeknu.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Period;
import java.util.List;

@RequestMapping("/api/notifications")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public PageResponseDto<List<NotificationListResponse>> notificationListWithin30Days(
            @RequestParam(name = "deviceId") String deviceId,
            @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        PageDto pageDto = PageDto.of(page);
        List<NotificationListResponse> notificationList =
                notificationService.getNotificationList(deviceId, Period.ofDays(30), pageDto);
        return PageResponseDto.of(notificationList, pageDto);
    }
}
