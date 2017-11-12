import java.io.File;
import java.io.FileNotFoundException;
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
	
	public static ArrayList<String> convertFileToArrayList(File file) throws FileNotFoundException  { //calls preprocessing method
		String finalString = "";
		String[] finalArray;
		ArrayList<String> finalArrayList;
		Scanner targetInput = null;
		try { targetInput = new Scanner(file);} catch (FileNotFoundException e) {e.printStackTrace();}
		
		//read textfile one line at a time
		while(targetInput.hasNext()) {
			finalString += targetInput.next().trim() + " "; 
		}
		finalArrayList = preprocess(finalString);
		return finalArrayList;
	}
	
	public static ArrayList<String> preprocess(String textString) throws FileNotFoundException{
		ArrayList<String> finalArrayList = new ArrayList<String>();
		String[] textArray;
		
		textArray = textString.toLowerCase().split("[-!~,.():\"\\s]+"); // lower case & punctuation 
		
		// stopwords
		String stopwords = "";
		String[] stopwordArray;
        File stopwordsFile = new File("./data/stopwords.txt");
        Scanner stopwordsInput = new Scanner(stopwordsFile);
        while(stopwordsInput.hasNext()) {
        	stopwords += stopwordsInput.next().trim() + " ";
        }
        stopwordArray = stopwords.split(" ");
        
		for(int i = 0; i < textArray.length; i++) {
			boolean isStopword = false;
			for(int k = 0; k < stopwordArray.length; k++) { //check is stopword
				if((textArray[i] + " ").equals(stopwordArray[k] + " ")) {
		 			isStopword = true;
		 			break;
		 		}
		 	}
			if(!isStopword) { //keep non-stopwords 
				finalArrayList.add(textArray[i]);
			}
		 }
		
		return finalArrayList;
	}
	
	public static ArrayList<String> toUnique(ArrayList<String> arrayList){
		ArrayList<String> uniqueArrayList;
		uniqueArrayList = new ArrayList<String>(new LinkedHashSet<String>(arrayList)); //unique tokens 
		return uniqueArrayList;
	}

}
