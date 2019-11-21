

import java.io.FileNotFoundException;

import core.FileReader;
import core.InvalidFormatException;
import data_representation.CSV;
import metrics.comparison.CommonElements;
import metrics.comparison.JaccardIndex;
import metrics.comparison.PairwiseComparisonStrategy;
import metrics.comparison.TestCaseFormatMismatchException;

/**
 * Drivers for testing this subsection of the project
 * 
 * @author luke
 */
public class Capstone {

	public static void main(String[] args) {
		try {
			String testCase1 = getTestCase("data.txt");
			String testCase2 = getTestCase("data2.txt");
			
			System.out.println("test case 1: " + testCase1);
			System.out.println("test case 2: " + testCase2);
			
			PairwiseComparisonStrategy compareStrategy = new CommonElements();
			System.out.println("number of common elements: " + compareStrategy.compare(new CSV(testCase1), new CSV(testCase2)));
			
			compareStrategy = new JaccardIndex();
			System.out.println("Jaccard Index: " + compareStrategy.compare(new CSV(testCase1), new CSV(testCase2)));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (TestCaseFormatMismatchException e) {
			e.printStackTrace();
		}
	}
	
	public static String getTestCase(String filename) throws FileNotFoundException, InvalidFormatException {
		FileReader reader = new FileReader();
		reader.setParser(new CSV());
		return reader.readTestCase(filename);
	}

}
