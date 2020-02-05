package metrics.comparison;

import data_representation.DataRepresentation;

import java.io.*;
import java.util.zip.DeflaterOutputStream;

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

    private long getCompressedSize(String contents) throws Exception {
        File file = new File( "normalized-compression-distance-file");
        File compressedFile = new File( "compressed-normalized-compression-distance-file");

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
