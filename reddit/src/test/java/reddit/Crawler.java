package reddit;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;

import java.util.ArrayList;
import java.util.List;

import net.dean.jraw.RedditClient;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.SubredditSearchPaginator;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.models.SubredditSearchResult;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
public class Crawler {

	public static void main(String[] args) {
		UserAgent userAgent = new UserAgent("bot", "com.example.usefulbot", "v0.1", "mattbdean");
		Credentials credentials = Credentials.script("tempcrawler", "temptemp",
			    "LIwSbI8F6-1H5A", "dqglRyYhMd4S-bZA4mAnyQfl6_k");
		NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);

		// Authenticate and get a RedditClient instance
		RedditClient reddit = OAuthHelper.automatic(adapter, credentials);
		//Subreddit sr = reddit.subreddit("RocketLeague").about();
		List<SubredditSearchResult> mylists = reddit.searchSubredditsByName("Climate change");
		for (SubredditSearchResult s : mylists) {
			//Subreddit sr = reddit.subreddit(s.getName()).about();
			System.out.println(s.getName());

			DefaultPaginator<Submission> paginator = reddit.subreddit(s.getName()).posts().build();
			Listing<Submission> posts;
			while((posts = paginator.next()) != null) {
				for(Submission sub: posts) {
					System.out.println(sub.getSelfText());
				}
			}
			
		    
		}
	

	}

}
