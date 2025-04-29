package hansungnotice.notice.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeTest {

    @Test
    @DisplayName("Notice 객체 생성 및 필드값 확인 테스트")
    void createNoticeTest() {
        // given
        String title = "공지사항 제목";
        LocalDate date = LocalDate.of(2025, 4, 29);
        String url = "http://example.com";
        NoticeType type = NoticeType.STUDENT;

        // when
        Notice notice = new Notice(title, date, url, type);

        // then
        assertThat(notice.getTitle()).isEqualTo(title);
        assertThat(notice.getPostedDate()).isEqualTo(date);
        assertThat(notice.getUrl()).isEqualTo(url);
        assertThat(notice.getType()).isEqualTo(type);
    }
}
