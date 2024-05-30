package ac.knu.likeknu.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Table(name = "shuttle_bus")
@Entity
public class ShuttleBus {

    @Id
    private String id;

    private String busName;

    @JoinColumn(name = "shuttle_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Shuttle shuttle;

    @CollectionTable(name = "shuttle_time", joinColumns = @JoinColumn(name = "bus_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    private List<ShuttleTime> shuttleTimes = new ArrayList<>();

    protected ShuttleBus() {
    }

    @Builder
    public ShuttleBus(String busName, Shuttle shuttle, List<ShuttleTime> shuttleTimes) {
        this.busName = busName;
        this.shuttle = shuttle;
        this.shuttleTimes = shuttleTimes;
    }

    public LocalTime getDepartureTime() {
        return shuttleTimes.stream()
                .map(ShuttleTime::getArrivalTime)
                .min(LocalTime::compareTo)
                .orElse(LocalTime.of(23, 59, 59));
    }

    public boolean isAvailableToday() {
        return getDepartureTime().isAfter(LocalTime.now());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ShuttleBus that = (ShuttleBus) object;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
