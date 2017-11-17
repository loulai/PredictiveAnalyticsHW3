
import com.mkobos.pca_transform.PCA;

import Jama.Matrix;

/** An example program using the library */
public class MyPCA {
	public static void main(String[] args){
		System.out.println("Running a demonstration program on some sample data ...");
		
		/** Training data matrix with each row corresponding to data point and
		* each column corresponding to dimension. */
		Matrix trainingData = new Matrix(new double[][] {
			{1, 2, 3, 4, 5, 6},
			{6, 5, 4, 3, 2, 1},
			{2, 2, 2, 2, 2, 2}});
		PCA pca = new PCA(trainingData);
		System.out.println("what is this: " + pca.getEigenvalue(0));
		
		Matrix myTD = new Matrix(new double[][] {
			{2, 3, 1, 1},
			{4, 4, 1, 1},
			{0, 1, 4, 4},
			{0, 1, 2, 2}});
		PCA myPca = new PCA(myTD);
		System.out.println("what is this: " + myPca.getEigenvalue(0));
		
		//myTD.eig();
		
		/** Test data to be transformed. The same convention of representing
		* data points as in the training data matrix is used. */
		Matrix testData = new Matrix(new double[][] {
				{1, 2, 3, 4, 5, 6},
				{1, 2, 1, 2, 1, 2}});
		
		/** The transformed test data. */
		Matrix transformedData = pca.transform(testData, PCA.TransformationType.ROTATION);
		
		System.out.println("Transformed data (each row corresponding to transformed data point):");
		for(int r = 0; r < transformedData.getRowDimension(); r++){
			for(int c = 0; c < transformedData.getColumnDimension(); c++){
				System.out.print(transformedData.get(r, c));
				if (c == transformedData.getColumnDimension()-1) continue;
				System.out.print(", ");
			}
			System.out.println();
		}
	}
}
