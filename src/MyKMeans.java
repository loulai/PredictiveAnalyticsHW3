
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.FileHandler;
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

		/*
		// Initialize the necessary input variables for KMeans()
		File inputFile = new File("./tfidfMatrix.csv");
		int k = 2;
		DistanceFunction myDistFunct = DistanceFunction.getDistanceFunctionByName("cosine");
		
		// Create new KMeans object 
		MyKMeans myKMeans = new MyKMeans();
		
		// Run KMeans 
		List<Cluster> listOfClusters = myKMeans.runAlgorithm(inputFile, k, myDistFunct);
		
		// Print statistics
		myKMeans.printStatistics();
		*/
	
		/////////// >>>>>>>>>> Comparing to JavaML

		// Load dataset in correct input format for JavaML
		// Dataset data = FileHandler.loadDataset(new File("./iris.data"), 4, ","); // data >> [{[5.1, 3.5, 1.4, 0.2];Iris-setosa}, .. {}]
		Dataset data = FileHandler.loadDataset(new File("./transposedTFIDF.csv"), ",");

		/*
		// JavaML
			// Create a new JavaML KMeans object. When no options specified defaults to generating 4 clusters. 
			Clusterer km = new KMeans();
			System.out.println(" >> after KMeans()");
			//System.out.println(km);
			System.out.println();
			
			// Cluster the data. Returns an array of data sets, with each dataset representing a cluster
			Dataset[] clusters = km.cluster(data);
			System.out.println(" >> after clustering");
			//System.out.println(clusters[0].instance(0));
			System.out.println();
		*/
		// MyKMeans
			// Create new MyKMeans object 
			MyKMeans myKMeans = new MyKMeans();
			
			// Run KMeans 
			File inputFile = new File("./tfidfMatrix.csv");
			int k = 4;
			DistanceFunction myDistFunct = DistanceFunction.getDistanceFunctionByName("cosine");
			List<Cluster> listOfClusters = myKMeans.runAlgorithm(inputFile, k, myDistFunct);
			
			// Initialize a new Dataset array the size of the number of clusters generated
			Dataset[] myClusters = new Dataset[listOfClusters.size()];
		
			for(int i = 0; i < k; i++) { // -> Cluster
				// Store current cluster's vectors
				ArrayList<ArrayList<Double>> currentVectors = listOfClusters.get(i).vectors;
				
				// Count how many vectors in a cluster
				int numVectorsInCluster = currentVectors.size();
		
				// Dataset = cluster
				Dataset tempCluster = new DefaultDataset();
				
				for(int m = 0; m < numVectorsInCluster; m++) { // -> Vector
					// Store current loop's vector
					ArrayList<Double> currentVector = currentVectors.get(m);
					
					System.out.printf("cluster: %d vec: %d\n", i+1, m+1);
				
					// Convert an ArrayList to an Array
					double[] values = new double[currentVector.size()];
					for(int z = 0; z < currentVector.size(); z++) { // -> Numbers
						values[z] = currentVector.get(z);
					}
					tempCluster.add(new DenseInstance(values));// Instance = vector
				}
				myClusters[i] = tempCluster;
			}
			System.out.println("Number of clusters: " + myClusters.length);
			System.out.println("Number of vectors in cluster one: " + myClusters[0].size());
			System.out.println("Dimensions in cluster 1 > vec 1: " + myClusters[0].instance(0).size());
					
		// Create a cluster quality measure 
		ClusterEvaluation sse = new SumOfSquaredErrors();
		System.out.println(" >> after SSE");
		//System.out.println(sse);
		System.out.println();
		
		/*
		// Measure cluster quality 
		double score = sse.score(clusters);
		System.out.println(" >> after scoring");
		System.out.println("score  " + score);
		System.out.println();
		*/
		
		/*
		////-------------------------
		//Dataset data2 = FileHandler.loadDataset(new File("./iris.data"), 4, ",");
		File irisInput = new File("./iristrans.csv");
		
		System.out.println(irisInput);
		MyKMeans km2 = new MyKMeans();
		List<Cluster> clustersAsArrayList = km2.runAlgorithm(irisInput, 4, myDistFunct);
		int[] numVecs = new int[4];
		int i = 0;
		for(Cluster cluster:clustersAsArrayList) {
			numVecs[i] = cluster.getSize();
			System.out.println(numVecs[i]);
			i++;
			System.out.println("what");
		}
		//System.out.println(clustersAsArrayList.get(0).getVectors());
	*/
		/*
		Dataset[] cluster2 = 
		ClusterEvaluation sse2 = new SumOfSquaredErrors();
		double score2 = sse2.score(clusters2);
		*/
		
		/*// Test: reveals that one vector will always be left unclustered. Todo: figure out why and fix
		int[] numVecs = new int[k];
		int i = 0;
		for(Cluster cluster:listOfClusters) {
			numVecs[i] = cluster.getSize();
			System.out.println(numVecs[i]);
			i++;
			System.out.println("what");
		}*/
		
	}
	
	// The list of clusters generated
	List<Cluster> clusters = null;
	
	// A random number generator because K-Means is a randomized algorithm
	Random random = new Random(System.currentTimeMillis());
	
	// The distance function to be used for clustering
	DistanceFunction distf = null;
	
	// The names of the attributes
	List<String> attributeNames = null;
	
	// For statistics
	long startTimestamp; // the start time of the latest execution
	long endTimestamp;   // the end time of the latest execution
	long iterationCount; // the number of iterations that was performed

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
		
		this.distf = distf;
		
		// Structure to store the vectors from the file
		ArrayList<ArrayList<Double>> initialVectors;
		
		// Variables to store the minimum and maximum values in vectors
		double maxValue = 0;
		double minValue = 0;
	
		// Read the input file and generate vectors. See @CSVToVectors.java
		CSVToVectors reader = new CSVToVectors();
		initialVectors = reader.generateVectors(inputFile);
		//System.out.println(initialVectors.size());
		
		// Get size of vectors
		int vectorsSize = initialVectors.get(0).size(); //1499 for articles 1 to 14
		
		// attributeNames = reader.getAttributeNames();
		
		// Get min and max for each vector from the initial vectors
		for(ArrayList<Double> vector : initialVectors){
			minValue = Collections.min(vector);
			maxValue = Collections.max(vector);
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
	ArrayList<Cluster> applyKMeans(int k, DistanceFunction distf, ArrayList<ArrayList<Double>> vectors, double minValue, double maxValue, int vectorsSize) {
		
		// Initialize empty list of clusters
		ArrayList<Cluster> newClusters = new ArrayList<Cluster>();
		
		int recomputingCount = 0;
		
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

			// / for each vector
			for (ArrayList<Double> vector : vectors) {
				// find the nearest cluster and the cluster containing the item
				Cluster nearestCluster = null;
				Cluster containingCluster = null;
				double distanceToNearestCluster = Double.MAX_VALUE;
				
				// for each cluster
				for (Cluster cluster : newClusters) {
					
					// calculate the distance of the cluster mean to the vector
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

				// if the nearest cluster is not the cluster containing the vector
				if (containingCluster != nearestCluster) {
					// remove the vector from the containing cluster
					if (containingCluster != null) {
						containingCluster.removeVector(vector);
					}
					// add the vector to the nearest cluster
					nearestCluster.addVector(vector);
					changed = true;
				}
			}

			if(!changed){ // exit condition for main loop
				break;
			}
			
			// (3) Recompute new centriods
			for (Cluster cluster : newClusters) {
				cluster.recomputeClusterMean();
				System.out.println("recomputing #" + recomputingCount);
				recomputingCount++;
			}
			// (4) Repeat steps (2) and (3) until no there is no more point reassignment
		}
		return newClusters;
	}

	/**
	 * ----------------- RandomVector -----------------
	 */
	ArrayList<Double> generateRandomVector(double minValue, double maxValue, int vectorsSize) {
		ArrayList<Double> randomVector = new ArrayList<Double>(vectorsSize);
		
		// Generate a random double for each position
		for(int i = 0; i < vectorsSize; i++){
			Double randomDouble = minValue + (maxValue - minValue) * random.nextDouble() ;
			randomVector.add(randomDouble);
		}
		return randomVector;
	}
	
	/**
	 * ----------------- PrintStats -----------------
	 */
	public void printStatistics() {
		System.out.println("=========== KMEANS - STATISTICSS ===========");
		System.out.println(" Total time     : " + (endTimestamp - startTimestamp) + " ms");
		System.out.println(" Iteration count: " + iterationCount);
		System.out.println("============================================\n");
	}
}

/**
 *  Notes:
 *  - An alternative to step (1) is to randomly generate the clusters THEN determine the cluster centers.
 *    However, this is harder to compute and unusual. An easier way is to generate K centroids, which is what
 *    this program does.
 */
