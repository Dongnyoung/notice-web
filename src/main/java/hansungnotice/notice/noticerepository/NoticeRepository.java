package hansungnotice.notice.noticerepository;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    /**
     * 타입별 공지사항 조회 (최신순)
     */
    List<Notice> findByTypeOrderByPostedDateDesc(NoticeType type);
    
    /**
     * 타입별 공지사항 조회 (페이징)
     */
    Page<Notice> findByTypeOrderByPostedDateDesc(NoticeType type, Pageable pageable);
    
    /**
     * 최근 공지사항 조회 (전체)
     */
    List<Notice> findAllByOrderByPostedDateDesc();
    
    /**
     * 특정 날짜 이후의 공지사항 조회
     */
    List<Notice> findByPostedDateAfterOrderByPostedDateDesc(LocalDate date);
    
    /**
     * 제목에 키워드가 포함된 공지사항 검색
     */
    List<Notice> findByTitleContainingIgnoreCaseOrderByPostedDateDesc(String keyword);
    
    /**
     * 타입별 최신 N개 공지사항 조회
     */
    @Query("SELECT n FROM Notice n WHERE n.type = :type ORDER BY n.postedDate DESC")
    List<Notice> findTopByTypeOrderByPostedDateDesc(@Param("type") NoticeType type, Pageable pageable);

    boolean existsByTitleAndPostedDateAndType(String title, LocalDate postedDate, NoticeType type);

    /**
     * 타입별 공지사항 개수 조회
     */
    long countByType(NoticeType type);
}
