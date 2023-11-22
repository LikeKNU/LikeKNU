package ac.knu.likeknu.controller.dto.device.response;

import ac.knu.likeknu.domain.value.Tag;

public record SubscribeTagListResponse(String tag) {

    public static SubscribeTagListResponse of(Tag tag) {
        return new SubscribeTagListResponse(tag.getTagName());
    }
}
