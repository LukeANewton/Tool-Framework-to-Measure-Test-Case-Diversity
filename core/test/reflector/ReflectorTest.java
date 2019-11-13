package reflector;

import metrics.Comparison;
import org.junit.jupiter.api.Test;
import reflector.Reflector;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReflectorTest {

    @Test
    public void testReflect() {
        Reflector r = new Reflector();
        Object instance = null;
        try {
            instance = r.loadClass("path/to/DistanceMetric");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to find class.");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Failed to find class.");
        }
        assertTrue(instance instanceof Comparison);
    }

}
