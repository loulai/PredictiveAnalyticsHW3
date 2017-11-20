import java.util.ArrayList;

public class Cluster {
	
	ArrayList<Vector> vectors; // a cluster is a collections of vectors of doubles
	Vector mean;  // the mean of the vectors in this cluster
	Vector sum;  // the sum of all vectors in this cluster
	
	public Cluster(int vectorsSize){
		// Initialize an ArrayList of vectors, which are ArrayLists of doubles
		// Number of vectors per cluster will depend on K and centroids, so initialize without initial capacity
		vectors = new ArrayList<Vector>();
		
		// Similarly, sum and mean are initially empty
		sum = new Vector(vectorsSize);
		mean = new Vector(vectorsSize);
	}
	
	// Returns how many vectors are in this cluster
	public int getSize() {
		return vectors.size();
	}
	
	public ArrayList<Vector> getVectors() {
		return vectors;
	}
	
	public void addVector(Vector vector) {
		// Add vector to cluster
		vectors.add(vector); 
	
		// Add vector to 'sum'
		if(sum.isEmpty()) {
			// If empty, sum is simply all the values in the vector. No summing needed.
			sum.addAll(vector);
		} else {
			// If not empty, sum current 'sum' with vector
			for(int i = 0; i < vector.getSize(); i++){
				sum.setValue(i, sum.getValue(i) + vector.getValue(i));
			}
		}
		
	}

	public void recomputeClusterMean() {
		for(int i = 0; i < sum.getSize(); i++){
			mean.setValue(i, sum.getValue(i) / vectors.size());
		}
	}

	public void removeVector(Vector vector) {
		vectors.remove(vector);
		for(int i = 0; i < vector.getSize(); i++){
			sum.setValue(i, sum.getValue(i) - vector.getValue(i));
		}
	}
	
	public boolean contains(Vector vector) {
		return vectors.contains(vector);
	}
}

/*quick test --
		Vector one = new Vector();
		one.add(1.0);
		one.add(2.0);
		one.add(3.0);
		
		Vector two = new Vector();
		two.add(1.0);
		two.add(2.0);
		two.add(3.0); 
		
		Vector three = new Vector();
		three.add(1.0);
		three.add(1.0);
		three.add(3.0);

		Vector joker = new Vector();
		joker.add(1.0);
		joker.add(2.0);
		joker.add(3.0);
		
		ArrayList<Vector> fakeCluster = new ArrayList<Vector>();
		fakeCluster.add(one);
		fakeCluster.add(two);
		fakeCluster.add(three);
		
		System.out.println("o: "+ fakeCluster.getValue(0));
		
		boolean tf = fakeCluster.contains(joker);
		
		System.out.println("tf: "+ tf);

*/