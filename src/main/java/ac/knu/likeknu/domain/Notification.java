package ac.knu.likeknu.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "notification")
@Entity
public class Notification {

    @Id
    private String id;

    @Column(nullable = false)
    private String notificationTitle;

    @Column(nullable = false)
    private String notificationBody;

    @Column(nullable = false)
    private LocalDateTime notificationDate;

    @Column(nullable = false)
    private String notificationUrl;

    @Column(nullable = false)
    private boolean read;

    @JoinTable(name = "device_notification",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "device_id"))
    @ManyToMany
    private List<Device> devices = new ArrayList<>();

    protected Notification() {
    }

    @Builder
    public Notification(String notificationTitle, String notificationBody, LocalDateTime notificationDate, String notificationUrl, Boolean read) {
        this.notificationTitle = notificationTitle;
        this.notificationBody = notificationBody;
        this.notificationDate = notificationDate;
        this.notificationUrl = notificationUrl;
        this.read = read;
    }
}
