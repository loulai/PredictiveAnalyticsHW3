import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import Jama.Matrix;
import net.sf.javaml.tools.data.FileHandler;


public abstract class DistanceFunction {
	
	public abstract double calculateDistance(Vector vectorA, Vector vectorB);
	
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
		/*
		// Debugging: Prints out the Cosine & Euclidian distances of two vectors
		ArrayList<Double> arrayList1 = new ArrayList<Double>(Arrays.asList(3.0, 2.0, 0.0, 5.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0));
		ArrayList<Double> arrayList2 = new ArrayList<Double>(Arrays.asList(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0));
		Vector array1 = new Vector(arrayList1);
		Vector array2 = new Vector(arrayList2);
		
		System.out.println("Cosine distance   : " + new DistanceCosine().calculateDistance(array1,array2));    // 0.69
		System.out.println("Euclidian distance: " + new DistanceEuclidian().calculateDistance(array1,array2)); // 6.16
		
		// Distance Matrix: outputs a distance matrix for given files
		// Generate input format as required. Just go with it.
		File inputFile = new File("./tfidfMatrixLong.csv");
		CSVToVectors testReader = new CSVToVectors(inputFile);
		Dataset dataset = new Dataset(testReader);
		
		System.out.println("==== Cosine Distance Matrix ====");
		new DistanceCosine().distanceMatrix(dataset);
		
		System.out.println("\n==== Euclidian Distance Matrix ====");
		new DistanceEuclidian().distanceMatrix(dataset);
		*/
	}
	
	// Generates n-by-n distance matrix of either cosine or euclidian distancance 
	public Dataset distanceMatrix(Dataset dataset) {
		ArrayList<Vector> data = dataset.vectors;
		double[][] row = new double[data.size()][];
		for(int i = 0; i<data.size(); i++) {
			Vector currentArticle = data.get(i);
		
			double[] column = new double[data.size()];
			for(int k = 0; k < data.size(); k++) {
				double distance;
				distance = calculateDistance(currentArticle, data.get(k));
				column[k] = distance;
			}
			row[i] = column;
		}
		Matrix distanceMatrix = new Matrix(row);
		Dataset finalDataset = new Dataset(distanceMatrix);
		finalDataset.printMatrix();
		return finalDataset;
	}
}

