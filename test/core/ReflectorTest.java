package core;


import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ReflectorTest {

    private static Reflector reflector;

    @BeforeEach
    private void setup() {
        reflector = new Reflector();
    }

    @Test
    public void testReflectNoArgsConstructor() {
        reflector.setClassSource("metrics.pairwise.");
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
        assertNotNull(instance, "Failed to instantiate class '" + className + "'.");
    }

    @Test
    @Ignore
    public void testReflectCustomPath() {
        Object instance = null;
        String className = "metrics.DistanceMetric";
        String classPath = "C:\\Users\\Shoyu\\code\\4907-Project\\out\\production\\4907-Project\\";
        try {
            instance = reflector.loadClass(classPath, className);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        assertNotNull(instance, "Failed to instantiate class '" + className + "'.");
    }

}
