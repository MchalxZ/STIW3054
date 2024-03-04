package uum;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Configuration
public class KafkaProducerConfig1 {
   SharingFactory w = new SharingFactory();

    @Bean(name="KafkaTemplate1")
    public KafkaTemplate<String, String> kafkaTemplate1() {
        return new KafkaTemplate<>(w.producerFactory());
    }

    @Value("${github.api.url}")
    private String githubApiUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducerConfig1(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<String> getIssues() {
        String apiUrl = githubApiUrl + "/repos/MchalxZ/STIW3054/issues";
        GithubContent[] issues = restTemplate.getForObject(apiUrl, GithubContent[].class);
        List<String> titles = new ArrayList<>();
        if (issues != null) {
            for (GithubContent issue : issues) {
                titles.add(issue.getTitle());
            }
        }
        return titles;
    }

}
