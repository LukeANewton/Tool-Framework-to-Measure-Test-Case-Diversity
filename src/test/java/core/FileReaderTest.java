package core;

import data_representation.CSV;
import data_representation.DataRepresentation;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;


public class FileReaderTest {

    private static FileReaderService fileReader;

    @Before
     /*setup to perform before each test case*/
    public void setup() {
        fileReader = new FileReaderService();
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
        new File(filename).delete();
    }

    @Test
    /*
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * tests the 'normal' case for reading a file. That is, the file exists and the format passed
     * matches the format of the file contents. The contents read in should match the contents of
     * the file.
     */
    public void testSingleTestCasePositiveCase() throws FileNotFoundException, InvalidFormatException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String filename = "test.txt";
        DataRepresentation d = new CSV();
        String contents = "1,2,3,4,5,6";
        createFile(filename, contents);

        String testcase = fileReader.readIntoDataRepresentation(filename, null, d)[0].toString();
        assertEquals(testcase, contents);

        removeFile(filename);
    }

    @Test
    /*
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * tests the 'normal' case for reading a file. That is, the file exists, the delimiter is in the file,
     * and the format passed matches the format of the file contents. The result should be a list of test
     * cases split on the delimiter
     */
    public void testManyTestCasesPositiveCase() throws NoSuchMethodException, InvalidFormatException, IllegalAccessException, InstantiationException, FileNotFoundException, InvocationTargetException {
        String filename = "test";
        String delimiter = "   ";
        DataRepresentation d = new CSV();
        String contents = "1,2,3,4,5,6   1,2,3   5,6,7";
        createFile(filename, contents);

        DataRepresentation[] testcase = fileReader.readIntoDataRepresentation(filename, delimiter, d);
        assertEquals(3, testcase.length);
        assertEquals("1,2,3,4,5,6", testcase[0].toString());
        assertEquals("1,2,3", testcase[1].toString());
        assertEquals("5,6,7", testcase[2].toString());

        removeFile(filename);
    }

    @Test
    /*
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * tests the case of the specified delimiter is null. In this case, everything should be
     * stored as a single test case
     */
    public void testNullDelimiter() throws NoSuchMethodException, InvalidFormatException, IllegalAccessException, InstantiationException, FileNotFoundException, InvocationTargetException {
        String filename = "test";
        String delimiter = null;
        DataRepresentation d = new CSV();
        String contents = "1,2,3,4,5,6   1,2,3   5,6,7";
        createFile(filename, contents);

        DataRepresentation[] testcase = fileReader.readIntoDataRepresentation(filename, delimiter, d);
        assertEquals(1, testcase.length);
        removeFile(filename);
    }

    @Test
    /*
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * Test for the case of an empty filename. a FileNotFoundException should be thrown
     * since files cannot be nameless
     */
    public void testEmptyFilename() {
        String filename = "";
        String delimiter = "\r\n";
        DataRepresentation d = new CSV();
        String contents = "1,2,3,4,5,6\n1,2,3\n5,6,7";
        createFile(filename, contents);

        try {
            fileReader.readIntoDataRepresentation(filename, delimiter, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof FileNotFoundException);
        }
        removeFile(filename);
    }

    @Test
    /*
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * tests for the case of an empty file. The contents of an empty file will not match the
     * given data representation, so it should throw a InvalidFormatException
     */
    public void testReadEmptyFile() throws NoSuchMethodException, InvalidFormatException, IllegalAccessException, InstantiationException, FileNotFoundException, InvocationTargetException {
        String filename = "test";
        String delimiter = "\r\n";
        DataRepresentation d = new CSV();
        String contents = "";
        createFile(filename, contents);

        DataRepresentation[] testCases = fileReader.readIntoDataRepresentation(filename, delimiter, d);
        assertEquals(testCases.length, 0);
        removeFile(filename);
    }

    @Test
    /*
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * Test for when the data representation passed is null. An InvalidFormatException
     * should be thrown
     */
    public void testNullFormat() {
        String filename = "test";
        String delimiter = "\r\n";
        DataRepresentation d = null;
        String contents = "1,2,3,4,5,6\n1,2,3\n5,6,7";
        createFile(filename, contents);

        try {
            fileReader.readIntoDataRepresentation(filename, delimiter, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
        removeFile(filename);
    }

    @Test
    /*
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     * Test for when the filename does not match any existing files. A
     * FileNotFoundExcpetion should be thrown
     */
    public void testNoMatchingFile() {
        String filename = "test";
        String delimiter = "\r\n";
        DataRepresentation d = new CSV();
        String contents = "1,2,3,4,5,6\n1,2,3\n5,6,7";
        createFile(filename, contents);

        try {
            fileReader.readIntoDataRepresentation("filename", delimiter, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof FileNotFoundException);
        }
        removeFile(filename);
    }

    @Test
    /*
     * Test for: String[] read(String filename, String delimiter, DataRepresentation format)
     *
     *  Test for when the contents of the file does not match the passed data representation.
     *  An InvalidFormatException should be thrown
     */
    public void testFormatMismatch() {
        String filename = "test";
        String delimiter = "s";
        DataRepresentation d = new CSV();
        String contents = "\n\ns\n\n";
        createFile(filename, contents);

        try {
            fileReader.readIntoDataRepresentation(filename, delimiter, d);
            fail(); // if exception not thrown, fail
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
        removeFile(filename);
    }
}

