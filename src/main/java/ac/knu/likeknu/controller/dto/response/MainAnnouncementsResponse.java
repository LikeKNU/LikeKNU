package ac.knu.likeknu.controller.dto.response;

import ac.knu.likeknu.domain.Announcement;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MainAnnouncementsResponse {

    private Long announcementId;
    private String announcementTitle;
    private String announcementUrl;

    @Builder
    public MainAnnouncementsResponse(Long announcementId, String announcementTitle, String announcementUrl) {
        this.announcementId = announcementId;
        this.announcementTitle = announcementTitle;
        this.announcementUrl = announcementUrl;
    }

    public static MainAnnouncementsResponse of(Announcement announcement) {
        return MainAnnouncementsResponse.builder()
                .announcementId(announcement.getAnnouncementId())
                .announcementTitle(announcement.getAnnouncementTitle())
                .announcementUrl(announcement.getAnnouncementUrl())
                .build();
    }
}
