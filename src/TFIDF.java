import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TFIDF extends Preprocessing {
	
	public Map<String, ArrayList<Double>> columnsMap;
	public ArrayList<String> uniqueTerms;
	public int nRow;
	public int nCol;
	//public File[] mainCorpus; //what is this is this needed?
	public File[][] fileArray; //[c1][a1,a2,a3]  [c2][a1,a2]..
	public File[] evaluatedArticles; //all articles under consideration used TFIDF. Lack C information.
	public String[] articleNames;
	
	public static int getTF(String word, ArrayList<String> doc) throws FileNotFoundException {
		int termFreq = 0;
		for(int i = 0; i < doc.size(); i++) {
			if(word.equals(doc.get(i))) {
				termFreq++;
			}
		}
		return termFreq;
	}
	
	public static double getIDF(String word, File[] corpus) throws FileNotFoundException {
		int docsContainingWord = getNumDocsContaingWord(word, corpus);
		double idf = 0;
		if(docsContainingWord == 0) {
			System.out.printf("OOV: No documents contain word: %s\n", word);
			idf=-1;
		} else {
			idf = Math.log(corpus.length / docsContainingWord);
		}
		return idf;
	}
	
	public static int getNumDocsContaingWord(String word, File[] corpus) throws FileNotFoundException {
		int docsContainingWord = 0;

		for(int i = 0; i < corpus.length; i++) { //search every doc for the target word
			ArrayList<String> currentDoc = convertFileToArrayList(corpus[i]);
			for(int k=0; k < currentDoc.size(); k++) { 
				if(word.equals(currentDoc.get(k))) {
					docsContainingWord++;
					break; //found, stop searching
				}
			}
		}
		return docsContainingWord;
	}
	
	public static double getTFIDF(String word, File file, File[] corpus) throws FileNotFoundException {
		ArrayList<String> targetFile = convertFileToArrayList(file);
		int tf = getTF(word, targetFile);
		double idf = getIDF(word, corpus);
		double tfidf = tf * idf;
		return tfidf;
	}	
	
	private static ArrayList<String> mergeDocs(File[] corpus) throws FileNotFoundException{
		//append each word in each doc to grand arrayList
		ArrayList<String> grandArrayList = new ArrayList<String>();
		for(int i = 0; i < corpus.length; i++) {
			ArrayList<String> doc = convertFileToArrayList(corpus[i]); 
			for (int k =0; k < doc.size();k++) {
				grandArrayList.add(doc.get(k));
			}
		}
		return grandArrayList;
	}
	
	public TFIDF(int n) throws FileNotFoundException {
		File[][] fileArray = readFiles();
		
		File[] evaluatedArticles = new File[n]; //to evaluate only n articles
		
		int count = 0;
		for (int i = 0; i < fileArray.length && count < n; i++) { //outer: c
			for(int k = 0; k < fileArray[i].length && count < n; k++) {//inner: articles
				evaluatedArticles[count] = fileArray[i][k];
				count++;
			}
		}
		
		ArrayList<String> grandArrayList = mergeDocs(evaluatedArticles);
		ArrayList<String> terms = toUnique(grandArrayList);
		Map<String, ArrayList<Double>> columnsMap = new HashMap(); // each doc is a column
		
		for(int i=0;i < evaluatedArticles.length; i++) { //create doc1:[0, 0, 0, 0] for each doc
			ArrayList<Double> tfidf = new ArrayList<>(Collections.nCopies(terms.size(), 0.0)); //initialize all zeros
			columnsMap.put(String.valueOf(i), tfidf);
		}	
		
		//setting values
		uniqueTerms = terms;
		this.columnsMap = columnsMap;
		nRow = terms.size();
		nCol = evaluatedArticles.length;
		this.fileArray = fileArray; //all corpus
		this.evaluatedArticles = evaluatedArticles; //flieArray is a list of all articles. in the final evaluation, this should equal 122.
	}

	public void printTFIDF() {
		//column header
		System.out.printf("%-14s ", ""); //padding for row
		for(String key:columnsMap.keySet()) {
			System.out.printf("%-14s ", key);
		}
		System.out.println();
		
		//rows
		for(int i = 0; i < nRow; i++) {
			System.out.printf("%-14s ", uniqueTerms.get(i)); //current row (e.g. "the")
			for(String key:columnsMap.keySet()) { //iterates over all columns for that row
				System.out.printf("%-14f ", columnsMap.get(key).get(i)); 
			}
			System.out.println();
		}
	}
	
	public TFIDF addTFIDF() throws FileNotFoundException {
		for(int i = 0; i< nCol; i++) {
			for(int k=0; k< nRow; k++) {
				String currentWord = uniqueTerms.get(k);
				double tfidf = getTFIDF(currentWord, evaluatedArticles[i], evaluatedArticles);
				
				//update
				columnsMap.get(String.valueOf(i)).set(k,  tfidf);
			}
		}
		return this;
	}
	
	public void printToCSV() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter("tfidfNTN.csv");
		
		//column header
		for(String key:columnsMap.keySet()) {
			writer.print("," + key);
		}
		writer.println();
		
		//rows
		for(int i = 0; i < nRow; i++) {
			writer.print(uniqueTerms.get(i) + ","); //each term
		
			for(String key:columnsMap.keySet()) { //iterates over all columns for that row
				writer.print(columnsMap.get(key).get(i) + ","); //TFIDF score
			}
			writer.println();
		}
		writer.close();
	}
	
	public static File[][] readFiles(){
		//reading files
		File[] corpraNames = new File("./data").listFiles();
		File[][] fileArray = new File[15][];
		
		int count = 0;
		for(File file:corpraNames) {
			
			if(file.isDirectory()) {
				int numArticles = 0;
				
		//System.out.println(file);
		int cIndex = Integer.valueOf(file.getName().replaceAll("[^0-9]","")) - 1; // C1 -> 0
		//System.out.println(cIndex);
		
		for(File subfile:file.listFiles()) {
			numArticles++; //counting
		}
		
		//String[] articleNames = new String[15]; //2D array to flatten later
		File[] articles = new File[numArticles];
		
		//System.out.println("num articles " + numArticles);
		for(File subfile:file.listFiles()) {
			//System.out.print("\t" + subfile + "\n"); 
			int articleIndex = Integer.valueOf(subfile.getName().replaceAll("[^0-9]","")) - 1; // Article 2 -> 1
			//articleNames[articleIndex] = subfile.getName();
			articles[articleIndex] = subfile;
			count++;
				}
				fileArray[cIndex] = articles;
			}
		}
		
		/*proves fileArray is sorted
		for(int i = 0; i < fileArray.length; i++) {
			for(int k = 0; k < fileArray[i].length; k++) {
				System.out.println(i + " " + fileArray[i][k]);
			}
		}
		*/
		return fileArray;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		TFIDF myTFIDF = new TFIDF(1); //evaluate 5 articles
		System.out.println("hello");
		myTFIDF.addTFIDF();
		myTFIDF.printTFIDF();
		System.out.println("-------------");
		myTFIDF.printToCSV();
	}
	
	public static double getTFIDFVerbose(String word, File file, File[] corpus) throws FileNotFoundException { //same code but with print statements
		ArrayList<String> targetFile = convertFileToArrayList(file);
		int tf = getTF(word, targetFile);
		double idf = getIDF(word, corpus);
		double tfidf = tf * idf;
		int numDocsContainingWord = getNumDocsContaingWord(word, corpus);
		System.out.printf("Target Word: %s\n", word);
		System.out.printf(">>> TF: %d\n", tf);
		System.out.printf("corpusSize: %d\n", corpus.length);
		System.out.printf("numDocsContainingWord: %d\n", numDocsContainingWord);
		System.out.printf(">>> IDF: %f\n", idf);
		System.out.printf(">>> TFIDF: %f\n", tfidf);
		return tfidf;
	}
}

