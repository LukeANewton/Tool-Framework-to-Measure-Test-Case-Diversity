package com.compare.metrics.comparison;

import com.compare.data_representation.DataRepresentation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * A pairwise comparison metric that calculates the Levenshtein
 * distance between two test cases. That is, the minimum number of 
 * insertions, deletions, or modifications to elements in the test
 * cases to transform one test case into the other.
 * 
 * Algorithm adapted from: 
 * https://en.wikipedia.org/wiki/Levenshtein_distance#Computing_Levenshtein_distance
 * 
 * @author luke
 *
 */
@Component
public class Levenshtein implements PairwiseComparisonStrategy {

	@Override
	public String getDescription() {
		return "Calculates the minimum number of insertion/deletion/modification operations to transform one test case into another";
	}

	@Override
	public double compare(DataRepresentation testCase1, DataRepresentation testCase2)
			throws TestCaseFormatMismatchException {
		if (!testCase1.checkFormat(testCase2.toString()) || !testCase2.checkFormat(testCase1.toString()))
			throw new TestCaseFormatMismatchException();
		
		//unpack DataRepresentation information
		ArrayList<String> t1 = new ArrayList<>();
		ArrayList<String> t2 = new ArrayList<>();
		while (testCase1.hasNext())
			t1.add(testCase1.next());
		while (testCase2.hasNext())
			t2.add(testCase2.next());
		
		//create matrix for distances, initialized to all zeros
		int[][] m = new int[t1.size()][t2.size()];
		for (int i = 0; i < t1.size(); i++) {
			for (int j = 0; j < t2.size(); j++) {
				m[i][j] = 0;
			}
		}
		
		int cost;
		for(int i = 1; i < t1.size(); i++) 
			m[i][0] = i;
		for(int j = 1; j < t2.size(); j++) 
			m[0][j] = j;
		
		for (int i = 1; i < t1.size(); i++) {
			for (int j = 1; j < t2.size(); j++) {
				if(t1.get(i).equals(t2.get(j)))
					cost = 0;
				else
					cost = 1;
				
				m[i][j] = min(m[i-1][j] + 1, 
						      m[i][j-1] + 1,
						      m[i-1][j-1] + cost);
				
			}
		}	
		return m[t1.size()-1][t2.size()-1];
	}
	
	/**
	 * helper method to find the minimum of 3 integers. Math.min only finds
	 * the minimum between 2 integers, not 3
	 * 
	 * @param i the first integer
	 * @param j the second integer
	 * @param k the third integer
	 * @return the minimum value between the 3 passed integers
	 */
	private int min(int i, int j, int k) {
		return Math.min(Math.min(i,j), k);
	}

		
		


}
