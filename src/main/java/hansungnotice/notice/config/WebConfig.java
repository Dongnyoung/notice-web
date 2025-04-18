package hansungnotice.notice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // 모든 경로에 대해 CORS 설정

                .allowedOrigins("chrome-extension://apcgkcekbhhjdddlmlfehfomdlnpdeek",
                        "chrome-extension://jnjiphlahogdjmgfclhnpkbbdljcoioj",
                        "chrome-extension://gdgbacdejnopofifjidncbbkpeefmhim",
                        "chrome-extension://akjfnodcnglonkkfiompeaobokjeemei",
                        "chrome-extension://mmhknbcagbaabienhmaanfgonhpdlebj",
                        "chrome-extension://hojnkejedejifjdooaadmcocnniapbfa",
                        "chrome-extension://alcegohhmcjalnhcnicehncabkajjlpb",
                        "chrome-extension://lgmdkodflhaamkbbfanfbimgkhpedbho",
                        "chrome-extension://bopieamkhdjmoaklfnmcomlgoimnagog",
                        "http://52.79.204.245:8080")  // 크롬 익스텐션 ID를 명시
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
                .allowedHeaders("*")  // 모든 헤더 허용
                .allowCredentials(true);  // 쿠키 전송 허용
    }
}
