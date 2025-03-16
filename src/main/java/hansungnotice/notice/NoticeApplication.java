package hansungnotice.notice;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.service.StudentNotice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class NoticeApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(NoticeApplication.class, args);

		// StudentNotice 서비스 빈 가져오기
		StudentNotice studentNotice = context.getBean(StudentNotice.class);

		// 크롤링하여 DB에 저장
		studentNotice.fetchAndSaveNotices();

		// 최신 공지사항 가져오기
		List<Notice> notices = studentNotice.getRecentNotices();

		// 공지사항 출력
		System.out.println("📢 한성대 최신 공지사항");
		System.out.println("==============================");
		for (Notice notice : notices) {
			System.out.println("공지 제목: " + notice.getTitle());
			System.out.println("게시일: " + notice.getPostedDate());
			System.out.println("----------------------------");
		}
	}
}
