package core;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import model.Config;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * A test suite for the Reflection service.
 */
public class ReflectionServiceTest {

    /**
     * Tests that a class with a no args constructor can be instantiated.
     *
     * @throws NoSuchMethodException when the constructor can't be found.
     * @throws ClassNotFoundException when the class or interface doesn't exist.
     * @throws IllegalAccessException when the class or interface are in a read protected space.
     * @throws InvocationTargetException when the class or interface cannot be invoked.
     * @throws InstantiationException when an object of the class cannot be instantiated.
     * @throws InvalidFormatException when the classPath or interfacePath is malformed.
     */
    @Test
    public void testReflectNoArgsConstructor() throws InvalidFormatException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ReflectionService reflector = new ReflectionService();
        Object instance = null;
        String classPath = "metrics.comparison.CommonElements";
        String implementedInterface = "metrics.comparison.PairwiseComparisonStrategy";
        instance = reflector.loadClass(classPath, implementedInterface);
        assertNotNull("Failed to instantiate class '" + classPath + "'.", instance);
    }

    /**
     * Tests that a class with a no args constructor given a null or empty interface can be instantiated.
     *
     * @throws NoSuchMethodException when the constructor can't be found.
     * @throws ClassNotFoundException when the class or interface doesn't exist.
     * @throws IllegalAccessException when the class or interface are in a read protected space.
     * @throws InvocationTargetException when the class or interface cannot be invoked.
     * @throws InstantiationException when an object of the class cannot be instantiated.
     * @throws InvalidFormatException when the classPath or interfacePath is malformed.
     */
    @Test
    public void testReflectNoArgsConstructorNullIF() throws InvalidFormatException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ReflectionService reflector = new ReflectionService();
        Object instance = null;
        String classPath = "metrics.comparison.CommonElements";
        instance = reflector.loadClass(classPath, null);
        assertNotNull("Failed to instantiate class '" + classPath + "' with a null interface.", instance);

        instance = reflector.loadClass(classPath, "");
        assertNotNull("Failed to instantiate class '" + classPath + "' with an empty interface.", instance);
    }

    /**
     * Tests that an InstantiationException is thrown when asked for a class that doesn't implement the provided interface.
     *
     * @throws NoSuchMethodException when the constructor can't be found.
     * @throws ClassNotFoundException when the class or interface doesn't exist.
     * @throws IllegalAccessException when the class or interface are in a read protected space.
     * @throws InvocationTargetException when the class or interface cannot be invoked.
     * @throws InstantiationException when an object of the class cannot be instantiated.
     * @throws InvalidFormatException when the classPath or interfacePath is malformed.
     */
    @Test(expected = InstantiationException.class)
    public void testReflectNoArgsConstructorInvalidIF() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException, InvalidFormatException {
        ReflectionService reflector = new ReflectionService();
        Object instance;
        String className = "java.awt.Rectangle";

        // Prove that the class is normally instantiated fine with the right interface.
        String correctlyImplementedInterface = "java.io.Serializable";
        instance = reflector.loadClass(className, correctlyImplementedInterface);
        assertNotNull(instance);

        // Attempt to pass an incorrect interface, throwing an InstantiationException.
        String implementedInterface = "java.awt.ActiveEvent";
        reflector.loadClass(className, implementedInterface);
    }

    /**
     * Test that a class with a constructor that takes arguments can be instantiated.
     *
     * @throws NoSuchMethodException when the constructor can't be found.
     * @throws ClassNotFoundException when the class or interface doesn't exist.
     * @throws IllegalAccessException when the class or interface are in a read protected space.
     * @throws InvocationTargetException when the class or interface cannot be invoked.
     * @throws InstantiationException when an object of the class cannot be instantiated.
     * @throws InvalidFormatException when the classPath or interfacePath is malformed.
     */
    @Test
    public void testReflectArgsConstructor() throws NoSuchMethodException, InvalidFormatException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        ReflectionService reflector = new ReflectionService();
        Object instance = null;
        String classPath = "java.awt.Rectangle";
        String implementedInterface = "java.io.Serializable";
        Class[] constructorTemplate = new Class[] {int.class, int.class};
        Object[] constructorValues = new Object[] {5, 10};

        instance = reflector.loadClass(classPath, implementedInterface, constructorTemplate, constructorValues);
        assertNotNull("Failed to instantiate class '" + classPath + "'.", instance);

        Rectangle rec = (Rectangle) instance;
        assertEquals("Failed to set constructor argument.", 10, rec.height);
    }

    /**
     * Test that a class with a constructor that takes arguments can be instantiated.
     *
     * @throws NoSuchMethodException when the constructor can't be found.
     * @throws ClassNotFoundException when the class or interface doesn't exist.
     * @throws IllegalAccessException when the class or interface are in a read protected space.
     * @throws InvocationTargetException when the class or interface cannot be invoked.
     * @throws InstantiationException when an object of the class cannot be instantiated.
     * @throws InvalidFormatException when the classPath or interfacePath is malformed.
     */
    @Test
    public void testReflectArgsConstructorInvalidIF() throws NoSuchMethodException, InvalidFormatException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        ReflectionService reflector = new ReflectionService();
        Object instance = null;
        String classPath = "java.awt.Rectangle";
        String implementedInterface = null;
        Class[] constructorTemplate = new Class[] {int.class, int.class};
        Object[] constructorValues = new Object[] {5, 10};

        instance = reflector.loadClass(classPath, implementedInterface, constructorTemplate, constructorValues);
        assertNotNull("Failed to instantiate class '" + classPath + "' with null interface.", instance);

        Rectangle rec = (Rectangle) instance;
        assertEquals("Failed to set constructor argument.", 10, rec.height);

        implementedInterface = "";
        instance = reflector.loadClass(classPath, implementedInterface, constructorTemplate, constructorValues);
        assertNotNull("Failed to instantiate class '" + classPath + "' with empty interface.", instance);

        rec = (Rectangle) instance;
        assertEquals("Failed to set constructor argument.", 10, rec.height);

    }

    /**
     * test for when reflection is invoked with a malformed path when loading a class with a no args constructor.
     * an InvalidFormat Exception should be thrown
     */
    @Test
    public void testInvalidPathFormatNoArgs() {
        ReflectionService reflector = new ReflectionService();

        String classPath = ".CommonElements";
        String implementedInterface = "PairwiseComparisonStrategy";

        try {
            reflector.loadClass(classPath, implementedInterface);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }

        classPath = "metrics/comparison.CommonElements";
        implementedInterface = "metrics.comparison.PairwiseComparisonStrategy";

        try {
            reflector.loadClass(classPath, implementedInterface);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }

        classPath = "metrics.comparison.CommonElements";
        implementedInterface = "metrics.comparison/PairwiseComparisonStrategy";
        try {
            reflector.loadClass(classPath, implementedInterface);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
    }

    /**
     * test for when reflection is invoked with a malformed pathname when loading a class with a constructor that takes args.
     * an InvalidFormat Exception should be thrown
     */
    @Test
    public void testInvalidPathFormatArgs(){
        ReflectionService reflector = new ReflectionService();

        String className = "java.awt/Rectangle";
        String implementedInterface = "java.io.Serializable";
        Class[] constructorTemplate = new Class[] {int.class, int.class};
        Object[] constructorValues = new Object[] {5, 10};

        try {
            reflector.loadClass(className, implementedInterface, constructorTemplate, constructorValues);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }

        className = "java.awt.Rectangle";
        implementedInterface = "java/io.Serializable";
        try {
            reflector.loadClass(className, implementedInterface, constructorTemplate, constructorValues);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
    }

    @Test
    /**
     * test for the searchPackage() method, which searches a package for all objects that implement a specified interface
     */
    public void testSearchPackage() {
        ReflectionService reflector = new ReflectionService();
        try {
            Object[] list = reflector.searchPackage("metrics.comparison", "metrics.comparison.PairwiseComparisonStrategy");
            assertEquals(3, list.length);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    /**
     * test for the searchPackage() method, which searches a package for all objects that implement a specified interface.
     * This method introduces a non-class file in the package to ensure that it is skipped over and does not cause any errors
     */
    public void testSearchPackageWithNonClassFile() {
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
            Object[] list = reflector.searchPackage("metrics.comparison", "metrics.comparison.PairwiseComparisonStrategy");
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
    public void testSearchPackageNoSuchDirectory() {
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
