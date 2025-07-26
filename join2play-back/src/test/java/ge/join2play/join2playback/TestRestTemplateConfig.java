package ge.join2play.join2playback;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestRestTemplateConfig {
    @Bean
    public TestRestTemplate testRestTemplate(RestTemplateBuilder builder) {
        return new TestRestTemplate(TestRestTemplate.HttpClientOption.ENABLE_COOKIES);
    }
}
