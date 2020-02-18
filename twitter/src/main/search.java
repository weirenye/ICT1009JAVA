

import java.io.FileOutputStream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class search {
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
			int numOfTweets = 0, i;
			String radUnit = "km";
			
			try {
				ConfigurationBuilder cb = new ConfigurationBuilder();
		    	cb.setDebugEnabled(true)
		    	  .setOAuthConsumerKey("WcpAl4uduJkr36sBZLnBGKRwq")
		    	  .setOAuthConsumerSecret("TXUknKM6CcGo8tCuYXTBiLlpsG2p4ua6yEbDysmVJ33cn5zhID")
		    	  .setOAuthAccessToken("339646879-iRE1PqgOXBJ74aWtiOei02lmNO6IBEaX6VIeMUkP")
		    	  .setOAuthAccessTokenSecret("Zw4IBn4sblig06uj2x2oAedP6yIlIXBXVLvscQFk9fpY4");
		    	TwitterFactory tf = new TwitterFactory(cb.build());
				
				//Initialize twitter object from factory
				Twitter twitter = tf.getInstance();
				System.out.println("Fetching "+word+" Tweets...");
	
				//Define your query object. The parameter to the Query constructor is the word to search
				Query query = new Query("#"+word);
				
				//Define the result set size. This example will return 1,000 results
				query.setCount(100);
		        query.setLang("en");
				
				//Execute the search method in the twitter object. The results are contained in a QueryResult object that contains one object Status per Tweet
				QueryResult result = twitter.search(query);
		        
				String fileName = "./data_"+word+".csv";
				
				FileOutputStream fos = new FileOutputStream(fileName);
				fos.write(239);
			    fos.write(187);
			    fos.write(191);
                OutputStreamWriter osw = new OutputStreamWriter(fos, 
                        StandardCharsets.UTF_8);
                CSVWriter writer = new CSVWriter(osw);
                String[] heading = {"Date", "Tweet", "Location", "Retweets", "Link to tweet"};
                writer.writeNext(heading);
				//Add resulting entries to the List that we will return
                
                for(i=0;i<3;i++)//there is more pages to load
                {
	            	for(Status status: result.getTweets()){
	            		DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
	            	    DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
	            	    inputFormat.setLenient(true);

	            	    Date date = status.getCreatedAt();
	            	    String outputDate = outputFormat.format(date);
	            		String url= "https://twitter.com/" + status.getUser().getScreenName() 
	            			    + "/status/" + status.getId();
						String[] line = new String[] {outputDate, cleanText(status.getText()), status.getUser().getLocation(), Integer.toString(status.getRetweetCount()),url};
	//    					System.out.println(cleanText(status.getText()));
					    writer.writeNext(line);

	            	}
	            	if (result.nextQuery() == null) {
	            		break;
	            	}
	            	else {
		                query = result.nextQuery();
		                result = twitter.search(query);
	            	}
                }
				
			} catch (TwitterException te) {
				//Print any error that may be associated with this code
				te.printStackTrace();
			}
			
			
			//Return List with all the tweets found as part of the search
			System.out.println(word+" Tweets Fetched");
			return tweets;
		}
	
}