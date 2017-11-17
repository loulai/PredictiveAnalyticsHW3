import java.io.File;
import java.io.PrintWriter;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 * Calculates SVD 
 * @input a Jama Matrix object, with rows representing articles
 * 		  and column values representing TFIDF scores for a particular term
 */
public class MySVD {
	
   public static void main(String[] args) { 
    // Import file
    File inputFile = new File("./tfidfMatrix.csv");
    
    // Format file into Dataset
	CSVToVectors tfidfVectors = new CSVToVectors(inputFile);
	Dataset tfidfDataset = new Dataset(tfidfVectors);	
	
	// Create a M-by-N matrix that doesn't have full rank
	// This converts a 8*5 matrix to 5*3
	// 8*5 x 5*3 = 8*3
	// M*N x N*3 = M*3
	int M = 14, N = 14; // This is the largest possible. Turns out Jama has an error and can't process matrices with full rank! 
	Dataset reducedDataset = tfidfDataset.subset(M, N);
	Matrix B = Matrix.random(N, 3);
	Matrix A = reducedDataset.matrix.times(B).times(B.transpose());
	
	// A
	System.out.print("A = ");
	A.print(9, 6);
	System.out.println();
	
	// Compute SVD 
	SingularValueDecomposition s = A.svd();
	
	// U
	System.out.print("U = ");
	Matrix U = s.getU();
	U.print(9, 6);
	
	// Sigma
	System.out.print("Sigma = ");
	Matrix S = s.getS();
	S.print(9, 6);
	
	// V
	System.out.print("V = ");
	Matrix V = s.getV();
	V.print(9, 6);
	
	// Print statistics
	System.out.println("rank = " + s.rank());
	System.out.println("condition number = " + s.cond());
	System.out.println("2-norm = " + s.norm2());
	
	// Print out singular values
	System.out.print("singular values = ");
	Matrix svalues = new Matrix(s.getSingularValues(), 1);
	svalues.print(9, 6);
	
   }
}
/**
 *  Note: This can be improved by analyzing all values of all vectors. Currently it is constrained
 *        by faulty Jama that cannot calculate SVD for full-rank matrices.
 *        
 *  Example: Currently calcluates 14 * 14. Ideally, should calculate 112 * #allwords
 */
