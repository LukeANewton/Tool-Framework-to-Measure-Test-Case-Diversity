package core;

import org.junit.Test;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

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
        String implementedInterface = null;
        instance = reflector.loadClass(classPath, implementedInterface);
        assertNotNull("Failed to instantiate class '" + classPath + "' with a null interface.", instance);

        implementedInterface = "";
        instance = reflector.loadClass(classPath, implementedInterface);
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
}
