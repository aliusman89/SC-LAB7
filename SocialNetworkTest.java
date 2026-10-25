import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

/** Ten partition-based tests for the two required methods. */
public class SocialNetworkTest {
    @Test public void emptyTweetsProduceEmptyGraph() {
        assertTrue(SocialNetwork.guessFollowsGraph(Collections.emptyList()).isEmpty());
    }

    @Test public void tweetsWithoutMentionsAddNoEntries() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet("Alice", "Hello world"),
            new Tweet("Bob", "No tags here"));
        assertTrue(SocialNetwork.guessFollowsGraph(tweets).isEmpty());
    }

    @Test public void singleMentionCreatesFollowEvidence() {
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(
            Collections.singletonList(new Tweet("Ernie", "Hi @Bert")));
        assertTrue(graph.get("Ernie").contains("Bert"));
    }

    @Test public void multipleMentionsAreCaptured() {
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(
            Collections.singletonList(new Tweet("Ernie", "Hi @Bert and @Cookie")));
        assertTrue(graph.get("Ernie").containsAll(Arrays.asList("Bert", "Cookie")));
    }

    @Test public void mentionsAcrossTweetsAreCombinedAndDeduplicated() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet("Ernie", "@Bert"),
            new Tweet("Ernie", "@Bert and @Elmo"));
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertEquals(new HashSet<>(Arrays.asList("Bert", "Elmo")), graph.get("Ernie"));
    }

    @Test public void emptyGraphHasNoInfluencers() {
        assertTrue(SocialNetwork.influencers(Collections.emptyMap()).isEmpty());
    }

    @Test public void userWithNoFollowersIsNotAnInfluencer() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("Alice", Collections.emptySet());
        assertTrue(SocialNetwork.influencers(graph).isEmpty());
    }

    @Test public void singleInfluencerIsReturned() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("Alice", Collections.singleton("Bert"));
        assertEquals(Collections.singletonList("Bert"), SocialNetwork.influencers(graph));
    }

    @Test public void influencersAreOrderedByFollowerCount() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("Alice", new HashSet<>(Arrays.asList("Bert", "Cookie")));
        graph.put("Ernie", new HashSet<>(Arrays.asList("Bert", "Cookie")));
        graph.put("Elmo", Collections.singleton("Bert"));
        List<String> result = SocialNetwork.influencers(graph);
        assertEquals("Bert", result.get(0));
        assertEquals(2, result.size());
        assertEquals("Cookie", result.get(1));
    }

    @Test public void tiedInfluencersMayAppearInEitherOrder() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("Alice", new HashSet<>(Arrays.asList("Bert", "Cookie")));
        List<String> result = SocialNetwork.influencers(graph);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("Bert", "Cookie")));
    }
}
