package hansungnotice.notice.service;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolNoticeService {

    private final NoticeService noticeService;
    private final CrawlingService crawlingService;

    /**
     * 학교 공지사항 크롤링 및 저장
     */
    @Transactional
    public void fetchAndSaveNotices(String url) {
        log.info("학교 공지사항 크롤링 시작: {}", url);
        
        // 기존 학교 공지사항 삭제
        noticeService.deleteNoticesByType(NoticeType.SCHOOL);
        
        // 새로운 공지사항 크롤링 및 저장
        List<Notice> notices = crawlingService.crawlSchoolNotices(url);
        noticeService.saveAllNotices(notices);
        
        log.info("학교 공지사항 크롤링 완료: {}개 저장", notices.size());
    }

    /**
     * 학교 공지사항 조회
     */
    public List<Notice> getSchoolNotices() {
        return noticeService.getNoticesByType(NoticeType.SCHOOL);
    }
}
