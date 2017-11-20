import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class takes the TFIDF CSV file as input and generates a 2D ArrayList as output.
 * The output is then fed into Dataset.java.
 * @see Dataset.java
 */
public class CSVToVectors {
	
	public static void main(String[] args) {
		
		try {
			File inputFile = new File("./tfidfMatrix.csv");
			CSVToVectors testReader = new CSVToVectors();
			ArrayList<ArrayList<Double>> testVectors = testReader.generateVectors(inputFile);
		} catch (IOException e) { e.printStackTrace();}
		
	}
	
	int numArticles; // Number of articles equals vectors 
	int dimensions;  // Dimensions equals the number of terms (e.g. 1499)
	ArrayList<ArrayList<Double>> vectors;

	public CSVToVectors(){
	}
	
	/**
	 * 	Generates vectors as 2D ArrayLists
	 */
	public CSVToVectors(File file){
		try { generateVectors(file); } catch (IOException e) {}
	}
	
	
	public ArrayList<ArrayList<Double>> generateVectors(File file) throws IOException {
		
		// **temp** 14, in testing should be all 122 articles
		int numArticles = 14;

		// Input type is a CSV
		File csvFile = file;
		
		// Initializing buffered reader
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		
		// Each vector is an arrayList<Double>
		// The return value is an arrayList of vectors, hence ArrayList<ArrayList<Double>>
		vectors = new ArrayList<ArrayList<Double>>(numArticles);
		
		// Each line is one row being read by bufferedReader
		String line; 
		
		// Read the header row, which has the article 'names'
		String[] headerLine = br.readLine().split(",");
		//System.out.println(headerLine.split(",")[1]);
		
		// Initial rowCounter begins at 1 (not 0) to omit the header row we just read
		int rowCounter = 1;
		
		while ((line = br.readLine()) != null) {
			String[] oneLine = line.split(",");
			
			int numColumns = oneLine.length; //15 (14 articles, 1 term column) ** temp **
			
			if(rowCounter == 1) {
				//initializing *14* vectors (ArrayLists<Double>)
				for(int i = 0; i < numArticles; i++) {
					System.out.println(headerLine[i+1]);
					List<Double> newVector = new ArrayList<Double>(Collections.nCopies(1499, 0.0));
					vectors.add((ArrayList<Double>) newVector);
				}
			}
			
			// Loop through each column, assign value
			// Begins at 1 to omit term column
			for (int c = 1; c < numArticles + 1; c++) { 
				Double number = Double.parseDouble(oneLine[c]);
				vectors.get(c-1).set(rowCounter-1, number);
			}
			
			// At the end, this number is the total number of rows in the tfidf matrix
			rowCounter++; 
	    }
		
		// Account for counting the header
		dimensions = rowCounter - 1;
		
		// For debugging: prints out articles as vectors
		/*
		for(int i = 0; i < numArticles; i++){ // End: should iterate 122 times for 122 articles
			// Get each vector
			ArrayList<Double> vector = vectors.get(i); 
			System.out.println("--------------------- Vector/Article " + (i+1) + " ---------------------");
			System.out.println("Vector size: " + vector.size());
			//System.out.println("[vector/article] row. TFIDF");
			for(int k = 0; k < vector.size(); k++) {
				System.out.printf("Vec #%2d  Row #%4d  TFIDF: %f\n", i+1, k+1, vector.get(k));
			}
		}*/
		return vectors;
	}
			
	}

 