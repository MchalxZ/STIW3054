package my.uum;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static my.uum.BotState.AWAITING_CONFIRMATION;
import static my.uum.ResponseHandlers.*;

@RestController
public class WebhookController2 {
    private final ObjectMapper objectMapper;

    private static StringBuilder we = new StringBuilder();

    public WebhookController2(ObjectMapper objectMapper, CommentService commentService) {
        this.objectMapper = objectMapper;
        this.commentService = commentService;
    }
    public static String wee(){
        return we.toString();
    }

    private final CommentService commentService;

    @PostMapping
    public void addComment(@RequestParam String username) {
        commentService.addComment(username);
    }

    @GetMapping("/active-commenters")
    public List<Map.Entry<String, Integer>> getActiveCommenters() {
        return commentService.getActiveCommenters();
    }

    @Autowired
    private MessageProducer messageProducer;

    @PostMapping("/webhook2")
    public ResponseEntity<String> handleWebhook(@RequestBody String encodedPayload) {
        try {
            String jsonContent = extractJsonContent(encodedPayload);
            JsonNode jsonNode = objectMapper.readTree(jsonContent);
            String action = jsonNode.path("action").asText();
            if ("created".equals(action)) {
                JsonNode issueNode = jsonNode.path("issue");
                String issueTitle = issueNode.path("title").asText();
                String issueUrl = issueNode.path("html_url").asText();
                JsonNode commentsNode = jsonNode.path("comment");
                String commentBody = commentsNode.path("body").asText();
                String commentTimeUTC = commentsNode.path("created_at").asText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                LocalDateTime utcDateTime = LocalDateTime.parse(commentTimeUTC, formatter);
                ZonedDateTime utcZonedDateTime = utcDateTime.atZone(ZoneId.of("UTC"));
                ZonedDateTime malaysiaDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Kuala_Lumpur"));
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String commentTime = malaysiaDateTime.format(outputFormatter);
                JsonNode commenterNode = commentsNode.path("user");
                String commenterName = commenterNode.path("login").asText();
                we.append("Issue Title: ").append(issueTitle);
                we.append("\nIssue URL: ").append(issueUrl);
                we.append("\nComment Body: ").append(commentBody);
                we.append("\nComment Time (Malaysia): ").append(commentTime);
                we.append("\nCommenter: ").append(commenterName).append("\n");
                commentService.addComment(commenterName);
                String e = getActiveCommenters().toString();
                BotState w = chatStates.get(chatID);
                    messageProducer.sendMessage2("topic_demo2", WebhookController2.wee());
                if(w.equals(AWAITING_CONFIRMATION)){
                    Thread ww = new Thread(() -> {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(chatID);
                        sendMessage.setText(String.valueOf(we));
                        sender.execute(sendMessage);
                    });
                   ww.start();
                   Thread.sleep(4000);
                   Thread ww2 = new Thread(() -> {
                       SendMessage sendMessage = new SendMessage();
                       sendMessage.setChatId(chatID);
                       sendMessage.setText(String.valueOf(e));
                       sender.execute(sendMessage);
                   });
                   ww2.start();
                   Thread.sleep(3000);
                   ww.interrupt();
                   ww2.interrupt();
                }
            }
            return ResponseEntity.ok("Webhook received successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON payload");
        }
    }

    private String extractJsonContent(String encodedPayload) throws UnsupportedEncodingException {
        return URLDecoder.decode(encodedPayload.substring("payload=".length()), "UTF-8");
    }
}


