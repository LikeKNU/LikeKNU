package ac.knu.likeknu.service;

import ac.knu.likeknu.controller.dto.base.PageDto;
import ac.knu.likeknu.controller.dto.notification.NotificationListResponse;
import ac.knu.likeknu.domain.Device;
import ac.knu.likeknu.domain.Notification;
import ac.knu.likeknu.exception.BusinessException;
import ac.knu.likeknu.repository.DeviceRepository;
import ac.knu.likeknu.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class NotificationService {

    private static final int DEFAULT_NOTIFICATION_PAGE_SIZE = 15;

    private final DeviceRepository deviceRepository;
    private final NotificationRepository notificationRepository;

    public NotificationService(DeviceRepository deviceRepository, NotificationRepository notificationRepository) {
        this.deviceRepository = deviceRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationListResponse> getNotificationList(String deviceId, Period period, PageDto pageDto) {
        LocalDateTime fromDate = LocalDate.now().minus(period).atStartOfDay();
        PageRequest pageRequest = PageRequest.of(pageDto.getCurrentPage() - 1, DEFAULT_NOTIFICATION_PAGE_SIZE,
                Sort.by(Sort.Order.desc("notificationDate")));
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new BusinessException(String.format("Device not found! [%s]", deviceId)));

        Page<Notification> notifications = notificationRepository
                .findByDevicesContainingAndNotificationDateGreaterThanEqual(device, fromDate, pageRequest);
        pageDto.updateTotalPages(notifications.getTotalPages());
        return notifications.stream()
                .map(NotificationListResponse::of)
                .toList();
    }
}
