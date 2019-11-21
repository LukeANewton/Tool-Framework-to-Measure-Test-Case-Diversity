package core;

import main.java.core.Reflector;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertNotNull;


public class ReflectorTest {

    private static Reflector reflector;

    @Before
    public void setup() {
        reflector = new Reflector();
    }

    @Test
    public void testReflectNoArgsConstructor() {
        reflector.setClassSource("main.java.comparison.");
        Object instance = null;
        String className = "DistanceMetric";

        try {
            instance = reflector.loadClass(className);
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to find class " + className);
        } catch (IllegalAccessException e) {
            System.err.println("Restricted access to file; cannot load " + className);
        } catch (InstantiationException e) {
            System.err.println("Failed to create instance of " + className);
        }
        assertNotNull("Failed to instantiate class '" + className + "'.", instance);
    }

    @Test
    @Ignore
    public void testReflectCustomPath() {
        Object instance = null;
        String className = "metrics.comparison.DistanceMetric";
        String classPath = "C:\\Users\\Shoyu\\code\\4907-Project\\out\\production\\4907-Project\\";
        try {
            instance = reflector.loadClass(classPath, className);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        assertNotNull("Failed to instantiate class '" + className + "'.", instance);
    }

}
