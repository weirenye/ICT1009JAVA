import java.util.Scanner; 
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class cnaScraper {
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
		Elements nextPage;
		String nextLink;
		Elements er;
		
		try {
			
			document = Jsoup.connect(searchlink).get();
			er = document.getElementsByClass("result-section__error-headline");
			if (!er.isEmpty()) {
				print("No search results for "+filename);
				return;
			}
			
			FileOutputStream fout = new FileOutputStream("CNA_Articles_on_"+filename+".csv");
			PrintStream csv =new PrintStream(fout);
			
			//delimiters set to "=";
			csv.println("Title=Date=Source=Link=Article");

			int totalpages = Integer.parseInt(document.getElementsByClass("pagination__index-value").get(1).text()); 
			for(int i = 0; i<totalpages; i++ ) {
				// get links from the results page
				Elements results = document.getElementsByClass("teaser__heading").not("teaser__category-container");
				links = results.select("a[href]");
				int counter = 0;
				for (Element link : links) {
					
					String linkHref = link.attr("href");
					String linkText = link.text();
					linkHref = "https://www.channelnewsasia.com"+linkHref;
					csv.println(linkText +"="+getDate(linkHref)+"="+getSource(linkHref)+"="+ linkHref+"=\""+ getArticle(linkHref)+ "\"");
					
					print(linkHref);
					print(linkText);
					
					getArticle(linkHref);
					counter++;
					if (counter >= 10){break;}
				}
				
				//select the link that goes to the next result page
				nextPage = document.getElementsByClass("pagination__link is-next").not("section__cta");
				nextLink = "https://www.channelnewsasia.com"+nextPage.attr("href");
				document = Jsoup.connect(nextLink).get();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getDate(String linkHref) {
		String Date = "";
		try {
			Document articlelink = Jsoup.connect(linkHref).get();
			Elements d = articlelink.getElementsByClass("article__details-item");
			Date = d.text();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return Date;
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