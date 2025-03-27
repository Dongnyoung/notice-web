package hansungnotice.notice.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import hansungnotice.notice.noticerepository.NoticeRepository;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service

public class SchoolService {

    private final NoticeRepository noticeRepository;
    public SchoolService(NoticeRepository noticeRepository){
        this.noticeRepository=noticeRepository;
    }

    @Transactional
    public void schoolFetchAndSaveNotices(String url, NoticeType noticeType){
        try{
            Document doc = Jsoup.connect(url).get();

            // form[name=viewForm] 내에서 board-table horizon1 클래스를 찾고, tbody 안의 tr의 td.td-subject 선택
            Elements noticeValues = doc.select("form[name=viewForm] .board-table.horizon1 tbody tr");
            //고정 공지가 아닌거부터 출력을 위해
            boolean foundNotice = false;
            // 상위 5개만 출력
            int count = 0;
            for (Element value : noticeValues) {
                if (value.className().contains("notice")) {
                    foundNotice = true;
                    continue;
                }
                if (!foundNotice) continue; // 아직 'notice' 클래스가 안 나왔으면 스킵

                Element titleElement = value.selectFirst("td.td-subject");
                Element dateElement = value.selectFirst("td.td-date");
                Element urlElement = value.selectFirst("a");
                if (titleElement == null || dateElement == null || urlElement==null) {
                    System.out.println("공지 항목을 찾을 수 없습니다.");
                    continue;
                }

                if (count >= 5) break;

                String noticeTitle = titleElement.text(); // 공지 제목
                String noticeDateStr = dateElement.text(); // 공지 날짜 (String)
                String noticeUrl = urlElement.attr("href");

                //절대주소 변환
                noticeUrl = "http://www.hansung.ac.kr"+noticeUrl;
                // String → LocalDateTime 변환
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate = LocalDate.parse(noticeDateStr, formatter);
                //LocalDateTime noticeDate = localDate.atStartOfDay(); // 00:00:00 시간 추가

                //추가
                Notice notice = new Notice(noticeTitle,localDate,noticeUrl, noticeType);
                noticeRepository.save(notice);
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Notice> findNotice() {
        return noticeRepository.findAllByOrderByIdDesc();
    }
}
