import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Infers a simple social network from tweet mentions. */
public final class SocialNetwork {
    private SocialNetwork() { }

    /**
     * Builds a map from each author to the distinct users they mention.
     * Tweets without mentions do not add entries.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> graph = new HashMap<>();
        for (Tweet tweet : tweets) {
            Set<String> mentioned = tweet.getMentionedUsers();
            if (!mentioned.isEmpty()) {
                graph.computeIfAbsent(tweet.getAuthor(), key -> new java.util.HashSet<>())
                     .addAll(mentioned);
            }
        }
        return graph;
    }

    /**
     * Returns users with at least one inferred follower, in descending follower-count
     * order. Users tied on follower count may appear in either order.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCounts = new HashMap<>();
        for (Set<String> followedUsers : followsGraph.values()) {
            for (String followed : followedUsers) {
                followerCounts.merge(followed, 1, Integer::sum);
            }
        }
        List<String> result = new ArrayList<>(followerCounts.keySet());
        result.sort((a, b) ->
            Integer.compare(followerCounts.get(b), followerCounts.get(a)));
        return result;
    }
}
