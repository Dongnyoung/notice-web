package hansungnotice.notice.noticerepository;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByOrderByIdAsc();  // 내림차순 정렬
    //List<Notice> findByTitleContaining(String keyword);
}
