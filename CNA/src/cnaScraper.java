import java.util.Scanner; 
import java.util.Date;
import java.text.SimpleDateFormat;  
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

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
				print("Page: "+ i);
				int numResults = Integer.parseInt(document.getElementsByClass("result-section__index-value").get(0).text());
				int counter = 0;
				// for every link in the article, get article details and insert into CSV
				for (Element link : links) {
					
					String linkHref = link.attr("href");
					String linkText = link.text();
					linkHref = "https://www.channelnewsasia.com"+linkHref;
					if(!getDate(linkHref).isEmpty()){
						String[] line = new String[] {linkText, getDate(linkHref), getSource(linkHref), linkHref, getArticle(linkHref)};
						writer.writeNext(line);
						print(linkHref);
						print(linkText);
					}
					counter++;
					if (counter >= numResults){break;}
				}
				
				//select the link that goes to the next result page
				nextPage = document.getElementsByClass("pagination__link is-next").first();
				nextLink = "https://www.channelnewsasia.com"+nextPage.attr("href");
				document = Jsoup.connect(nextLink).get();
				print(nextLink);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getDate(String linkHref){
		String DatePublished = "";
		String StoreDates[];
		Date ads;
		//12 Feb 2020 11:35PM
		SimpleDateFormat dp =new SimpleDateFormat("dd MMM yyyy hh:mmaa");
		SimpleDateFormat returnFormat =new SimpleDateFormat("dd/MM/yyy");
		try {
			Document articlelink = Jsoup.connect(linkHref).get();
			Elements d = articlelink.getElementsByClass("article__details-item");
			DatePublished = d.text();
			
			if (!DatePublished.isEmpty()) {
				if (DatePublished.contains(" (Updated: ")){
					print(DatePublished);
					StoreDates = DatePublished.split(" \\(Updated\\: ");
					DatePublished = StoreDates[1];
					DatePublished = DatePublished.replace(")", "");
				}
				ads = dp.parse(DatePublished);
				DatePublished = returnFormat.format(ads);
			}
			
		}catch (IOException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		
		return DatePublished;
	}
	
	public static String getSource(String linkHref) {
		String Source = "";
		try {
			Document articlelink = Jsoup.connect(linkHref).get();
			Elements d = articlelink.getElementsByClass("article__source");
			Source = d.text();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		Source = Source.replace("Source: ", "");
		return Source;
	}
	
	public static String getArticle(String linkHref) {
		String Article = "";
		try {
			Document articlelink = Jsoup.connect(linkHref).get();
			Elements results = articlelink.getElementsByClass("c-rte--article");
			Elements fresults = results.select("p");
			
			for (Element pp : fresults) {
				String para = pp.text();
				Article = Article +" "+ para;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Article;
	}
	
	public static void print(String string) {
		System.out.println(string);
	}
		
}