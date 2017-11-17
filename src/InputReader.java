
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputReader {
	
	/** The number of time series in the last file that was read */
	int clusterCount = 0;
	
	/** The number of dimensions used to describe each instance */
	int dimensionCount = 0;
	
	/** The names of the attributes **/
	private List<String> attributeNames = null;
		
	/**
	 * Default constructor
	 */
	public InputReader() {
	}

	public List<Cluster> runAlgorithm(String input) throws IOException {
		
		// create a variable to store 
		List<Cluster> clusters = new ArrayList<Cluster>();

		BufferedReader myInput = null;
		String thisLine;

		// prepare object 
		myInput = new BufferedReader(new InputStreamReader( new FileInputStream(new File(input))));
		
		// for each line (transaction) until the end of file
		while ((thisLine = myInput.readLine()) != null) {

			// if the line is  a comment, is  empty or is a
			// kind of metadata
			if (thisLine.isEmpty() == true ||
					thisLine.charAt(0) == '#' || thisLine.charAt(0) == '%'){
				continue;
			}
			

			//========================== CONVERT THE LINE TO A CLUSTER ===============================
			// We first process the line to remove all occurrences of the '[' character because we do not need them
			//[Instance2 1.0 0.0][Instance0 1.0 1.0][Instance1 0.0 1.0]
			thisLine = thisLine.substring(1,thisLine.length()).replace('[', ' ');
			
			// Then, we split the lines into string values 
			String tokens[] = thisLine.split(" "); 
			
			// Then, if it is the first instance of the first cluster, we will count how many dimensions there is in an instance.
			// We will also check if the instance has name. Having this information will make it easier
			// thereafter for reading all the instances in the file
			if(clusters.size() == 0){
				// For each token on this line
				for(String token : tokens){
			
					boolean lastDimension = false;
					// If it is the last dimension of the first instance
					if(token.charAt(token.length()-1)== ']'){
						// remove the ] character
						token = token.substring(0, token.length()-1);
						// remember that it is the last dimension of the first instance
						lastDimension = true;
					}

					// try to convert the current token to a double value
					double value = 0;
					boolean isNumber = true;
					try {
						value = Double.parseDouble(token);
					} catch (NumberFormatException nfe) {
						// if it is not a double value, we will remember it
						isNumber = false;
					}
					
					// if it is a number,  increase the number of dimension for the first instance
					if(isNumber){
						dimensionCount++;
					}
					
					// if it is the last dimension of the first instance, we don't need to look at the other instance
					// because we have collected the information that we want, that is
					// the number of dimensions to describe each instance and wheter instances have names or not
					if(lastDimension){
						break;
					}
				}

			}
			
			// Now we will read all instances in the current line (cluster)
			// If instances have names
			
				// If the instances don't have names
				
				// This will store the current instance being read
				DoubleArray instance = null;
				// This will store the values of the current instance
				double[] values = null;
				// This is the current cluster
				Cluster cluster = new Cluster();
				// This is the index of the current value in the current instance
				int indexValue = 0;
				
				// This value indicate that we are starting a new instance
				boolean newInstance = true;
				
				// For each token on this like
				for(int i=0; i < tokens.length; i++){
					// if this is a new instance
					if(newInstance){
						// We prepare the new instance
						values = new double[dimensionCount];
						instance =  new DoubleArray(values);
						indexValue = 0;
						newInstance = false;
					} 
					
					// We take the current token
					String token = tokens[i];
					// We check if the token contains ']'
					if(token.charAt(token.length()-1) == ']'){
						newInstance = true;
						token = token.substring(0, token.length()-1);
						cluster.addVector(instance);
					}
	
					// We convert the token to a double value
					double value = 0;
					try {
						value = Double.parseDouble(token);
					} catch (NumberFormatException nfe) {
						// if it is not a double value, we will remember it
						nfe.printStackTrace();
						throw new RuntimeException("Error in input file - parseDouble");
					}
					// We insert the value in the values of the current instance
					values[indexValue++] = value; 
				}
				// We add the cluster to the list of clusters
				clusters.add(cluster);
			}
		
		// If the file did not contain attribute names, we will generate some
		if(attributeNames.size() == 0 && clusters.size() > 0){
			int dimensionCount = clusters.get(0).getVectors().get(0).data.length;
			for(int i = 0; i < dimensionCount; i++){
				attributeNames.add("Attribute"+i);
			}
		}
		
		// remember the number of clusters
		clusterCount = clusters.size();
		
		// closed input file
		myInput.close();
			
		
		// return the clusters
		return clusters;
	}

	/**
	 * Return the number of dimensions for describing each instance
	 * @return the number of dimensions
	 */
	public int getDimensionCount() {
		return dimensionCount;
	}

}