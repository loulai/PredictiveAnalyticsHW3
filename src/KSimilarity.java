import java.io.File;

public class KSimilarity {

	public static void main(String[] args) {
		File testArticle = new File("./data/C1/article01.txt");
		KSimilarity kSim = new KSimilarity(testArticle);
	}
	
	public double cosineSimilarity(double[] vectorA, double[] vectorB) {
		double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vectorA.length; i++) {
	        dotProduct += vectorA[i] * vectorB[i];
	        normA += Math.pow(vectorA[i], 2);
	        normB += Math.pow(vectorB[i], 2);
	    }   
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}
	
	public KSimilarity(File file) {
		fileToInt(file);
	}
	
	public static void fileToInt(File file) {
		String c = file.getParentFile().getName(); //C1
		String a = file.getName().replaceAll("[^0-9]",""); //10
		System.out.println(Integer.valueOf(a));
		
		int[] articlesPerFolder = {8, 8, 4, 8, 13, 5, 8, 10, 4, 18, 8 ,10, 7, 5, 6};
		int articleNum = 0;
		for(int i = 0; i < Integer.valueOf(a);i++) {
			articleNum = articleNum + articlesPerFolder[i];
		}
	}
}
