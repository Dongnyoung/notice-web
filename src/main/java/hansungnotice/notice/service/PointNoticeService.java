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
public class PointNoticeService {

    private final NoticeService noticeService;
    private final CrawlingService crawlingService;

    /**
     * 비교과 공지사항 크롤링 및 저장
     */
    @Transactional
    public void fetchAndSaveNotices(String url) {
        log.info("비교과 공지사항 크롤링 시작: {}", url);
        
        // 기존 비교과 공지사항 삭제
        noticeService.deleteNoticesByType(NoticeType.POINT);
        
        // 새로운 공지사항 크롤링 및 저장
        List<Notice> notices = crawlingService.crawlPointNotices(url);
        noticeService.saveAllNotices(notices);
        
        log.info("비교과 공지사항 크롤링 완료: {}개 저장", notices.size());
    }

    /**
     * 비교과 공지사항 조회
     */
    public List<Notice> getPointNotices() {
        return noticeService.getNoticesByType(NoticeType.POINT);
    }
}
