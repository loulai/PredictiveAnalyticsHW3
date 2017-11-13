import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Scanner;

public class Preprocessing {

	public static void main(String[] args) throws FileNotFoundException {
		File testFile = new File("./data/C1/article01.txt");
		ArrayList<String> test = convertFileToArrayList(testFile);
		System.out.println(test);
	}
	
	public static ArrayList<String> convertFileToArrayList(File file) throws FileNotFoundException  { 
		// converts one file to an ArrayList
		// calls preprocessing method
		String finalString = "";
		String[] finalArray;
		ArrayList<String> finalArrayList;
		
		try { 
		Scanner sc = new Scanner(file);
		
		//read textfile one line at a time
		while(sc.hasNext()) {
			finalString += sc.next().trim() + " "; 
		}
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
		finalArrayList = preprocess(finalString);
		return finalArrayList;
	}
	
	public static ArrayList<String> preprocess(String textString) throws FileNotFoundException{
		ArrayList<String> textArray = new ArrayList<String>(Arrays.asList(textString.toLowerCase().split("[-!~,.():\"\\s]+"))); // lower case & punctuation 
		
		// read stopwords
		String stopwords = "";
		ArrayList<String> stopwordArray;
        File stopwordsFile = new File("./data/stopwords.txt");
        Scanner stopwordsInput = new Scanner(stopwordsFile);
        while(stopwordsInput.hasNext()) {
        	stopwords += stopwordsInput.next().trim() + " ";
        }
        stopwordArray = new ArrayList<String>(Arrays.asList(stopwords.split(" ")));
       
        // remove stopwords 
        textArray.removeAll(stopwordArray);
        
		return textArray;
	}
	
	public static ArrayList<String> toUnique(ArrayList<String> evaluatedArticles){
		ArrayList<String> uniqueArrayList;
		uniqueArrayList = new ArrayList<String>(new LinkedHashSet<String>(evaluatedArticles)); //unique tokens 
		return uniqueArrayList;
	}

}
