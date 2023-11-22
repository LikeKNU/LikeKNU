package ac.knu.likeknu.controller;

import ac.knu.likeknu.controller.dto.base.ResponseDto;
import ac.knu.likeknu.controller.dto.schedule.ScheduleResponse;
import ac.knu.likeknu.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseDto<List<ScheduleResponse>> getSchedule() {
        List<ScheduleResponse> responses = scheduleService.getScheduleResponses();
        return ResponseDto.of(responses);
    }

}
