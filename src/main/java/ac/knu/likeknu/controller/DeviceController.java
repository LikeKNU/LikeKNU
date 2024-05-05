package ac.knu.likeknu.controller;

import ac.knu.likeknu.controller.dto.base.ResponseDto;
import ac.knu.likeknu.controller.dto.device.request.BookmarkRequest;
import ac.knu.likeknu.controller.dto.device.request.CampusModificationRequest;
import ac.knu.likeknu.controller.dto.device.request.ChangeNotificationRequest;
import ac.knu.likeknu.controller.dto.device.request.DeviceRegistrationRequest;
import ac.knu.likeknu.controller.dto.device.request.DeviceTokenRequest;
import ac.knu.likeknu.controller.dto.device.request.SubscribeTagsUpdateRequest;
import ac.knu.likeknu.controller.dto.device.response.SubscribeTagListResponse;
import ac.knu.likeknu.controller.dto.device.response.TurnOnNotificationResponse;
import ac.knu.likeknu.service.BookmarkService;
import ac.knu.likeknu.service.DeviceService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/devices")
@RestController
public class DeviceController {

    private final DeviceService deviceService;
    private final BookmarkService bookmarkService;

    public DeviceController(DeviceService deviceService, BookmarkService bookmarkService) {
        this.deviceService = deviceService;
        this.bookmarkService = bookmarkService;
    }

    @PostMapping
    public ResponseDto<String> registerDevice(@RequestBody DeviceRegistrationRequest request) {
        deviceService.registerDevice(request);
        return ResponseDto.of("The device was successfully registered.");
    }

    @PutMapping("/campus")
    public ResponseDto<String> modifyCampus(@RequestBody CampusModificationRequest request) {
        deviceService.modifyCampusByDeviceId(request);
        return ResponseDto.of("Campus has been changed successfully.");
    }

    @PostMapping("/{deviceId}/bookmarks")
    public ResponseDto<String> createNewBookmark(
            @PathVariable("deviceId") String deviceId, @RequestBody BookmarkRequest request
    ) {
        bookmarkService.addAnnouncementBookmark(deviceId, request.announcementId());
        return ResponseDto.of("The bookmark has been created successfully.");
    }

    @DeleteMapping("/{deviceId}/bookmarks/{announcementId}")
    public ResponseDto<String> removeBookmark(
            @PathVariable("deviceId") String deviceId,
            @PathVariable("announcementId") String announcementId
    ) {
        bookmarkService.removeAnnouncementBookmark(deviceId, announcementId);
        return ResponseDto.of("The bookmark has been removed successfully.");
    }

    @PostMapping("/token")
    public ResponseDto<String> registerTokenByDevice(@RequestBody DeviceTokenRequest request) {
        deviceService.registerTokenByDevice(request);
        return ResponseDto.of("The token is well registered.");
    }

    @GetMapping("/subscribes")
    public ResponseDto<List<SubscribeTagListResponse>> deviceSubscribeTagList(
            @RequestParam("deviceId") String deviceId
    ) {
        List<SubscribeTagListResponse> subscribeTagList = deviceService.getSubscribeTagList(deviceId);
        return ResponseDto.of(subscribeTagList);
    }

    @PutMapping("/subscribes")
    public ResponseDto<String> updateDeviceSubscribeTagList(
            @RequestBody SubscribeTagsUpdateRequest subscribeTagsUpdateRequest
    ) {
        deviceService.updateSubscribeTagList(subscribeTagsUpdateRequest);
        return ResponseDto.of(null);
    }

    @GetMapping("/notifications")
    public ResponseDto<TurnOnNotificationResponse> whetherDeviceTurnOnNotifications(
            @RequestParam("deviceId") String deviceId
    ) {
        boolean isTurnOn = deviceService.isTurnOnPushNotifications(deviceId);
        return ResponseDto.of(new TurnOnNotificationResponse(isTurnOn));
    }

    @PutMapping("/notifications")
    public ResponseDto<String> updateTurnOnDeviceNotifications(
            @RequestBody ChangeNotificationRequest request
    ) {
        deviceService.changeDeviceNotifications(request);
        return ResponseDto.of("Device push notification settings have been changed.");
    }
}
