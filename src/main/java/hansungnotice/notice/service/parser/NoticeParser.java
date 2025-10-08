package hansungnotice.notice.service.parser;

import hansungnotice.notice.domain.Notice;
import org.jsoup.nodes.Element;



public interface NoticeParser {
    String selector(); //목록을 뽑을 셀렉터

    Notice parse(Element element); //요소에서 notice하나를 추출
}
