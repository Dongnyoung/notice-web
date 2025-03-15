package hansungnotice.notice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String url;
    private LocalDateTime postedDate; //게시일

    public Notice(Long id, String title, String url, LocalDateTime postedDate) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.postedDate = postedDate;
    }

}
