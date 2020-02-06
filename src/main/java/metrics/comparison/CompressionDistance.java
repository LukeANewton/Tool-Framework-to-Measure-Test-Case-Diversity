package metrics.comparison;

import data_representation.DataRepresentation;

import java.io.*;
import java.util.zip.DeflaterOutputStream;

/**
 * this implements a pairwise comparison metric called the
 * normalized compression distance. It writes the test cases to files and writes
 * another file that contains both test cases concatenated, then compresses all
 * three files and compares the sizes.
 *
 * the idea is that files are compressed more when there are more redundancies,
 * so if the concatenated file is a similar size to the individual test cases,
 * the test cases must have many redundancies between them (ie. many similar elements!)
 *
 * @author luke
 */
public class CompressionDistance implements PairwiseComparisonStrategy {
    @Override
    public double compare(DataRepresentation testCase1, DataRepresentation testCase2) throws Exception{
        String testCaseString1;
        String testCaseString2;

        //get the test cases as strings
        StringBuilder sb = new StringBuilder();
        while(testCase1.hasNext())
            sb.append(testCase1.next());
        testCaseString1 = sb.toString();

        sb = new StringBuilder();
        while(testCase2.hasNext())
            sb.append(testCase2.next());
        testCaseString2 = sb.toString();

        //get compression sizes
        double length1 = getCompressedSize(testCaseString1);
        double length2 = getCompressedSize(testCaseString2);
        double concatenatedLength = getCompressedSize(testCaseString1 + testCaseString2);

        return (concatenatedLength - Math.min(length1,length2)) / Math.max(length1,length2);
    }

    /**
     *creates a compressed file out of the passed contents and returns the size of the file
     *
     * @param contents the information to place in the file
     * @return the size of the compressed file
     * @throws Exception when the file cannot be written or compressed
     */
    private long getCompressedSize(String contents) throws Exception {
        File file = File.createTempFile( "normalized-compression-distance-file", null);
        File compressedFile = File.createTempFile( "compressed-normalized-compression-distance-file", null);

        //write to file
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(contents);
        bw.flush();
        bw.close();

        //compress file
        FileInputStream fis=new FileInputStream(file);
        FileOutputStream fos=new FileOutputStream(compressedFile);
        DeflaterOutputStream dos=new DeflaterOutputStream(fos);
        int data;
        while ((data=fis.read())!=-1)
            dos.write(data);
        fis.close();
        dos.close();

        //get size of compressed file
        long size = compressedFile.length();

        //clean up files
        file.delete();
        compressedFile.delete();

        return size;
    }

    @Override
    public String getDescription() {
        return "calculates the similarity of two test cases by the ratio of size between " +
                "the individual test cases compressed, and the compression of the test cases concatenated";
    }
}
