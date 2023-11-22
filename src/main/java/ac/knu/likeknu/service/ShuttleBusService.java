package ac.knu.likeknu.service;

import ac.knu.likeknu.controller.dto.shuttlebus.ShuttleBusesArrivalTimeResponse;
import ac.knu.likeknu.controller.dto.shuttlebus.ShuttleListResponse;
import ac.knu.likeknu.domain.Shuttle;
import ac.knu.likeknu.domain.ShuttleBus;
import ac.knu.likeknu.domain.value.Campus;
import ac.knu.likeknu.exception.BusinessException;
import ac.knu.likeknu.repository.ShuttleBusRepository;
import ac.knu.likeknu.repository.ShuttleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class ShuttleBusService {

    private final ShuttleRepository shuttleRepository;
    private final ShuttleBusRepository shuttleBusRepository;

    public ShuttleBusService(ShuttleRepository shuttleRepository, ShuttleBusRepository shuttleBusRepository) {
        this.shuttleRepository = shuttleRepository;
        this.shuttleBusRepository = shuttleBusRepository;
    }

    /**
     * 셔틀버스 경로 목록 조회
     *
     * @param campus      캠퍼스
     * @param shuttleType 셔틀버스 타입 (등교버스•순환버스)
     * @return 캠퍼스별 셔틀버스 경로 목록
     */
    public List<ShuttleListResponse> getRouteList(Campus campus) {
        return shuttleRepository.findByCampusesContains(campus)
                .stream()
                .map(ShuttleListResponse::of)
                .toList();
    }

    /**
     * 셔틀버스 경유지 및 도착 시간 조회
     *
     * @param shuttleId 셔틀 ID
     * @return 특정 셔틀의 버스 목록과 경유지 및 도착 시간 정보
     */
    public List<ShuttleBusesArrivalTimeResponse> getShuttleBusesArrivalTime(String shuttleId) {
        Shuttle shuttle = shuttleRepository.findById(shuttleId)
                .orElseThrow(() -> new BusinessException(String.format("Shuttle not found [%s]", shuttleId)));
        return shuttleBusRepository.findByShuttle(shuttle)
                .stream()
                .sorted(Comparator.comparing(ShuttleBus::getDepartureTime))
                .map(ShuttleBusesArrivalTimeResponse::of)
                .toList();
    }
}
