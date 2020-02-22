import java.util.Scanner; 
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.Random;

import java.text.SimpleDateFormat;  
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVWriter;


public class cnaScraper{
	public static Document document;
	
	public static void main(String args[]){
		Scanner userInput = new Scanner(System.in);
		print("running...");
		print("Enter keyword to search: ");
		String toSearch = userInput.nextLine();
		searchBy(toSearch);
		print("done");
	}
	
	public static void searchBy(String keyword) {
		String filename;
		filename = keyword;
		if (keyword.contains(" ")){
			keyword = keyword.replace(" ", "+");
			filename = filename.replace(" ", "_");
		}
		String searchlink = "https://www.channelnewsasia.com/action/news/8396414/search?query=" + keyword;
		getlinks(searchlink, filename);
	}
	
	public static void getlinks(String searchlink, String filename) {
		Elements links;
		Element nextPage;
		String nextLink;
		Elements er;
		String nr;
		int numResults;
		Random rand = new Random();
		
		try {
			
			document = Jsoup.connect(searchlink).get();
			//for when there is no search results for the term:
			er = document.getElementsByClass("result-section__error-headline");
			if (!er.isEmpty()) {
				print("No search results for "+filename);
				return;
			}
			
			//setup for writing data ito CSV file
			FileOutputStream fos = new FileOutputStream("CNA_Articles_on_"+filename+".csv");
			OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            CSVWriter writer = new CSVWriter(osw);
            String[] heading = {"Title", "Date", "Source", "Link", "Article"};
            writer.writeNext(heading);

            //will loop according to how many pages of results are there
			int totalpages = Integer.parseInt(document.getElementsByClass("pagination__index-value").get(1).text()); 
			
			for(int i = 0; i<totalpages; i++ ) {
				// get links from the current results page
				Elements results = document.getElementsByClass("teaser__heading").not("teaser__category-container");
				links = results.select("a[href]");
				if (!er.isEmpty()) {
					print("Cant find links on page "+i);
				}
				print("Page: "+ i);
				if (i == totalpages -1) {
					numResults = 10;
				}else {
					numResults = Integer.parseInt(document.getElementsByClass("result-section__index-value").first().text());
				}
				int counter = 0;
				// for every link in the article, get article details and insert into CSV
				for (Element link : links) {
					
					String linkHref = link.attr("href");
					String linkText = link.text();
					linkHref = "https://www.channelnewsasia.com"+linkHref;
					Document articlelink = Jsoup.connect(linkHref).get();
					if(!getDate(articlelink).isEmpty()){
						TimeUnit.SECONDS.sleep(rand.nextInt(5));
						String[] line = new String[] {linkText, getDate(articlelink), getSource(articlelink), linkHref, getArticle(articlelink)};
						writer.writeNext(line);
						print(linkText);
						print(linkHref);
					}
					counter++;
					if (counter >= numResults){break;}
				}
				
				//select the link that goes to the next result page
				nextPage = document.getElementsByClass("pagination__link is-next").first();
				//nextPage = document.getElementsByClass("pagination__link is-next").not("section__cta");
				nextLink = "https://www.channelnewsasia.com"+nextPage.attr("href");
				TimeUnit.SECONDS.sleep(rand.nextInt(20));
				document = Jsoup.connect(nextLink).get();
				print(nextLink);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String getDate(Document articlelink){
		String DatePublished = "";
		String StoreDates[];
		Date ads;
		//12 Feb 2020 11:35PM
		SimpleDateFormat dp =new SimpleDateFormat("dd MMM yyyy hh:mmaa");
		SimpleDateFormat returnFormat =new SimpleDateFormat("dd/MM/yyy");
		try {
			Elements d = articlelink.getElementsByClass("article__details-item");
			DatePublished = d.text();
			
			if (!DatePublished.isEmpty()) {
				if (DatePublished.contains(" (Updated: ")){
					StoreDates = DatePublished.split(" \\(Updated\\: ");
					DatePublished = StoreDates[1];
					DatePublished = DatePublished.replace(")", "");
				}
				ads = dp.parse(DatePublished);
				DatePublished = returnFormat.format(ads);
			}
			
		}catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		
		return DatePublished;
	}
	
	public static String getSource(Document articlelink) {
		String Source = "";
		try {
			Elements d = articlelink.getElementsByClass("article__source");
			Source = d.text();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		Source = Source.replace("Source: ", "");
		return Source;
	}
	
	public static String getArticle(Document articlelink) {
		String Article = "";
		try {
			Elements results = articlelink.getElementsByClass("c-rte--article");
			Elements fresults = results.select("p");
			
			for (Element pp : fresults) {
				String para = pp.text();
				Article = Article +" "+ para;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Article;
	}
	
	public static void print(String string) {
		System.out.println(string);
	}
		
}