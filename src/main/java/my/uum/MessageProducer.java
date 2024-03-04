package my.uum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

    @Component
    public class MessageProducer {

        @Autowired
        @Qualifier("KafkaTemplate1")
        private KafkaTemplate<String, String> kafkaTemplate1;

        @Autowired
        @Qualifier("KafkaTemplate2")
        private KafkaTemplate<String, String> kafkaTemplate2;

        public void sendMessage1(String topic, String message) {
            kafkaTemplate1.send(topic, message);
        }
        public void sendMessage2(String topic, String message) {
            kafkaTemplate2.send(topic, message);
        }
    }

