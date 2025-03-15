package hansungnotice.notice;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NoticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoticeApplication.class, args);

		try {
			// 인코딩된 URL을 그대로 사용
			String url = "https://www.hansung.ac.kr/hansung/8385/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGaGFuc3VuZyUyRjE0MyUyRmFydGNsTGlzdC5kbyUzRmJic0NsU2VxJTNEMjM2JTI2YmJzT3BlbldyZFNlcSUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNnNyY2hDb2x1bW4lM0RzaiUyNnNyY2hXcmQlM0QlMjY%3D";
			// 해당 페이지를 Jsoup을 사용하여 요청
			Document doc = Jsoup.connect(url).get();

			Elements noticeTitles = doc.select("form[name=viewForm] .board-table.horizon1 tbody tr td.td-subject");

			int count=0;
			for (Element title : noticeTitles) {
				String noticeTitle = title.text(); // 공지사항 제목

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
