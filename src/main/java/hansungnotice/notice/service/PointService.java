package hansungnotice.notice.service;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import hansungnotice.notice.noticerepository.NoticeRepository;
import jakarta.transaction.Transactional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PointService {
    private final NoticeRepository noticeRepository;
    public PointService(NoticeRepository noticeRepository){
        this.noticeRepository=noticeRepository;
    }

    @Transactional
    public void pointFetchAndSaveNotices(String url, NoticeType noticeType){
        try{
            Document doc = Jsoup.connect(url).get();


            Elements noticeValues = doc.select("div[data-module='eco'][data-role='item'].OPEN a");

            // 상위 5개만 출력
            int count = 0;
            for (Element value : noticeValues) {



                Element titleElement = value.selectFirst(".title_wrap b");
                Element dateElement = value.selectFirst("small time");
                Element urlElement = value.selectFirst("a");
                if (titleElement == null || dateElement == null) {
                    System.out.println("공지 항목을 찾을 수 없습니다.");
                    continue;
                }

                if (count >= 5) break;

                String noticeTitle = titleElement.text(); // 공지 제목
                String noticeDateStr = dateElement.text().replaceAll("\\(.*\\)", "").split(" ")[0]; // 공지 날짜 (String)
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
    public List<Notice> findPoint() {
        return noticeRepository.findAllByOrderByIdDesc();
    }
}
