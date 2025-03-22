package hansungnotice.notice.noticerepository;

import hansungnotice.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByOrderByIdDesc();  // 최신 5개 공지 가져오기
    //List<Notice> findByTitleContaining(String keyword);
}
