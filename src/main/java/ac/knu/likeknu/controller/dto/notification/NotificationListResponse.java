package ac.knu.likeknu.controller.dto.notification;

import ac.knu.likeknu.domain.Notification;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class NotificationListResponse {

    private final String notificationId;
    private final String notificationTitle;
    private final String notificationBody;
    private final String notificationDate;
    private final String notificationUrl;
    @JsonProperty(value = "read")
    private final boolean isRead;

    @Builder
    public NotificationListResponse(String notificationId, String notificationTitle, String notificationBody, String notificationDate, String notificationUrl, boolean isRead) {
        this.notificationId = notificationId;
        this.notificationTitle = notificationTitle;
        this.notificationBody = notificationBody;
        this.notificationDate = notificationDate;
        this.notificationUrl = notificationUrl;
        this.isRead = isRead;
    }

    public static NotificationListResponse of(Notification notification) {
        LocalDateTime date = notification.getNotificationDate();
        Duration duration = Duration.between(date, LocalDateTime.now());
        String notificationDate = getFormattedNotificationDate(duration, date);
        return NotificationListResponse.builder()
                .notificationId(notification.getId())
                .notificationTitle(notification.getNotificationTitle())
                .notificationBody(notification.getNotificationBody())
                .notificationDate(notificationDate)
                .notificationUrl(notification.getNotificationUrl())
                .isRead(notification.isRead())
                .build();
    }

    private static String getFormattedNotificationDate(Duration duration, LocalDateTime date) {
        long seconds = duration.toSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        if (seconds < 60) {
            return seconds + "초 전";
        }
        if (minutes < 60) {
            return minutes + "분 전";
        }
        if (hours < 24) {
            return hours + "시간 전";
        }
        if (days <= 10) {
            return days + "일 전";
        }
        return date.format(DateTimeFormatter.ofPattern("M월 d일"));
    }
}
