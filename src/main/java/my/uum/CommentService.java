package my.uum;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    private final Map<String, Integer> commentCounts = new HashMap<>();

    public void addComment(String username) {
        commentCounts.put(username, commentCounts.getOrDefault(username, 0) + 1);
    }

    public List<Map.Entry<String, Integer>> getActiveCommenters() {
        List<Map.Entry<String, Integer>> activeCommenters = new ArrayList<>(commentCounts.entrySet());
        activeCommenters.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return activeCommenters;
    }
}
