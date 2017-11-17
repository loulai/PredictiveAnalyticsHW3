import java.util.ArrayList;
import java.util.Arrays;

public abstract class DistanceFunction {
	
	public abstract double calculateDistance(ArrayList<Double> vectorA, ArrayList<Double> vectorB);
	
	public  abstract String getName();
	
	public static DistanceFunction getDistanceFunctionByName(String name){
		if(DistanceCosine.NAME.equals(name)) {
			return new DistanceCosine();
		}else if(DistanceEuclidian.NAME.equals(name)) {
			return new DistanceEuclidian();
		} else {
			System.out.println("Distance functions can only be 'cosine' or 'euclidian'");
		}
		return null;
	}
	
	public static void main(String[] args) {
		/*// Debugging: Prints out the Cosine & Euclidian distances of two vectors
		ArrayList<Double> array1 = new ArrayList<Double>(Arrays.asList(3.0, 2.0, 0.0, 5.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0));
		ArrayList<Double> array2 = new ArrayList<Double>(Arrays.asList(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0));
		
		System.out.println("Cosine distance   : " + new DistanceCosine().calculateDistance(array1,array2));    // 0.69
		System.out.println("Euclidian distance: " + new DistanceEuclidian().calculateDistance(array1,array2)); // 6.16
		*/
	}
}

