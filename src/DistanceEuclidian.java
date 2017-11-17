import java.util.ArrayList;
import java.util.Arrays;


public class DistanceEuclidian extends DistanceFunction {
	
	static String NAME = "euclidian";
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public double calculateDistance(ArrayList<Double> vectorA, ArrayList<Double> vectorB) {
		double sum =0;	
		for(int i=0; i< vectorA.size(); i++){
			sum += Math.pow(vectorA.get(i) - vectorB.get(i), 2);
		}
		return Math.sqrt(sum);
	}
	
	public static void main(String[] args) {
		ArrayList<Double> array1 = new ArrayList<Double>(Arrays.asList(3.0, 2.0, 0.0, 5.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0));
		ArrayList<Double> array2 = new ArrayList<Double>(Arrays.asList(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0));
		System.out.println(new DistanceEuclidian().calculateDistance(array1,array2));
		// 6.16
		
		ArrayList<Double> array3 = new ArrayList<Double>(Arrays.asList(0.0, 0.0));
		ArrayList<Double> array4 = new ArrayList<Double>(Arrays.asList(0.0, 0.0));
		System.out.println(new DistanceEuclidian().calculateDistance(array3,array4));
		//0
	}
	
	
	

}