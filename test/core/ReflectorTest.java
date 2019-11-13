package core;

import metrics.Comparison;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ReflectorTest {

    @Test
    public void testReflect() {
        Reflector r = new Reflector("metrics.");
        Object instance = null;
        String className = "DistanceMetric";

        try {
            instance = r.loadClass(className);
            Comparison comparisonMethod = (Comparison)instance;
            comparisonMethod.compare(1, 2);
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to find class " + className);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Failed to find class.");
        }
        assertNotNull(instance, "Unable to instantiate class '" + className + "'.");
    }

}
