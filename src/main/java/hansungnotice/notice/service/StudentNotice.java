package hansungnotice.notice;

import java.io.IOException;
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
            Elements noticeTitles = doc.select("form[name=viewForm] .board-table.horizon1 tbody tr td.td-subject");

            // 상위 5개만 출력
            int count = 0;
            for (Element title : noticeTitles) {
                if (count >= 5) break; // 5개까지만 출력
                String noticeTitle = title.text(); // 공지사항 제목

                System.out.println("공지 제목: " + noticeTitle);
                count++; // 카운트 증가
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Notice> getRecentNotices() {
        return noticeRepository.findTop5ByOrderByIdDesc();
    }
}
