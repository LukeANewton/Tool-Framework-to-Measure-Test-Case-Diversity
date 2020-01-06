package core;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
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
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        assertNotNull("Failed to instantiate class '" + className + "'.", instance);
    }
}
