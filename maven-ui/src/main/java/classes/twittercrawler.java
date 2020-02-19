package classes;
import java.io.IOException;


import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import twitter4j.TwitterException;

public class twittercrawler {

	public static void main(String[] args) throws TwitterException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException{
    	//Instantiating current class to an object
		search search = new search();
		wordCloud wc = new wordCloud();
		
		//Preview of the found tweets to main console
		
		search.searchForWord("climate change");
		search.searchForWord("global warming"); 
		search.searchForWord("greenhouse gas"); 
		search.searchForWord("fossil fuels"); 
		search.searchForWord("emissions"); 
		search.searchForWord("sea level rise");
		wc.generateWordCloud("climate change");
		wc.generateWordCloud("global warming");
		wc.generateWordCloud("greenhouse gas");
		wc.generateWordCloud("fossil fuels");
		wc.generateWordCloud("emissions");
		wc.generateWordCloud("sea level rise");
		// TODO Auto-generated method stub
	}

}
