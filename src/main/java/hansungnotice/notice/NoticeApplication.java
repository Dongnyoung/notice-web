package hansungnotice.notice;

import hansungnotice.notice.domain.Notice;
import hansungnotice.notice.domain.NoticeType;
import hansungnotice.notice.service.SchoolService;
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

		// SchoolService 서비스 빈 가져오기
		SchoolService schoolService =context.getBean(SchoolService.class);

		// 크롤링하여 DB에 저장
		String stuUrl = "https://www.hansung.ac.kr/hansung/8385/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGaGFuc3VuZyUyRjE0MyUyRmFydGNsTGlzdC5kbyUzRmJic0NsU2VxJTNEMjM2JTI2YmJzT3BlbldyZFNlcSUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNnNyY2hDb2x1bW4lM0RzaiUyNnNyY2hXcmQlM0QlMjY%3D";
		String schUrl = "https://www.hansung.ac.kr/hansung/8385/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGaGFuc3VuZyUyRjE0MyUyRmFydGNsTGlzdC5kbyUzRmJic0NsU2VxJTNEMTY2JTI2YmJzT3BlbldyZFNlcSUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNnNyY2hDb2x1bW4lM0RzaiUyNnNyY2hXcmQlM0QlMjY%3D";

		studentNotice.fetchAndSaveNotices(stuUrl,NoticeType.STUDENT);
		schoolService.schoolFetchAndSaveNotices(schUrl,NoticeType.SCHOOL);
		// 최신 공지사항 가져오기
		List<Notice> notices1 = studentNotice.getRecentNotices();
		List<Notice> notices2 = schoolService.findNotice();

		// 공지사항 출력
		System.out.println("📢 한성대 학사 공지사항");
		System.out.println("==============================");

		for (Notice  notice : notices1) {
				if(notice.getType().equals(NoticeType.STUDENT)) {
					System.out.println("공지 제목: " + notice.getTitle());
					System.out.println("게시일: " + notice.getPostedDate());
					System.out.println("----------------------------");
				}
		}
		System.out.println("📢 한성대 학교 공지사항");
		System.out.println("==============================");
		for (Notice  notice : notices2) {
			if(notice.getType().equals(NoticeType.SCHOOL)) {
				System.out.println("공지 제목: " + notice.getTitle());
				System.out.println("게시일: " + notice.getPostedDate());
				System.out.println("----------------------------");
			}
		}
	}
}
