

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Vector {

	ArrayList<Double> values;
	String articleName;
	int articleNumber;
	
	public static void main(String[] args) {
		/*
		System.out.println("vec");
		ArrayList<Double> arrList = new ArrayList<>();
		System.out.println(intToFilename(121));
		*/
		
	}
	
	public Vector(int size) {
		values = new ArrayList<Double>(Collections.nCopies(size, 0.0));
	}
	
	public Vector(int num, ArrayList<Double> al) {
		values = al;
		articleNumber = num;
		articleName = intToFilename(num);
	}
	
	public static String intToFilename(int num) {
		int[] aggr = {0, 8, 16, 20, 28, 41, 46, 54, 64, 68, 86, 94 ,104, 111, 116, 122};
		int c=0;
		int a=0;
		for(int i = 0; i < aggr.length; i++ ) {
			if(aggr[i] >= num) {
				c = i;
				a = num - aggr[i-1];
				break;
			}
		}
		return String.format("c%d, article%d", c,a);
	}

	public void setValue(int i, Double number) {
		values.set(i, number);
	}

	public void setArticleProperties(int i) {
		articleNumber = i;
		articleName = intToFilename(i);
	}

	public int getSize() {
		return values.size();
	}

	public Object getValue(int i) {
		return values.get(i);
	}

	public String getArticleName() {
		return articleName;
	}
}
