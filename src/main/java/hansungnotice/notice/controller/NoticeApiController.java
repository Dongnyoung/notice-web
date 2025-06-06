package hansungnotice.notice.controller;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import hansungnotice.notice.noticerepository.NoticeRepository;
import hansungnotice.notice.service.PointService;
import hansungnotice.notice.service.SchoolService;
import hansungnotice.notice.service.StudentNotice;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NoticeApiController {
    private final NoticeRepository noticeRepository;
    public NoticeApiController(NoticeRepository noticeRepository){
        this.noticeRepository=noticeRepository;
    }
    @Autowired
    private StudentNotice studentNotice;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private PointService pointService;

    @PostConstruct
    public void init() {
        fetchNotices();  // 서버 시작 시 1번 실행
    }

    @Scheduled(cron = "0 0 9,21 * * *", zone = "Asia/Seoul") //오전 9시, 오후 9시 두번 갱신
    // 크롤링하여 DB에 저장
    public void fetchNotices() {
        noticeRepository.deleteAll();
        String stuUrl = "https://www.hansung.ac.kr/hansung/8385/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGaGFuc3VuZyUyRjE0MyUyRmFydGNsTGlzdC5kbyUzRmJic0NsU2VxJTNEMjM2JTI2YmJzT3BlbldyZFNlcSUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNnNyY2hDb2x1bW4lM0RzaiUyNnNyY2hXcmQlM0QlMjY%3D";
        String schUrl = "https://www.hansung.ac.kr/hansung/8385/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGaGFuc3VuZyUyRjE0MyUyRmFydGNsTGlzdC5kbyUzRmJic0NsU2VxJTNEMTY2JTI2YmJzT3BlbldyZFNlcSUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNnNyY2hDb2x1bW4lM0RzaiUyNnNyY2hXcmQlM0QlMjY%3D";
        String poUrl = "https://hsportal.hansung.ac.kr/ko/program/all/list/0/1?sort=applicant";

        studentNotice.fetchAndSaveNotices(stuUrl, NoticeType.STUDENT);
        schoolService.schoolFetchAndSaveNotices(schUrl, NoticeType.SCHOOL);
        pointService.pointFetchAndSaveNotices(poUrl, NoticeType.POINT);
    }


    // 학생 공지사항 조회
    @GetMapping("/student")
    public List<Notice> getStudentNotices() {
        List<Notice> allNotices = studentNotice.getRecentNotices();
        // NoticeType.STUDENT인 공지사항만 필터링
        return allNotices.stream()
                .filter(notice -> notice.getType().equals(NoticeType.STUDENT))
                .collect(Collectors.toList());
    }

    // 학교 공지사항 조회
    @GetMapping("/school")
    public List<Notice> getSchoolNotices() {
        List<Notice> allNotices = schoolService.findNotice();
        // NoticeType.SCHOOL인 공지사항만 필터링
        return allNotices.stream()
                .filter(notice -> notice.getType().equals(NoticeType.SCHOOL))
                .collect(Collectors.toList());
    }

    // 비교과 공지사항 조회
    @GetMapping("/point")
    public List<Notice> getPointNotices() {
        List<Notice> allNotices = pointService.findPoint();
        // NoticeType.POINT인 공지사항만 필터링
        return allNotices.stream()
                .filter(notice -> notice.getType().equals(NoticeType.POINT))
                .collect(Collectors.toList());
    }
}
