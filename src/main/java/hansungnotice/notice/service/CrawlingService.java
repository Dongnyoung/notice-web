package hansungnotice.notice.service;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import hansungnotice.notice.service.parser.MainParser;
import hansungnotice.notice.service.parser.NoticeParser;
import hansungnotice.notice.service.parser.PointParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlingService {

    private static final int MAX_NOTICES = 5;

    // 타입별 전략 맵
    private final Map<NoticeType, NoticeParser> parsers = Map.of(
            NoticeType.STUDENT, new MainParser(NoticeType.STUDENT),
            NoticeType.SCHOOL,  new MainParser(NoticeType.SCHOOL),
            NoticeType.POINT,   new PointParser(NoticeType.POINT)
    );

    public List<Notice> crawl(String url, NoticeType type) {
        NoticeParser parser = parsers.get(type);
        if (parser == null) throw new IllegalArgumentException("No parser for type: " + type);

        List<Notice> result = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();

            Elements rows = doc.select(parser.selector());
            int count = 0;
            for (Element row : rows) {
                if (count >= MAX_NOTICES) break;
                Notice n = parser.parse(row);
                if (n != null) {
                    result.add(n);
                    count++;
                }
            }
            log.info("크롤링 완료: {} {}개", type, result.size());
        } catch (IOException e) {
            log.error("크롤링 오류: {}", e.getMessage(), e);
        }
        return result;
    }

    // 필요하다면 기존의 명시적 메서드도 thin wrapper로 유지
    public List<Notice> crawlStudentNotices(String url) { return crawl(url, NoticeType.STUDENT); }
    public List<Notice> crawlSchoolNotices(String url)  { return crawl(url, NoticeType.SCHOOL); }
    public List<Notice> crawlPointNotices(String url)   { return crawl(url, NoticeType.POINT); }
}
