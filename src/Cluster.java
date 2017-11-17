import java.util.ArrayList;

public class Cluster {
	
	ArrayList<ArrayList<Double>> vectors; // a cluster is a collections of vectors of doubles
	ArrayList<Double> mean;  // the mean of the vectors in this cluster
	ArrayList<Double> sum;  // the sum of all vectors in this cluster
	
	public Cluster(int vectorsSize){
		// Initialize an ArrayList of vectors, which are ArrayLists of doubles
		// Number of vectors per cluster will depend on K and centroids, so initialize without initial capacity
		vectors = new ArrayList<ArrayList<Double>>();
		
		// Similarly, sum and mean are initially empty
		sum = new ArrayList<Double>(vectorsSize);
		mean = new ArrayList<Double>(vectorsSize);
	}
	
	// Returns how many vectors are in this cluster
	public int getSize() {
		return vectors.size();
	}
	
	public ArrayList<ArrayList<Double>> getVectors() {
		return vectors;
	}
	
	public void addVector(ArrayList<Double> vector) {
		// Add vector to cluster
		vectors.add(vector); 
	
		// Add vector to 'sum'
		if(sum.isEmpty()) {
			// If empty, sum is simply all the values in the vector. No summing needed.
			sum.addAll(vector);
		} else {
			// If not empty, sum current 'sum' with vector
			for(int i = 0; i < vector.size(); i++){
				sum.set(i, sum.get(i) + vector.get(i));
			}
		}
		
	}

	public void recomputeClusterMean() {
		for(int i = 0; i < sum.size(); i++){
			mean.set(i, sum.get(i) / vectors.size());
		}
	}

	public void removeVector(ArrayList<Double> vector) {
		vectors.remove(vector);
		for(int i = 0; i < vector.size(); i++){
			sum.set(i, sum.get(i) - vector.get(i));
		}
	}
	
	public boolean contains(ArrayList<Double> vector) {
		return vectors.contains(vector);
	}
}

/*quick test --
		ArrayList<Double> one = new ArrayList<Double>();
		one.add(1.0);
		one.add(2.0);
		one.add(3.0);
		
		ArrayList<Double> two = new ArrayList<Double>();
		two.add(1.0);
		two.add(2.0);
		two.add(3.0); 
		
		ArrayList<Double> three = new ArrayList<Double>();
		three.add(1.0);
		three.add(1.0);
		three.add(3.0);

		ArrayList<Double> joker = new ArrayList<Double>();
		joker.add(1.0);
		joker.add(2.0);
		joker.add(3.0);
		
		ArrayList<ArrayList<Double>> fakeCluster = new ArrayList<ArrayList<Double>>();
		fakeCluster.add(one);
		fakeCluster.add(two);
		fakeCluster.add(three);
		
		System.out.println("o: "+ fakeCluster.get(0));
		
		boolean tf = fakeCluster.contains(joker);
		
		System.out.println("tf: "+ tf);

*/