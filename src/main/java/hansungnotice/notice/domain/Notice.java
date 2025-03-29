package hansungnotice.notice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@NoArgsConstructor
@Getter
@Entity
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDate postedDate; //게시일
    private String url;
    @Enumerated(EnumType.STRING)
    private NoticeType type; //공지유형 (학사공지,장학공지..)

    public NoticeType getType() {
        return type;
    }

    public Notice(String title, LocalDate postedDate, String url, NoticeType type) {
        this.title = title;
        this.postedDate = postedDate;
        this.type = type;
        this.url=url;
    }

}
