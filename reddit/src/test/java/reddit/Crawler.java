package reddit;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.dean.jraw.RedditClient;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.SubredditSearchPaginator;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Sorting;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.models.SubredditSearchResult;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
public class Crawler {

	public static void main(String[] args) throws IOException {
		//creating reddit.csv file
		FileWriter csvWriter = new FileWriter("reddit.csv");
		//adding headers to files
		csvWriter.append("NoOfComments,URL,Date,Upvotes,Title,Text\n");
		
		//used to log into reddit
		UserAgent userAgent = new UserAgent("bot", "com.example.usefulbot", "v0.1", "mattbdean");
		Credentials credentials = Credentials.script("tempcrawler", "temptemp", "LIwSbI8F6-1H5A", "dqglRyYhMd4S-bZA4mAnyQfl6_k");
		NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
		
		//temporary search
		String tempSearchQuery;
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Search Query: ");
		tempSearchQuery = sc.nextLine();
		sc.close();
		System.out.println("Search Query: " + tempSearchQuery);
		
		
		// Authenticate and get a RedditClient instance
		RedditClient reddit = OAuthHelper.automatic(adapter, credentials);
		
		//search for tempSearchQuery input from above in Subreddit and limit to 10 Subreddits
		
		int lastIndx = reddit.searchSubredditsByName(tempSearchQuery).size();
		int currIndx = 20;
		if (lastIndx < 20 )
		{
			currIndx = lastIndx;
		}
		
		List<SubredditSearchResult> mylists = reddit.searchSubredditsByName(tempSearchQuery).subList(0, currIndx);
		
		
		
		for (SubredditSearchResult s : mylists) {
			//limit it to top 3 search results for each subreddit
			DefaultPaginator<Submission> paginator = reddit.subreddit(s.getName()).posts().sorting(SubredditSort.HOT).limit(6).build();
			
			//writing into csv file
			Listing<Submission> posts;
				for(Submission sub: posts = paginator.next()) {
					String TextRemoveChar, TitleRemoveChar;
					TextRemoveChar = sub.getSelfText();
					TitleRemoveChar = sub.getTitle();
					TextRemoveChar = TextRemoveChar.replace("\n", "").replace(",", "_");
					TitleRemoveChar = TitleRemoveChar.replace("\n", "").replace(",", "_");
					String mys = sub.getCommentCount() + "," + "https://www.reddit.com" + sub.getPermalink() + "," + sub.getCreated() + "," + sub.getScore() + "," + TitleRemoveChar + "," + TextRemoveChar; 
					csvWriter.append(mys);
					csvWriter.append("\n");
				}
		}
		csvWriter.flush();
		csvWriter.close();
		System.out.println("CSV File closed");
	}

}
