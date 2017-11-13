import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class KSimilarity {

	public static void main(String[] args) throws FileNotFoundException {
		File testArticle = new File("./data/C1/article01.txt");
		KSimilarity kSim = new KSimilarity(testArticle,2);
	}
	
	public double cosineSimilarity(ArrayList<Double> vectorA, ArrayList<Double> vectorB) {
		double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vectorA.size(); i++) {
	        dotProduct += vectorA.get(i) * vectorB.get(i);
	        normA += Math.pow(vectorA.get(i), 2);
	        normB += Math.pow(vectorB.get(i), 2);
	    }   
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}
	
	public KSimilarity(File file, int k) throws FileNotFoundException {
		String vectorKey = String.valueOf(filenameToInt(file)-1);
		int n = 4; //num articles to evaluate
		TFIDF myTFIDF = new TFIDF(n); //evaluate 2 articles
		myTFIDF.addTFIDF();
		myTFIDF.printTFIDF();
		
		ArrayList<Double> vecA = myTFIDF.columnsMap.get(vectorKey);
		
		//System.out.println(vecA);
		double[] cosineValues = new double[n];
		int a = 0;
		for(String key : myTFIDF.columnsMap.keySet()) {
			ArrayList<Double> vecX = myTFIDF.columnsMap.get(key);
			cosineValues[i] = cosineSimilarity(vecA, vecX);
			System.out.println(cosineValues[a]);
		}
		
		for(int i = 0; i <cosineValues[i]; i++) {
			
		}
	}
	
	public int filenameToInt(File file) {
		int c = Integer.valueOf(file.getParentFile().getName().replace("C", "")); //C1
		int a = Integer.valueOf(file.getName().replaceAll("[^0-9]","")); //10
		int position;
		
		int[] aggr = {0, 8, 16, 20, 28, 41, 46, 54, 64, 68, 86, 94 ,104, 111, 116, 122};

		position = aggr[c-1] + a;
		return position;
		//System.out.printf("C%d A%d\n", c, a);
		//System.out.println(position);
	}
}
