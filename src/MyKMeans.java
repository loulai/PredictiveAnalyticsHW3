
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.FileHandler;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;

/** 
 * K-means algorithm steps: 
 * 0) Choose the number of clusters, k
 * 1) Generate k random points as centriods
 * 2) Assign each point to the nearest centriod
 * 3) Recompute the new centriods
 * 4) Repeat steps (2) and (3) until no there is no more point reassignment
 * 
 * @author Louise Y. Lai
 */

public class MyKMeans {
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		// Initialize the necessary input variables for KMeans()
		//File inputFile = new File("./tfidfMatrix.csv");
		File inputFile = new File("./tfidfMatrixLong.csv");
		int k = 3;
		DistanceFunction myDistFunct = DistanceFunction.getDistanceFunctionByName("cosine");
		
		// Create new KMeans object 
		MyKMeans myKMeans = new MyKMeans();
		
		// Run KMeans 
		List<Cluster> listOfClusters = myKMeans.runAlgorithm(inputFile, k, myDistFunct);
		
		// Print statistics
		myKMeans.printStatistics();
	}
	
	// The list of clusters generated
	List<Cluster> clusters = null;
	
	// A random number generator because K-Means is a randomized algorithm
	Random random = new Random(System.currentTimeMillis());
	
	// The distance function to be used for clustering
	DistanceFunction distf = null;
	
	// The names of the attributes
	List<String> attributeNames = null;
	
	// For evaluation
	long startTimestamp; // the start time of the latest execution
	long endTimestamp;   // the end time of the latest execution
	long iterationCount; // the number of iterations that was performed
	int numClusters;     // the number of clusters generated (equivalent to k)

	public MyKMeans() { 
		
	}
	
	/**
	 * ------------------ RunAlgorithm ------------------
	 * */
	public List<Cluster> runAlgorithm(File inputFile, int k, DistanceFunction distf) throws NumberFormatException, IOException {

		// record the start time
		startTimestamp =  System.currentTimeMillis();
		// reset the number of iterations
		iterationCount = 0;
		numClusters = k;
		
		this.distf = distf;
		
		// Structure to store the vectors from the file
		ArrayList<Vector> initialVectors;
		
		// Variables to store the minimum and maximum values in vectors
		double maxValue = 0;
		double minValue = 0;
	
		// Read the input file and generate vectors. See @CSVToVectors.java
		CSVToVectors reader = new CSVToVectors();
		initialVectors = reader.generateVectors(inputFile);
		//System.out.println(initialVectors.size());
		
		// Get size of vectors
		int vectorsSize = initialVectors.get(0).getSize(); //1499 for articles 1 to 14. 2020 for 1 to 20
		
		// attributeNames = reader.getAttributeNames();
		
		// Get min and max for each vector from the initial vectors
		for(Vector vector : initialVectors){
			minValue = Collections.min(vector.values);
			maxValue = Collections.max(vector.values);
		}
		
		clusters = applyKMeans(k, distf, initialVectors, minValue, maxValue, vectorsSize);
		
		// record end time
		endTimestamp =  System.currentTimeMillis();
		
		// return clusters
		return clusters;
	}

	/**
	 * ----------------- ApplyKMeans -----------------
	 */
	ArrayList<Cluster> applyKMeans(int k, DistanceFunction distf, ArrayList<Vector> vectors, double minValue, double maxValue, int vectorsSize) {
		
		// Initialize empty list of clusters
		ArrayList<Cluster> newClusters = new ArrayList<Cluster>();
		
		//int recomputingCount = 0;
		
		// (1) Randomly generate k empty clusters with a random cluster center
		for(int i = 0; i < k; i++){
		
			// Initialize new empty cluster 
			Cluster cluster = new Cluster(vectorsSize);
			
			// Assign random mean to that cluster
			cluster.mean = generateRandomVector(minValue, maxValue, vectorsSize); 
			
			newClusters.add(cluster);
		}
	
	
		boolean changed;
		while(true) {
			iterationCount++;
			changed = false;
			
			//  (2) Assign each point to the nearest centriod
			for (Vector vector : vectors) {
				Cluster nearestCluster = null;
				Cluster containingCluster = null;
				double distanceToNearestCluster = Double.MAX_VALUE;
				
				// for vector, consider each cluster and do the following:
				for (Cluster cluster : newClusters) {
					
					// calculate the distance of the centriod to the vector
					double distance = distf.calculateDistance(cluster.mean, vector);
					
					// if it is the smallest distance until now, record this cluster and the distance
					if (distance < distanceToNearestCluster) {
						nearestCluster = cluster;
						distanceToNearestCluster = distance;
					}
					
					// if the cluster contains the vector already, remember it
					if (cluster.contains(vector)) {
						containingCluster = cluster;
					}
				}

				// Update the vector to the nearest cluster
				if (containingCluster != nearestCluster) {
					if (containingCluster != null) {
						containingCluster.removeVector(vector);
					}
					nearestCluster.addVector(vector);
					changed = true;
				}
			}

			// Break if there was no cluster reassignment
			if(!changed){ 
				break;
			}
			
			// (3) Recompute new centriods
			for (Cluster cluster : newClusters) {
				cluster.recomputeClusterMean();
				//System.out.println("recomputing #" + recomputingCount);
				//recomputingCount++;
			}
			// (4) Repeat steps (2) and (3) until no there is no more point reassignment
		}
		return newClusters;
	}

	/**
	 * ----------------- RandomVector -----------------
	 */
	Vector generateRandomVector(double minValue, double maxValue, int vectorsSize) {
		Vector randomVector = new Vector(vectorsSize);
		
		// Generate a random double for each position
		for(int i = 0; i < vectorsSize; i++){
			Double randomDouble = minValue + (maxValue - minValue) * random.nextDouble() ;
			randomVector.setValue(i, randomDouble);
		}
		// Displays vaues of generated centroids
		// System.out.println(randomVector.values);
		return randomVector;
	}
	
	/**
	 * ----------------- PrintStats -----------------
	 */
	public void printStatistics() {
		int sum = 0;
		System.out.println("\n=========== MyKMeans.java Statistics ===========");
		System.out.println(" Total time       : " + (endTimestamp - startTimestamp) + " ms");
		System.out.println(" Total iterations : " + iterationCount);
		System.out.println("--------------------------------------------------");

		for(int i = 0; i < clusters.size(); i++) {
			System.out.printf("Cluster %d: (%d)\n", i+1, clusters.get(i).getSize());
			if(clusters.get(i) != null) {
				Cluster currentCluster = clusters.get(i);
				for(int k = 0; k < currentCluster.getSize(); k++) {
					System.out.printf("\t %s: %s\n", currentCluster.vectors.get(k).getArticleName(), currentCluster.vectors.get(k));
				}
			}
			System.out.println();;
			sum += clusters.get(i).getSize();
		}
		System.out.printf("Total articles: (%d)\n", sum);
		System.out.println("\n-------------- JavaML Comparison  --------------\n");
		this.javaMLComparison();
		System.out.println("==================================================\n");
	}
	
	/**
	 * ----------------- JavaML Comparison -----------------
	 * Steps to compare my K-means algorithm performance are:
	 * (1) Cluster articles using my own K-Means algorithm
	 * (2) Cluster articles using the K-Means algorithm by JavaML
	 * (3) Compare results using sum-of-squared-errors (SSE)
	 */
	public void javaMLComparison() {
		double myScore = 0;
		
		try {
		// Load dataset in correct input format for JavaML
		// Dataset data = FileHandler.loadDataset(new File("./iris.data"), 4, ","); // data >> [{[5.1, 3.5, 1.4, 0.2];Iris-setosa}, .. {}]
		Dataset data;
		//data = FileHandler.loadDataset(new File("./smallTransposedTFIDF.csv"), ",");
		data = FileHandler.loadDataset(new File("./transposedTFIDFLong.csv"), ",");
		
		// (1) MyKMeans
			// Set up input values
			File inputFile = new File("./tfidfMatrixLong.csv");
			DistanceFunction myDistFunct = DistanceFunction.getDistanceFunctionByName("cosine");
			
			// Run KMeans
			List<Cluster> listOfClusters = clusters;
			
			// Format MyKMeans clustering output for evaluation
			// (1) Initialize a new Dataset array the size of the number of clusters generated
			Dataset[] myClusters = new Dataset[listOfClusters.size()];
		
			// (2) Fill Dataset with clustered vectors
			for(int i = 0; i < numClusters; i++) { // -> Cluster
				// Store current cluster's vectors
				ArrayList<Vector> currentVectors = listOfClusters.get(i).vectors;
				
				// Count how many vectors in a cluster
				int numVectorsInCluster = currentVectors.size();
		
				// Dataset is a cluster; Dataset[] are clusters
				Dataset tempCluster = new DefaultDataset();
				
				for(int m = 0; m < numVectorsInCluster; m++) { // -> Vector
					// Store current loop's vector
					Vector currentVector = currentVectors.get(m);
				
					// Copy an ArrayList to Array
					double[] values = new double[currentVector.getSize()];
					for(int z = 0; z < currentVector.getSize(); z++) { // -> Numbers
						values[z] = currentVector.getValue(z);
					}
					tempCluster.add(new DenseInstance(values));// Instance = vector
				}
				myClusters[i] = tempCluster;
			}
		
		// (2) JavaML
			// Create a new JavaML KMeans object 
			Clusterer km = new KMeans(numClusters, (int) iterationCount);
			
			// Cluster the data. Returns an array of data sets, with each dataset representing a cluster
			System.out.println(">> JavaML clustering running");
			Dataset[] javaMLClusters = km.cluster(data);
			System.out.println("== JavaML clustering complete\n");
			
		// Evaluate JavaML and myKMeans clustering results based on Sum of Squared Errors (SSE)
		// Create two cluster quality measures
		ClusterEvaluation javaMLSSE = new SumOfSquaredErrors();
		ClusterEvaluation mySSE = new SumOfSquaredErrors();
		
		// MyKMeans SSE
		myScore  = mySSE.score(myClusters);
		System.out.printf("MyKMeans score: %.2f\n", myScore);
		
		// JavaML SSE
		double javaMLScore = javaMLSSE.score(javaMLClusters);
		System.out.printf("JavaML score  : %.2f\n", javaMLScore);
		
		System.out.printf("Difference    :  %.2f\n", myScore - javaMLScore);
		} catch (IOException e) {e.printStackTrace();}
	}
}


/**
 *  Notes:
 *  - An alternative to step (1) is to randomly generate the clusters THEN determine the cluster centers.
 *    However, this is harder to compute and unusual. An easier way is to generate K centroids, which is what
 *    this program does.
 */
