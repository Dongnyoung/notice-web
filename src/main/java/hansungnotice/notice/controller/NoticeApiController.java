package hansungnotice.notice.controller;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import hansungnotice.notice.service.NoticeService;
import hansungnotice.notice.service.PointNoticeService;
import hansungnotice.notice.service.SchoolNoticeService;
import hansungnotice.notice.service.StudentNoticeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@Slf4j
public class NoticeApiController {

    private final StudentNoticeService studentNoticeService;
    private final SchoolNoticeService schoolNoticeService;
    private final PointNoticeService pointNoticeService;
    private final NoticeService noticeService;

    // URL 상수
    private static final String STUDENT_URL = "https://www.hansung.ac.kr/hansung/8385/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGaGFuc3VuZyUyRjE0MyUyRmFydGNsTGlzdC5kbyUzRmJic0NsU2VxJTNEMjM2JTI2YmJzT3BlbldyZFNlcSUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNnNyY2hDb2x1bW4lM0RzaiUyNnNyY2hXcmQlM0QlMjY%3D";
    private static final String SCHOOL_URL = "https://www.hansung.ac.kr/hansung/8385/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGaGFuc3VuZyUyRjE0MyUyRmFydGNsTGlzdC5kbyUzRmJic0NsU2VxJTNEMTY2JTI2YmJzT3BlbldyZFNlcSUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNnNyY2hDb2x1bW4lM0RzaiUyNnNyY2hXcmQlM0QlMjY%3D";
    private static final String POINT_URL = "https://hsportal.hansung.ac.kr/ko/program";

    @PostConstruct
    public void init() {
        log.info("애플리케이션 시작 - 초기 공지사항 크롤링 실행");
        fetchAllNotices();
    }

    /**
     * 수동 크롤링 실행
     */
    @PostMapping("/crawl")
    public ResponseEntity<Map<String, Object>> fetchNotices() {
        log.info("수동 크롤링 요청 받음");
        fetchAllNotices();
        
        Map<String, Object> response = Map.of(
            "message", "크롤링이 완료되었습니다.",
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 스케줄된 크롤링 (오전 9시, 오후 9시)
     */
    @Scheduled(cron = "0 0 9,21 * * *", zone = "Asia/Seoul")
    public void scheduledCrawl() {
        log.info("스케줄된 크롤링 실행");
        fetchAllNotices();
    }

    /**
     * 모든 공지사항 크롤링
     */
    private void fetchAllNotices() {
        try {
            studentNoticeService.fetchAndSaveNotices(STUDENT_URL);
            schoolNoticeService.fetchAndSaveNotices(SCHOOL_URL);
            pointNoticeService.fetchAndSaveNotices(POINT_URL);
            log.info("모든 공지사항 크롤링 완료");
        } catch (Exception e) {
            log.error("크롤링 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    /**
     * 학생 공지사항 조회
     */
    @GetMapping("/student")
    public ResponseEntity<List<Notice>> getStudentNotices() {
        List<Notice> notices = studentNoticeService.getStudentNotices();
        return ResponseEntity.ok(notices);
    }

    /**
     * 학교 공지사항 조회
     */
    @GetMapping("/school")
    public ResponseEntity<List<Notice>> getSchoolNotices() {
        List<Notice> notices = schoolNoticeService.getSchoolNotices();
        return ResponseEntity.ok(notices);
    }

    /**
     * 비교과 공지사항 조회
     */
    @GetMapping("/point")
    public ResponseEntity<List<Notice>> getPointNotices() {
        List<Notice> notices = pointNoticeService.getPointNotices();
        return ResponseEntity.ok(notices);
    }

    /**
     * 전체 공지사항 조회
     */
    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        List<Notice> notices = noticeService.getRecentNotices();
        return ResponseEntity.ok(notices);
    }

    /**
     * 공지사항 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<Notice>> searchNotices(@RequestParam String keyword) {
        List<Notice> notices = noticeService.searchNoticesByTitle(keyword);
        return ResponseEntity.ok(notices);
    }

    /**
     * 공지사항 통계 조회
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getNoticeStats() {
        Map<String, Object> stats = Map.of(
            "student", noticeService.getNoticeCountByType(NoticeType.STUDENT),
            "school", noticeService.getNoticeCountByType(NoticeType.SCHOOL),
            "point", noticeService.getNoticeCountByType(NoticeType.POINT),
            "total", noticeService.getNoticeCountByType(NoticeType.STUDENT) + 
                    noticeService.getNoticeCountByType(NoticeType.SCHOOL) + 
                    noticeService.getNoticeCountByType(NoticeType.POINT)
        );
        return ResponseEntity.ok(stats);
    }
}
