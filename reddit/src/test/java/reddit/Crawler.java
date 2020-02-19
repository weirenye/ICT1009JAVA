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
		//creating csv file and headers
		FileWriter csvWriter = new FileWriter("reddit.csv");
		csvWriter.append("NoOfComments");
		csvWriter.append(",");
		csvWriter.append("URL");
		csvWriter.append(",");
		csvWriter.append("Date");
		csvWriter.append(",");
		csvWriter.append("Upvotes");
		csvWriter.append(",");
		csvWriter.append("Title");
		csvWriter.append(",");
		csvWriter.append("Text");
		csvWriter.append("\n");
		
	
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
		List<SubredditSearchResult> mylists = reddit.searchSubredditsByName(tempSearchQuery).subList(0, 10);
		
		for (SubredditSearchResult s : mylists) {
			//limit it to top 3 search results for each subreddit
			DefaultPaginator<Submission> paginator = reddit.subreddit(s.getName()).posts().sorting(SubredditSort.TOP).limit(3).build();
			
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

	}

}
