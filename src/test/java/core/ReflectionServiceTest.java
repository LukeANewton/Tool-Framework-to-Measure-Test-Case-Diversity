package core;

import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * A test suite for the Reflection service
 */
public class ReflectionServiceTest {

    @Test
    public void testReflectNoArgsConstructor() {
        ReflectionService reflector = new ReflectionService();
        reflector.setClassSource("metrics.comparison.");
        Object instance = null;
        String className = "CommonElements";

        try {
            instance = reflector.loadClass(className);
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to find class " + reflector.getClassSource() + className);
        } catch (IllegalAccessException e) {
            System.err.println("Restricted access to file; cannot load " + className);
        } catch (InstantiationException e) {
            System.err.println("Failed to create instance of " + className);
        } catch (NoSuchMethodException e) {
            System.err.println("Failed to find no args constructor for class " + className);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            System.err.println("Class path not correct format. Required: <classSource>.<class> Found: " + reflector.getClassSource() + className);
            e.printStackTrace();
        }
        assertNotNull("Failed to instantiate class '" + className + "'.", instance);
    }

    @Test
    public void testReflectArgsConstructor() {
        ReflectionService reflector = new ReflectionService("java.awt.");

        Object instance = null;
        String className = "Rectangle";
        Class[] constructorTemplate = new Class[] {int.class, int.class};
        Object[] constructorValues = new Object[] {5, 10};

        try {
            instance = reflector.loadClass(className, constructorTemplate, constructorValues);
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to find class " + reflector.getClassSource() + className);
        } catch (IllegalAccessException e) {
            System.err.println("Restricted access to file; cannot load " + className);
        } catch (InstantiationException e) {
            System.err.println("Failed to create instance of " + className);
        } catch (NoSuchMethodException e) {
            System.err.println("Failed to find args constructor for class '" + className + "' with arg template '" +
                    Arrays.toString(constructorTemplate) + "' and args '" + Arrays.toString(constructorValues) + "'.");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            System.err.println("Class path not correct format. Required: <classSource>.<class> Found: " + reflector.getClassSource() + className);
            e.printStackTrace();
        }
        assertNotNull("Failed to instantiate class '" + className + "'.", instance);

        Rectangle rec = (Rectangle) instance;

        assertEquals("Failed to set constructor argument.", 10, rec.height);
    }

    @Test
    public void testGetClassSource() {
        ReflectionService reflector = new ReflectionService("metrics.comparison.");
        assertEquals("Class source obtained from reflector is incorrect.", "metrics.comparison.", reflector.getClassSource());
    }

    @Test
    /**
     * test for when reflection is invoked with a malformed pathname.
     * an InvalidFormat Exception should be thrown
     */
    public void testInvalidPathFormatNoArgs(){
        ReflectionService reflector = new ReflectionService();
        reflector.setClassSource("metrics,comparison,");
        Object instance = null;
        String className = "CommonElements";

        try {
            instance = reflector.loadClass(className);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
    }

    @Test
    /**
     * test for when reflection is invoked with a malformed pathname.
     * an InvalidFormat Exception should be thrown
     */
    public void testInvalidPathFormatArgs(){
        ReflectionService reflector = new ReflectionService("java/awt/");

        Object instance = null;
        String className = "Rectangle";
        Class[] constructorTemplate = new Class[] {int.class, int.class};
        Object[] constructorValues = new Object[] {5, 10};

        try {
            instance = reflector.loadClass(className, constructorTemplate, constructorValues);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
    }

    @Test
    /**
     * test for the searchPackage() method, which searches a package for all objects that implement a specified interface
     */
    public void testSearchPackage(){
        ReflectionService reflector = new ReflectionService();
        try {
            Object[] list = reflector.searchPackage("metrics.comparison", "PairwiseComparisonStrategy");
            assertEquals(3, list.length);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**
     * test for the searchPackage() method, which searches a package for all objects that implement a specified interface.
     * This method introduces a non-class file, to ensure that it is skipped over and does not cause any errors
     */
    public void testSearchPackageWithNonClassFile(){
        //create file
        File file = new File("target/classes/metrics/comparison/banana");
        try {
            file.createNewFile();
        } catch (Exception e) {
            fail();
        }

        //do the test
        ReflectionService reflector = new ReflectionService();
        try {
            Object[] list = reflector.searchPackage("metrics.comparison", "PairwiseComparisonStrategy");
            assertEquals(3, list.length);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        //remove file
        file.delete();
    }

    @Test
    /**
     * test for the searchPackage() method, which searches a package for all objects that implement a specified interface.
     * this test is for the case where the package being searched for does not exist
     */
    public void testSearchPackageNoSuchDirectory(){
        ReflectionService reflector = new ReflectionService();
        try {
            Object[] list = reflector.searchPackage("banana", "PairwiseComparisonStrategy");
            assertNull(list);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
