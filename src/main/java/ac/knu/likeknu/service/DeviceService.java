package ac.knu.likeknu.service;

import ac.knu.likeknu.controller.dto.device.request.CampusModificationRequest;
import ac.knu.likeknu.controller.dto.device.request.ChangeNotificationRequest;
import ac.knu.likeknu.controller.dto.device.request.DeviceRegistrationRequest;
import ac.knu.likeknu.controller.dto.device.request.DeviceTokenRequest;
import ac.knu.likeknu.controller.dto.device.request.SubscribeTagsUpdateRequest;
import ac.knu.likeknu.controller.dto.device.request.TagName;
import ac.knu.likeknu.controller.dto.device.response.SubscribeTagListResponse;
import ac.knu.likeknu.domain.Device;
import ac.knu.likeknu.domain.value.Campus;
import ac.knu.likeknu.domain.value.Tag;
import ac.knu.likeknu.exception.BusinessException;
import ac.knu.likeknu.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    public void registerDeviceId(DeviceRegistrationRequest deviceRequest) {
        Device device = Device.of(deviceRequest);
        if (deviceRepository.existsById(device.getId())) {
            return;
        }
        deviceRepository.save(device);
    }

    @Transactional
    public void modifyCampusByDeviceId(CampusModificationRequest campusModificationRequest) {
        String deviceId = campusModificationRequest.deviceId();
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new BusinessException(String.format("deviceId: %s does not exist.", deviceId)));

        try {
            Campus campus = Campus.valueOf(campusModificationRequest.campus());
            device.setCampus(campus);
        } catch (IllegalArgumentException exception) {
            throw new BusinessException("Invalid campus");
        }
    }

    @Transactional
    public void registerTokenByDevice(DeviceTokenRequest tokenRequest) {
        String deviceId = tokenRequest.deviceId();
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new BusinessException(String.format("deviceId: %s does not exist.", deviceId)));
        device.setFcmToken(tokenRequest.token());
    }

    public List<SubscribeTagListResponse> getSubscribeTagList(String deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new BusinessException(String.format("Device not found! [%s]", deviceId)));
        return device.getSubscribeTags().stream()
                .map(SubscribeTagListResponse::of)
                .toList();
    }

    @Transactional
    public void updateSubscribeTagList(SubscribeTagsUpdateRequest subscribeTagsUpdateRequest) {
        String deviceId = subscribeTagsUpdateRequest.deviceId();
        List<TagName> tagNames = subscribeTagsUpdateRequest.tags();
        List<Tag> tags = convertToTags(tagNames);
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new BusinessException(String.format("Device not found! [%s]", deviceId)));
        device.updateSubscribesTags(tags);
    }

    private List<Tag> convertToTags(List<TagName> tagNames) {
        return tagNames.stream()
                .distinct()
                .map(TagName::tag)
                .map(Tag::of)
                .toList();
    }

    public boolean isTurnOnPushNotifications(String deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new BusinessException(String.format("Device not found! [%s]", deviceId)));
        return device.isTurnOnNotification();
    }

    @Transactional
    public void changeDeviceNotifications(ChangeNotificationRequest request) {
        String deviceId = request.deviceId();
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new BusinessException(String.format("Device not found! [%s]", deviceId)));

        device.updateNotification(request.notification());
    }
}
