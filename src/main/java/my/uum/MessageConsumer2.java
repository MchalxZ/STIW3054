package uum;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer2 {
    private String message;
    @KafkaListener(topics = "my-topic2", groupId = "my-group-id")
    public void listen(String message) {
        System.out.println("Received " + message);
    }
    public String retrieve(){
        return message;
    }
}
