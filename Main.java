import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        List<Tweet> tweets = Arrays.asList(
            new Tweet("Alice", "I like @Bert and @Cookie"),
            new Tweet("Ernie", "Watching @Bert"),
            new Tweet("Elmo", "Hello @Bert"),
            new Tweet("Cookie", "Hello @Bert and @Cookie")
        );
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        System.out.println("Inferred follow graph:");
        graph.forEach((author, mentioned) ->
            System.out.println(" " + author + " -> " + new TreeSet<>(mentioned)));
        System.out.println("Influencers (most followers first): " +
            SocialNetwork.influencers(graph));
    }
}
