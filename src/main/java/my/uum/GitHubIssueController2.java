package my.uum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubIssueController2 {

    private static KafkaProducerConfig2 gitHubIssueService = null;
    private static String issuesJson;
    @Autowired
    public GitHubIssueController2(KafkaProducerConfig2 gitHubIssueService) {
        GitHubIssueController2.gitHubIssueService = gitHubIssueService;
    }

    @GetMapping("/github-issues/3")
    public ResponseEntity<String> getGitHubIssues() {
         issuesJson = gitHubIssueService.getIssues().toString();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(issuesJson);
    }

    @GetMapping("/github-issues/4")
    public static String k(){
        issuesJson = gitHubIssueService.getIssues().toString();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(issuesJson).toString();
    }




}

