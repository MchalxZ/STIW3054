package my.uum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubIssueController1 {
    private static KafkaProducerConfig1 gitHubIssueService = null;
    private static String issuesJson;
    @Autowired
    public GitHubIssueController1(KafkaProducerConfig1 gitHubIssueService) {
        GitHubIssueController1.gitHubIssueService = gitHubIssueService;
    }

    @GetMapping("/github-issues")
    public ResponseEntity<String> getGitHubIssues() {
         issuesJson = gitHubIssueService.getIssues().toString();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(issuesJson);
    }

    @GetMapping("/github-issues/2")
    public static String k(){
        issuesJson = gitHubIssueService.getIssues().toString();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(issuesJson).toString();
    }




}

