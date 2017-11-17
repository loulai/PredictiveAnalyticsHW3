import java.util.ArrayList;
import java.util.Arrays;


public class DistanceCosine extends DistanceFunction {
	
	static String NAME = "cosine";
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public double calculateDistance(ArrayList<Double> vectorA, ArrayList<Double> vectorB) {
		double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vectorA.size(); i++) {
	        dotProduct += vectorA.get(i) * vectorB.get(i);
	        normA += Math.pow(vectorA.get(i), 2);
	        normB += Math.pow(vectorB.get(i), 2);
	    }   
	    return (1 - dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
	}
	
	public static void main(String[] args) {
		
		/*
		ArrayList<Double> array1 = new ArrayList<Double>(Arrays.asList(3.0, 2.0, 0.0, 5.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0));
		ArrayList<Double> array2 = new ArrayList<Double>(Arrays.asList(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0));
		System.out.println(new DistanceCosine().calculateDistance(array1,array2));
		// 0.685
		
		ArrayList<Double> array3 = new ArrayList<Double>(Arrays.asList(0.0, 0.0));
		ArrayList<Double> array4 = new ArrayList<Double>(Arrays.asList(0.0, 0.0));
		System.out.println(new DistanceCosine().calculateDistance(array3,array4));
		*/
	}
	
	
	

}