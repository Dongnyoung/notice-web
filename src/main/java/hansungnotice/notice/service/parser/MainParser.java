package hansungnotice.notice.service.parser;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import lombok.RequiredArgsConstructor;

import org.jsoup.nodes.Element;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class MainParser implements NoticeParser {
    private final NoticeType noticeType; // STUDENT or SCHOOL
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Override
    public String selector() {
        return "form[name=viewForm] .board-table.horizon1 tbody tr";
    }

    @Override
    public Notice parse(Element element) {
        // 상단 고정공지 스킵
        if (element.className() != null && element.className().contains("notice")) return null;

        Element titleEl = element.selectFirst("td.td-subject");
        Element dateEl  = element.selectFirst("td.td-date");
        Element urlEl   = element.selectFirst("a");

        if (titleEl == null || dateEl == null || urlEl == null) return null;

        String title = titleEl.text().trim();
        LocalDate date = LocalDate.parse(dateEl.text().trim(), FMT);

        String href = urlEl.attr("href");
        if (!href.startsWith("http")) {
            href = "http://www.hansung.ac.kr" + href; // 상대경로 보정
        }

        return Notice.builder()
                .title(title)
                .postedDate(date)
                .url(href)
                .type(noticeType)
                .build();
    }
}
