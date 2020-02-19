package classes;

import java.util.*;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

import java.awt.Color;
import java.awt.Dimension;
import java.io.*;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class wordCloud {
	public void generateWordCloud(String word) throws IOException {
		final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
		frequencyAnalyzer.setStopWords(loadStopWords());
		final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("./Dataset_txtFiles/wordcloud_"+word+".txt");
		final Dimension dimension = new Dimension(600, 300);
		final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
		wordCloud.setPadding(0);
		wordCloud.setBackground(new RectangleBackground(dimension));
		wordCloud.setColorPalette(new ColorPalette(Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE));
		wordCloud.setFontScalar(new LinearFontScalar(20, 80));
		wordCloud.build(wordFrequencies);
		wordCloud.writeToFile("./"+word+".png");
	}
	
	public static Collection<String> loadStopWords() throws IOException {
	    Collection<String> stopwords = Files.readAllLines(Paths.get("./Dataset_txtFiles/stopwords.txt"));
	    
	    return stopwords;
	}
}
