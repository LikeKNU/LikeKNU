package ac.knu.likeknu.service;

import ac.knu.likeknu.controller.dto.announcement.AnnouncementListResponse;
import ac.knu.likeknu.controller.dto.base.PageDto;
import ac.knu.likeknu.domain.Announcement;
import ac.knu.likeknu.domain.Device;
import ac.knu.likeknu.domain.constants.Campus;
import ac.knu.likeknu.domain.constants.Category;
import ac.knu.likeknu.domain.constants.Tag;
import ac.knu.likeknu.repository.AnnouncementRepository;
import ac.knu.likeknu.repository.DeviceRepository;
import ac.knu.likeknu.utils.TestInstanceFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@DisplayName("공지사항 비즈니스 로직 테스트")
@ExtendWith(value = MockitoExtension.class)
class AnnouncementServiceTest {

    @InjectMocks
    private AnnouncementService announcementService;

    @Mock
    private AnnouncementRepository announcementRepository;
    @Mock
    private DeviceRepository deviceRepository;

    @DisplayName("학생소식 조회에 성공한다.")
    @Test
    void getStudentNewsSuccess() throws Exception {
        // given
        Announcement announcement1 = TestInstanceFactory.createAnnouncement("Test A", "https://testa.com", Tag.SCHOLARSHIP);
        Announcement announcement2 = TestInstanceFactory.createAnnouncement("Test B", "https://testb.com", Tag.INTERNSHIP);
        Announcement announcement3 = TestInstanceFactory.createAnnouncement("Test C", "https://testc.com", Tag.TUITION);
        PageDto pageDto = PageDto.of(1);

        // when
        when(announcementRepository.findByCampusInAndCategory(
                eq(Set.of(Campus.ALL, Campus.CHEONAN)), eq(Category.STUDENT_NEWS), any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(announcement1, announcement2, announcement3), PageRequest.of(0, 10), 1));
        when(deviceRepository.findById(any(String.class)))
                .thenReturn(Optional.of(new Device("deviceA", null, Campus.SINGWAN, LocalDateTime.now(), null, null)));

        List<AnnouncementListResponse> announcementList =
                announcementService.getAnnouncements(Campus.CHEONAN, Category.STUDENT_NEWS, pageDto, "", "");

        // then
        AnnouncementListResponse announcementResponse = announcementList.get(0);
        assertAll(
                () -> assertThatList(announcementList).size().isEqualTo(3),
                () -> assertThat(announcementResponse.announcementId()).isEqualTo(announcement1.getId()),
                () -> assertThat(announcementResponse.announcementTag()).isEqualTo(announcement1.getTag().getTagName()),
                () -> assertThat(pageDto.getTotalPages()).isEqualTo(1)
        );
    }
}