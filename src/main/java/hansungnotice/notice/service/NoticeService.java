package hansungnotice.notice.service;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import hansungnotice.notice.noticerepository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final CrawlingService crawlingService;
    /**
     * 타입별 공지사항 조회
     */
    public List<Notice> getNoticesByType(NoticeType type) {
        return noticeRepository.findByTypeOrderByPostedDateDesc(type);
    }

    /**
     * 타입별 공지사항 조회 (페이징)
     */
    public Page<Notice> getNoticesByType(NoticeType type, Pageable pageable) {
        return noticeRepository.findByTypeOrderByPostedDateDesc(type, pageable);
    }

    /**
     * 최근 공지사항 조회
     */
    public List<Notice> getRecentNotices() {
        return noticeRepository.findAllByOrderByPostedDateDesc();
    }

    /**
     * 특정 날짜 이후의 공지사항 조회
     */
    public List<Notice> getNoticesAfterDate(LocalDate date) {
        return noticeRepository.findByPostedDateAfterOrderByPostedDateDesc(date);
    }

    /**
     * 제목으로 공지사항 검색
     */
    public List<Notice> searchNoticesByTitle(String keyword) {
        return noticeRepository.findByTitleContainingIgnoreCaseOrderByPostedDateDesc(keyword);
    }

    /**
     * 타입별 공지사항 개수 조회
     */
    public long getNoticeCountByType(NoticeType type) {
        return noticeRepository.countByType(type);
    }

    /**
     * 공지사항 저장
     */
    @Transactional
    public Notice saveNotice(Notice notice) {
        return noticeRepository.save(notice);
    }

    /**
     * 공지사항 일괄 저장
     */
    @Transactional
    public List<Notice> saveAllNotices(List<Notice> notices) {
        return noticeRepository.saveAll(notices);
    }

    /**
     * 모든 공지사항 삭제
     */
    @Transactional
    public void deleteAllNotices() {
        noticeRepository.deleteAll();
    }

    /**
     * 타입별 공지사항 삭제
     */
    @Transactional
    public void deleteNoticesByType(NoticeType type) {
        List<Notice> notices = noticeRepository.findByTypeOrderByPostedDateDesc(type);
        noticeRepository.deleteAll(notices);
    }
    /**
     * 단일 타입을 지정된 URL에서 크롤링 후, 중복을 제외하고 저장.
     * @return 저장된 개수
     */
    @Transactional
    public int fetchAndSave(String url, NoticeType type) {
        var crawled = switch (type) {
            case STUDENT -> crawlingService.crawlStudentNotices(url);
            case SCHOOL  -> crawlingService.crawlSchoolNotices(url);
            case POINT   -> crawlingService.crawlPointNotices(url);
        };

        int saved = 0;
        for (Notice n : crawled) {
            boolean exists = noticeRepository.existsByTitleAndPostedDateAndType(
                    n.getTitle(), n.getPostedDate(), n.getType()
            );
            if (!exists) {
                noticeRepository.save(n);
                saved++;
            }
        }
        log.info("fetchAndSave: type={}, url={}, saved={}", type, url, saved);
        return saved;
    }

    /**
     * 여러 타입을 한 번에 가져와 저장 (예: 스케줄러에서 호출)
     * @param sources 타입→URL 매핑
     * @return 타입별 저장결과
     */
    @Transactional
    public Map<NoticeType, Integer> fetchAndSaveAll(Map<NoticeType, String> sources) {
        Map<NoticeType, Integer> result = new EnumMap<>(NoticeType.class);
        for (var e : sources.entrySet()) {
            int cnt = fetchAndSave(e.getValue(), e.getKey());
            result.put(e.getKey(), cnt);
        }
        return result;
    }

    /**
     * (선택) 특정 타입을 '갈아끼우기' — 기존 타입 데이터 삭제 후 새로 저장
     * 리스트 최신성만 유지하고 싶을 때 유용.
     */
    @Transactional
    public int replaceTypeWithFresh(String url, NoticeType type) {
        deleteNoticesByType(type);
        return fetchAndSave(url, type);
    }

}
