package core;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import model.Config;
import org.junit.Test;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

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

    @Test
    /**
     * test for the retrieveConfigSetter() method, which looks to see if there is a setter in the Config object for a
     * given field. This is for the positive case, where the setter can be found
     */
    public void testRetrieveConfigSetterExists(){
        ReflectionService reflector = new ReflectionService();

        //read JSON
        JsonReader jsonReader = null;
        try {
            jsonReader = new JsonReader(new FileReader("config.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
        Gson gson = new Gson();
        Config config = gson.fromJson(jsonReader, Config.class);

        //attempt to find setter for a field
        String fieldName = "comparisonMethod";
        Method setter = null;
        try {
            setter = reflector.retrieveConfigSetter(config, String.class, fieldName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail();
        }

        //call the obtained method
        String currentValue = config.getComparisonMethod();
        String newValue = "banana";
        try {
            setter.invoke(config, newValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(newValue, config.getComparisonMethod());

        //restore old value in config file
        config.setComparisonMethod(currentValue);
    }

    @Test
    /**
     * test for the retrieveConfigSetter() method, which looks to see if there is a setter in the Config object for a
     * given field.This is for the negative case, where the setter does not exist
     */
    public void testRetrieveConfigSetterNotExists(){
        ReflectionService reflector = new ReflectionService();

        //read JSON
        JsonReader jsonReader = null;
        try {
            jsonReader = new JsonReader(new FileReader("config.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
        Gson gson = new Gson();
        Config config = gson.fromJson(jsonReader, Config.class);

        //attempt to find setter for a field
        String fieldName = "banana";
        Method setter = null;
        try {
            setter = reflector.retrieveConfigSetter(config, String.class, fieldName);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof  NoSuchMethodException);
        }
    }
}
