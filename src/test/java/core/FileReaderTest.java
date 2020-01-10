package core;

import data_representation.CSV;
import data_representation.DataRepresentation;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;


public class FileReaderTest {

    private static FileReader fileReader;

    @Before
     /**setup to perform before each test case*/
    public void setup() {
        fileReader = new FileReader();
    }

    /**
     * creates a file for use in testing
     *
     * @param filename the name to give the file
     * @param contents the contents of the file
     * @return true if the file was created successfully
     */
    private boolean createFile(String filename, String contents){
        //create the file
        File file = new File(filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
            return false;
        }

        //write contents to file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(contents.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * deletes a file with the specified name, used as cleanup after test
     *
     * @param filename the name of the file to remove
     */
    private void removeFile(String filename){
        File file = new File(filename);
        file.delete();
    }

    @Test
    /**
     * Test for: String read(String filename, DataRepresentation format)
     *
     * tests the 'normal' case for reading a file. That is, the file exists and the format passed
     * matches the format of the file contents. The contents read in should match the contents of
     * the file.
     */
    public void testSingleTestCasePositiveCase() {
        String filename = "test.txt";
        DataRepresentation d = new CSV();
        String contents = "1,2,3,4,5,6";
        createFile(filename, contents);

        try {
            String testcase = fileReader.read(filename, d);
            assertTrue(testcase.equals(contents));
        } catch (Exception e) {
            // if there is an exception here, the test should always fail
            fail();
        }
        removeFile(filename);
    }

    @Test
    /**
     * Test for: String read(String filename, DataRepresentation format)
     *
     * Test for the case of an empty file. The contents of an empty file will not match the
     * given data representation, so it should throw a InvalidFormatException
     */
    public void testSingleTestCaseEmptyFile() {
        String filename = "test.txt";
        DataRepresentation d = new CSV();
        String contents = "";
        createFile(filename, contents);

        try {
            String testcase = fileReader.read(filename, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
        removeFile(filename);
    }

    @Test
    /**
     * Test for: String read(String filename, DataRepresentation format)
     *
     * Test for the case of an empty filename. a FileNotFoundException should be thrown
     * since files cannot be nameless
     */
    public void testSingleTestCaseEmptyFilename() {
        String filename = "";
        DataRepresentation d = new CSV();

        try {
            String testcase = fileReader.read(filename, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof FileNotFoundException);
        }
    }

    @Test
    /**
     * Test for: String read(String filename, DataRepresentation format)
     *
     * Test for when the filename does not match any existing files. A
     * FileNotFoundExcpetion should be thrown
     */
    public void testSingleTestCaseNoMatchingFile() {
        String filename = "test.txt";
        DataRepresentation d = new CSV();
        createFile(filename,"1,2,3,4,5,6");

        try {
            String testcase = fileReader.read("filename", d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof FileNotFoundException);
        }
        removeFile(filename);
    }

    @Test
    /**
     * Test for: String read(String filename, DataRepresentation format)
     *
     * Test for when the data representation passed is null. An InvalidFormatException
     * should be thrown
     */
    public void testSingleTestCaseNullFormat() {
        String filename = "test.txt";
        DataRepresentation d = null;
        createFile(filename,"1,2,3,4,5,6");

        try {
            String testcase = fileReader.read(filename, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
        removeFile(filename);
    }

    @Test
    /**
     * Test for: String read(String filename, DataRepresentation format)
     *
     * Test for when the contents of the file does not match the passed data representation.
     * An InvalidFormatException should be thrown
     */
    public void testSingleTestCaseFormatMismatch() {
        String filename = "test.txt";
        DataRepresentation d = new CSV();
        createFile(filename,"\n\n\n\n\n");

        try {
            String testcase = fileReader.read(filename, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
        removeFile(filename);
    }

    @Test
    /**
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * tests the 'normal' case for reading a file. That is, the file exists, the delimiter is in the file,
     * and the format passed matches the format of the file contents. The result should be a list of test
     * cases split on the delimiter
     */
    public void testManyTestCasesPositiveCase() {
        String filename = "test";
        String delimiter = "   ";
        DataRepresentation d = new CSV();
        String contents = "1,2,3,4,5,6   1,2,3   5,6,7";
        createFile(filename, contents);

        try {
            String[] testcase = fileReader.read(filename, delimiter, d);
            assertTrue(testcase.length == 3);
            assertTrue(testcase[0].equals("1,2,3,4,5,6"));
            assertTrue(testcase[1].equals("1,2,3"));
            assertTrue(testcase[2].equals("5,6,7"));
        } catch (Exception e) {
            // if there is an exception here, the test should always fail
            e.printStackTrace();
            fail();
        }
        removeFile(filename);
    }

    @Test
    /**
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * tests the case of the specified delimiter is null. In this case, everything should be
     * stored as a single test case
     */
    public void testManyTestCasesNullDelimiter() {
        String filename = "test";
        String delimiter = null;
        DataRepresentation d = new CSV();
        String contents = "1,2,3,4,5,6   1,2,3   5,6,7";
        createFile(filename, contents);

        try {
            String[] testcase = fileReader.read(filename, delimiter, d);
            assertTrue(testcase.length == 1);
        } catch (Exception e) {
            // if there is an exception here, the test should always fail
            e.printStackTrace();
            fail();
        }
        removeFile(filename);
    }

    @Test
    /**
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * Test for the case of an empty filename. a FileNotFoundException should be thrown
     * since files cannot be nameless
     */
    public void testManyTestCasesEmptyFilename() {
        String filename = "";
        String delimiter = "\r\n";
        DataRepresentation d = new CSV();
        String contents = "1,2,3,4,5,6\n1,2,3\n5,6,7";
        createFile(filename, contents);

        try {
            String[] testcase = fileReader.read(filename, delimiter, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof FileNotFoundException);;
        }
        removeFile(filename);
    }

    @Test
    /**
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * tests for the case of an empty file. The contents of an empty file will not match the
     * given data representation, so it should throw a InvalidFormatException
     */
    public void testManyTestCasesEmptyFile() {
        String filename = "test";
        String delimiter = "\r\n";
        DataRepresentation d = new CSV();
        String contents = "";
        createFile(filename, contents);

        try {
            String[] testcase = fileReader.read(filename, delimiter, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
        removeFile(filename);
    }

    @Test
    /**
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * Test for when the data representation passed is null. An InvalidFormatException
     * should be thrown
     */
    public void testManyTestCasesNullFormat() {
        String filename = "test";
        String delimiter = "\r\n";
        DataRepresentation d = null;
        String contents = "1,2,3,4,5,6\n1,2,3\n5,6,7";
        createFile(filename, contents);

        try {
            String[] testcase = fileReader.read(filename, delimiter, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
        removeFile(filename);
    }

    @Test
    /**
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * Test for when the filename does not match any existing files. A
     * FileNotFoundExcpetion should be thrown
     */
    public void testManyTestCasesNoMatchingFile() {
        String filename = "test";
        String delimiter = "\r\n";
        DataRepresentation d = new CSV();
        String contents = "1,2,3,4,5,6\n1,2,3\n5,6,7";
        createFile(filename, contents);

        try {
            String[] testcase = fileReader.read("filename", delimiter, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof FileNotFoundException);
        }
        removeFile(filename);
    }

    @Test
    /**
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     *  Test for when the contents of the file does not match the passed data representation.
     *  An InvalidFormatException should be thrown
     */
    public void testManyTestCasesFormatMismatch() {
        String filename = "test";
        String delimiter = "s";
        DataRepresentation d = new CSV();
        String contents = "\n\ns\n\n";
        createFile(filename, contents);

        try {
            String[] testcase = fileReader.read(filename, delimiter, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
        removeFile(filename);
    }
}

