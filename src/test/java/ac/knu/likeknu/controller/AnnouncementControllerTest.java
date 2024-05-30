package ac.knu.likeknu.controller;

import ac.knu.likeknu.controller.dto.announcement.AnnouncementListResponse;
import ac.knu.likeknu.controller.dto.base.PageDto;
import ac.knu.likeknu.domain.Announcement;
import ac.knu.likeknu.domain.constants.Campus;
import ac.knu.likeknu.domain.constants.Category;
import ac.knu.likeknu.fixture.AnnouncementFixture;
import ac.knu.likeknu.service.AnnouncementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AnnouncementController 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AnnouncementControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AnnouncementService announcementService;

    Announcement announcement1;
    Announcement announcement2;

    @BeforeEach
    void setUp() {
        announcement1 = AnnouncementFixture.createAnnouncementWithTitle("공지사항1");
        announcement2 = AnnouncementFixture.createAnnouncementWithTitle("공지사항2");
    }

    @DisplayName("공지사항 목록 조회에 성공한다")
    @Test
    void should_success_fetchAnnouncements() throws Exception {
        given(announcementService.getAnnouncements(any(Campus.class), any(Category.class), any(PageDto.class), nullable(String.class)))
                .willReturn(List.of(AnnouncementListResponse.of(announcement1), AnnouncementListResponse.of(announcement2)));

        mockMvc.perform(get("/api/announcements/{category}", Category.STUDENT_NEWS.getPathVariable())
                        .queryParam("campus", Campus.SINGWAN.name()))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.body[*].announcementId").hasJsonPath(),
                        jsonPath("$.data.body[*].announcementTitle").hasJsonPath(),
                        jsonPath("$.data.body[*].announcementDate").hasJsonPath(),
                        jsonPath("$.data.body[*].announcementTag").hasJsonPath(),
                        jsonPath("$.data.body[*].announcementUrl").hasJsonPath(),
                        jsonPath("$.data.body[*].isBookmarked").hasJsonPath()
                );
    }

    @DisplayName("공지사항 제목 검색 조회에 성공한다")
    @Test
    void should_success_fetchSearchAnnouncements() throws Exception {
        given(announcementService.searchAnnouncements(any(Campus.class), any(PageDto.class), anyString(), nullable(String.class)))
                .willReturn(List.of(AnnouncementListResponse.of(announcement1), AnnouncementListResponse.of(announcement2)));

        mockMvc.perform(get("/api/announcements")
                        .queryParam("campus", Campus.SINGWAN.name())
                        .queryParam("keyword", "항1"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.body[*].announcementId").hasJsonPath(),
                        jsonPath("$.data.body[*].announcementTitle").hasJsonPath(),
                        jsonPath("$.data.body[*].announcementDate").hasJsonPath(),
                        jsonPath("$.data.body[*].announcementTag").hasJsonPath(),
                        jsonPath("$.data.body[*].announcementUrl").hasJsonPath(),
                        jsonPath("$.data.body[*].isBookmarked").hasJsonPath()
                );
    }
}
