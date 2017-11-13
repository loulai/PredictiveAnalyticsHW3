import java.io.File;

public class KSimilarity {

	public static void main(String[] args) {
		File testArticle = new File("./data/C14/article6.txt");
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
	
	public int fileToInt(File file) {
		int c = Integer.valueOf(file.getParentFile().getName().replace("C", "")); //C1
		int a = Integer.valueOf(file.getName().replaceAll("[^0-9]","")); //10
		int position;
		int fileInt;
		
		int[] aggr = {0, 8, 16, 20, 28, 41, 46, 54, 64, 68, 86, 94 ,104, 111, 116, 122};

		position = aggr[c-1] + a;
		return position;
		//System.out.printf("C%d A%d\n", c, a);
		//System.out.println(position);
	}
}
