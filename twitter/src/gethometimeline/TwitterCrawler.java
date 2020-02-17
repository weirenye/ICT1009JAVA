package gethometimeline;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterCrawler {
	//Consumer Key
//	static String consumerKeyStr = "5PRpCCmyVbf5EBQKpnY32DO2S";
//	static String consumerSecretStr = "4GJTp3LVFwSHy6KqTUR5zwGYIka94LBM9Vzx9T907DGLtLO7Jc";
//	
//	//Access Token
//	static String accessTokenStr = "OaSzPf5j2ZVmbN9gy4WG4KpbUbVOyaAgurfXUy6d";
//	static String accessTokenSecretStr = "6hOPTKH9SQ8WKKcL2dLHMBA6TDjANErFDzlYz1sqMQCLx";
	public static String cleanText(String text)
	{
		text = text.replace("\n", " ");
		text = text.replace("\t", " ");
		text = text.replace("…", "...");
		text = text.replace("RT", "");
		

		return text;
	}
	
	public List searchForWord(String word) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException{
			
			//List that will be used to store the resulting tweets
			List<String> tweets = new ArrayList();
			int numOfTweets = 0;
			
			
			try {
				ConfigurationBuilder cb = new ConfigurationBuilder();
		    	cb.setDebugEnabled(true)
		    	  .setOAuthConsumerKey("5PRpCCmyVbf5EBQKpnY32DO2S")
		    	  .setOAuthConsumerSecret("4GJTp3LVFwSHy6KqTUR5zwGYIka94LBM9Vzx9T907DGLtLO7Jc")
		    	  .setOAuthAccessToken("339646879-OaSzPf5j2ZVmbN9gy4WG4KpbUbVOyaAgurfXUy6d")
		    	  .setOAuthAccessTokenSecret("6hOPTKH9SQ8WKKcL2dLHMBA6TDjANErFDzlYz1sqMQCLx");
		    	TwitterFactory tf = new TwitterFactory(cb.build());
				
				//Initialize twitter object from factory
				Twitter twitter = tf.getInstance();
				System.out.println("Fetching "+word+" Tweets...");
	
//				//Set ConsumerKey and ConsumerSecret
//				twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
//				
//				//Create AccessToken object with the string variables we set previously
//				AccessToken accessToken = new AccessToken(accessTokenStr,
//						accessTokenSecretStr);
//	
//				//Set access token to twitter object
//				twitter.setOAuthAccessToken(accessToken);
	
				//Define your query object. The parameter to the Query constructor is the word to search
				Query query = new Query(word);
				
				//Define the result set size. This example will return 1,000 results
				query.setCount(100);
				
				//Execute the search method in the twitter object. The results are contained in a QueryResult object that contains one object Status per Tweet
				QueryResult result = twitter.search(query);
				
				String fileName = "C:/Users/Zhen Feng/Documents/1009/Assignment/latest.csv";
				
				FileOutputStream fos = new FileOutputStream(fileName);
				fos.write(239);
			    fos.write(187);
			    fos.write(191);
                OutputStreamWriter osw = new OutputStreamWriter(fos, 
                        StandardCharsets.UTF_8);
                CSVWriter writer = new CSVWriter(osw);
                String[] heading = {"User", "Tweet", "Link to tweet"};
                writer.writeNext(heading);
				//Add resulting entries to the List that we will return
                while (result.hasNext())//there is more pages to load
                {
                	if (numOfTweets == 1001) {
                		break;
                	}
                	for(Status status: result.getTweets()){
                		String url= "https://twitter.com/" + status.getUser().getScreenName() 
                			    + "/status/" + status.getId();
    					String[] line = new String[] {"@"+status.getUser().getName(),cleanText(status.getText()),url};
//    					System.out.println(cleanText(status.getText()));
    				    writer.writeNext(line);
    				    numOfTweets++;

    				}
	                query = result.nextQuery();
	                result = twitter.search(query);
                }
				
			} catch (TwitterException te) {
				//Print any error that may be associated with this code
				te.printStackTrace();
			}
			
			
			//Return List with all the tweets found as part of the search
			System.out.println(word+" Tweets Fetched");
			return tweets;
		}
	
    public static void main(String[] args) throws TwitterException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException{
    	//Instantiating current class to an object
		TwitterCrawler search = new TwitterCrawler();
		
		//Preview of the found tweets to main console
		
		search.searchForWord("climate");
//	    List<String> result = search.searchForWord("global warming");
        
    			
    
//    	ConfigurationBuilder cb = new ConfigurationBuilder();
//    	cb.setDebugEnabled(true)
//    	  .setOAuthConsumerKey("5PRpCCmyVbf5EBQKpnY32DO2S")
//    	  .setOAuthConsumerSecret("4GJTp3LVFwSHy6KqTUR5zwGYIka94LBM9Vzx9T907DGLtLO7Jc")
//    	  .setOAuthAccessToken("339646879-OaSzPf5j2ZVmbN9gy4WG4KpbUbVOyaAgurfXUy6d")
//    	  .setOAuthAccessTokenSecret("6hOPTKH9SQ8WKKcL2dLHMBA6TDjANErFDzlYz1sqMQCLx");
//    	TwitterFactory tf = new TwitterFactory(cb.build());
//    	Twitter twitter = tf.getInstance();
    	
//    	String csvFile = "C:/Users/Zhen Feng/Documents/1009/Assignment/output.csv";
//        FileWriter writer = new FileWriter(csvFile);
//        
//        CSVUtils.writeLine(writer, Arrays.asList("User", "Tweet"));
//        
//    	TwitterFactory.getSingleton();
//    	Query query = new Query("source:twitter4j yusukey");
//        QueryResult result = twitter.search(query);
////        List<Status> statuses = twitter.getHomeTimeline();
//        System.out.println("Showing home timeline.");
//        for (Status status : result.getTweets()) {
//        	List<String> list = new ArrayList<>();
//        	list.add("@"+status.getUser().getName());
//        	list.add(status.getText());
//        	
//        	CSVUtils.writeLine(writer, list);
////            System.out.println(status.getUser().getName() + ":" +
////                               status.getText());
//        	
//        }
//        writer.flush();
//        writer.close();
    }
}
