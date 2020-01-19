package core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.*;

public class FileWriterServiceTest {
    private FileWriterService fileWriterService;
    private static final String FILENAME = "file-writer-test.txt";
    private static final String FILE_CONTENTS = "banana";
    private File file;

    @Before
    public void setUp(){
        fileWriterService = new FileWriterService("");
        file = new File(FILENAME);

        if(file.exists())//ensure we start with no file with the specified name
            file.delete();
    }

    @After
    public void tearDown(){
        if(file.exists())
            file.delete();
    }

    /**
     * reads a file's contents into a single string
     *
     * @param filename the location of the file
     * @return a String containing the entire contents of the file
     * @throws FileNotFoundException thrown when no file with the specified name is found
     */
    private String readFile(String filename) throws FileNotFoundException {
        if(filename.equals(""))
            throw new FileNotFoundException();

        File file = new File(filename);

        Scanner sc = new Scanner(file);

        StringBuffer s = new StringBuffer();
        while (sc.hasNextLine()) {
            s.append(sc.nextLine());
            if(sc.hasNextLine())
                s.append(System.lineSeparator());
        }
        sc.close();
        return s.toString();
    }

    @Test
    /**test the write function for the positive normal case (no overwrite or append)*/
    public void testWrite(){
        try {// write the file
            fileWriterService.write(FILENAME, FILE_CONTENTS, false, false);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        //read back in to test
        assertTrue(file.exists());
        try {
            assertEquals(FILE_CONTENTS, readFile(FILENAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**write test using a path set in constructor*/
    public void testWriteFullPath(){
        String foldername = "service-test//";
        fileWriterService = new FileWriterService(foldername);

        //create the folder
        File folder = new File(foldername);
        if(!folder.exists())
            folder.mkdir();

        try {// write the file
            fileWriterService.write(FILENAME, FILE_CONTENTS, false, false);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        //read back in to test
        file = new File(foldername + FILENAME);
        assertTrue(file.exists());
        try {
            assertEquals(FILE_CONTENTS, readFile(foldername + FILENAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        //remove directory and file
        folder.delete();
        file.delete();
    }

    @Test
    /**test the write function for appending to a file that does not exist*/
    public void testWriteAppendNotExist(){
        try {// write the file
            fileWriterService.write(FILENAME, FILE_CONTENTS, false, true);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        //read back in to test
        assertTrue(file.exists());
        try {
            assertEquals(FILE_CONTENTS, readFile(FILENAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**test the write function for appending to a file*/
    public void testWriteAppend(){
        try {// write the file twice, once to create, once to append
            fileWriterService.write(FILENAME, FILE_CONTENTS, false, false);
            fileWriterService.write(FILENAME, FILE_CONTENTS, false, true);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        //read back in to test
        assertTrue(file.exists());
        try {
            assertEquals(FILE_CONTENTS+"\r\n"+FILE_CONTENTS, readFile(FILENAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**test the write function for overwriting a file*/
    public void testWriteOverwrite(){
        try {// write the file twice, once to create, once to overwrite
            fileWriterService.write(FILENAME, FILE_CONTENTS, false, false);
            fileWriterService.write(FILENAME, FILE_CONTENTS, true, false);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        //read back in to test
        assertTrue(file.exists());
        try {
            assertEquals(FILE_CONTENTS, readFile(FILENAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**test the write function for overwriting a file that does not exist*/
    public void testWriteOverwriteNotExists() {
        try {// write the file
            fileWriterService.write(FILENAME, FILE_CONTENTS, true, false);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        //read back in to test
        assertTrue(file.exists());
        try {
            assertEquals(FILE_CONTENTS, readFile(FILENAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**test the write function for a file when overwrite and append are set
     * This operates the same as overwrite, since if we overwrite, there is nothing to append to*/
    public void testWriteOverwriteAppend(){
        try {// write the file
            fileWriterService.write(FILENAME, FILE_CONTENTS, true, true);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        //read back in to test
        assertTrue(file.exists());
        try {
            assertEquals(FILE_CONTENTS, readFile(FILENAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = IOException.class)
    /**test the write function for a file we do no want to overwrite or append, but the file exists*/
    public void testWriteFailure() throws IOException {
        // write the file twice, once to create, once to trigger the exception
        try {
            fileWriterService.write(FILENAME, FILE_CONTENTS, false, false);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        fileWriterService.write(FILENAME, FILE_CONTENTS, false, false);
    }
}