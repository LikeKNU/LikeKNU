package ac.knu.likeknu.repository;

import ac.knu.likeknu.domain.Device;
import ac.knu.likeknu.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    Page<Notification> findByDevicesContainingAndNotificationDateGreaterThanEqual(Device device, LocalDateTime fromDate, Pageable pageable);
}
