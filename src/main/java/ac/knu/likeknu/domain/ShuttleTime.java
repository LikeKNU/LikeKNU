package ac.knu.likeknu.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

@ToString
@Getter
@Table(name = "shuttle_time")
@Entity
public class ShuttleTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sequence;

    private String arrivalStop;

    private LocalTime arrivalTime;

    @JoinColumn(name = "bus_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ShuttleBus shuttleBus;

    protected ShuttleTime() {
    }

    @Builder
    public ShuttleTime(int sequence, String arrivalStop, LocalTime arrivalTime, ShuttleBus shuttleBus) {
        this.sequence = sequence;
        this.arrivalStop = arrivalStop;
        this.arrivalTime = arrivalTime;
        this.shuttleBus = shuttleBus;
    }
}
