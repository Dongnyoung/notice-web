
package hansungnotice.notice.noticerepository;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("공지사항 저장 및 조회 테스트")
    void saveAndFindTest() {
        // given
        Notice notice = new Notice("테스트 공지", LocalDate.now(), "http://example.com", NoticeType.STUDENT);
        noticeRepository.save(notice);

        // when
        List<Notice> notices = noticeRepository.findAll();

        // then
        assertThat(notices).hasSize(1);
        assertThat(notices.get(0).getTitle()).isEqualTo("테스트 공지");
    }
}
