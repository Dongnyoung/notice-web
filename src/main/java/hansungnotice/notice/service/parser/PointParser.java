package hansungnotice.notice.service.parser;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class PointParser implements NoticeParser {

    private final NoticeType noticeType; // 보통 NoticeType.POINT 주입
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Override
    public String selector() {
        // 컨테이너(아이템) 기준 셀렉터: a 태그는 parse() 내부에서 다시 찾음
        // 필요시 ".program-list .program-item" 로 바꿔 테스트
        return "div[data-module='eco'][data-role='item'].OPEN";
    }

    @Override
    public Notice parse(Element item) {
        // a 태그 기준으로 세부 요소 추출
        Element a = item.selectFirst("a");
        if (a == null) return null;

        // 제목: 사이트별로 클래스가 다를 수 있어 fallback 두기
        Element titleEl = a.selectFirst(".title_wrap b");
        /*
        if (titleEl == null) {
            // 예비 후보 (실제 DOM을 보고 맞는 걸로 수정)
            titleEl = a.selectFirst(".program-title");
        }
        */
        if (titleEl == null) return null;

        // 날짜: 첫 번째 time을 "날짜"로 가정 (두 번째는 보통 "시간" → 13:00) 오류로 인해 수정함
        Element dateEl = a.select("time").first();
        if (dateEl == null) return null;

        String title = titleEl.text().trim();

        // 예: "2025.10.05(일) 09:00 ~ 13:00" 같은 케이스도 대비해서 괄호 및 뒤쪽 토큰 제거
        String rawDate = dateEl.text().trim();
        String dateStr = rawDate
                .replaceAll("\\(.*?\\)", "")   // 괄호 및 내부 제거
                .trim()
                .split("\\s+")[0];             // 맨 앞 토큰만(yyyy.MM.dd 기대)

        // yyyy.MM.dd 형태만 허용 (형식이 다르면 안전하게 skip)
        if (!dateStr.matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
            return null; // 필요시 로깅으로 바꿔도 됨
        }

        LocalDate date = LocalDate.parse(dateStr, FMT);

        String href = a.attr("href");
        if (!href.startsWith("http")) {
            // 상대경로 보정
            href = "http://hsportal.hansung.ac.kr" + href;
        }

        return Notice.builder()
                .title(title)
                .postedDate(date)      // Notice가 LocalDate를 쓰는 현재 구조에 맞춤
                .url(href)
                .type(noticeType)      // 보통 NoticeType.POINT
                .build();
    }
}
