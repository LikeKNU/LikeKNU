package ac.knu.likeknu.domain;

import ac.knu.likeknu.controller.dto.device.request.DeviceRegistrationRequest;
import ac.knu.likeknu.domain.value.Campus;
import ac.knu.likeknu.domain.value.Tag;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "device")
@Entity
public class Device {

    @Id
    private String id;

    @Setter
    @Column(unique = true)
    private String fcmToken;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Campus campus;

    @Column(name = "notification", nullable = false)
    private boolean isTurnOnNotification;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @JoinTable(name = "device_notification",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id"))
    @ManyToMany
    private List<Notification> notifications = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @CollectionTable(name = "subscribe", joinColumns = @JoinColumn(name = "device_id"))
    @Column(name = "tag")
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Tag> subscribeTags = new ArrayList<>();

    protected Device() {
    }

    @Builder
    public Device(String id, String fcmToken, Campus campus, LocalDateTime registeredAt) {
        this.id = id;
        this.fcmToken = fcmToken;
        this.campus = campus;
        this.registeredAt = registeredAt;
    }

    public static Device of(DeviceRegistrationRequest request) {
        return Device.builder()
                .id(request.deviceId())
                .campus(Campus.CHEONAN)
                .registeredAt(LocalDateTime.now())
                .build();
    }

    public void updateSubscribesTags(List<Tag> tags) {
        subscribeTags.clear();
        subscribeTags.addAll(tags);
    }

    public void updateNotification(boolean isTurnOnNotification) {
        this.isTurnOnNotification = isTurnOnNotification;
    }
}
