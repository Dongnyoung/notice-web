package hansungnotice.notice.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.noticerepository.NoticeRepository;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class StudentNotice {

    private final NoticeRepository noticeRepository;

    public StudentNotice(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }


    private final String url = "https://www.hansung.ac.kr/hansung/8385/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGaGFuc3VuZyUyRjE0MyUyRmFydGNsTGlzdC5kbyUzRmJic0NsU2VxJTNEMjM2JTI2YmJzT3BlbldyZFNlcSUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNnNyY2hDb2x1bW4lM0RzaiUyNnNyY2hXcmQlM0QlMjY%3D";
    @Transactional
     public void fetchAndSaveNotices(){
        try{
            Document doc = Jsoup.connect(url).get();

            // form[name=viewForm] 내에서 board-table horizon1 클래스를 찾고, tbody 안의 tr의 td.td-subject 선택
            Elements noticeValues = doc.select("form[name=viewForm] .board-table.horizon1 tbody tr");

            // 상위 5개만 출력
            int count = 0;
            for (Element value : noticeValues) {
                Element titleElement = value.selectFirst("td.td-subject");
                Element dateElement = value.selectFirst("td.td-date");

                if (titleElement == null || dateElement == null) {
                    System.out.println("공지 항목을 찾을 수 없습니다.");
                    continue;
                }

                if (count >= 5) break;

                String noticeTitle = titleElement.text(); // 공지 제목
                String noticeDateStr = dateElement.text(); // 공지 날짜 (String)

                // String → LocalDateTime 변환
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate = LocalDate.parse(noticeDateStr, formatter);
                //LocalDateTime noticeDate = localDate.atStartOfDay(); // 00:00:00 시간 추가

                //추가
                Notice notice = new Notice(noticeTitle,localDate);
                noticeRepository.save(notice);
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Notice> getRecentNotices() {
        return noticeRepository.findTop5ByOrderByIdDesc();
    }
}
