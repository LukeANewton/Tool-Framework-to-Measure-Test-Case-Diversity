package core;

import org.junit.Test;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A test suite for the Reflection service
 */
public class ReflectorTest {

    @Test
    public void testReflectNoArgsConstructor() {
        Reflector reflector = new Reflector();
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
        Reflector reflector = new Reflector("java.awt.");

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
        Reflector reflector = new Reflector("metrics.comparison.");
        assertEquals("Class source obtained from reflector is incorrect.", "metrics.comparison.", reflector.getClassSource());
    }
}
