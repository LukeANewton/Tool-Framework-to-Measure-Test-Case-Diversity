package core;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Creates instances of classes using their constructor and arguments provided by the caller.
 */
public class Reflector {

    private String classSource;

    public Reflector() {
        classSource = "";
    }

    public Reflector(String source) {
        classSource = endsWithPeriod(source);
    }

    /**
     * Loads a class with a no-args constructor.
     *
     * @param className the path of the class to load relative to the root of the project.
     * @return an instance of the loaded class.
     */
    public Object loadClass(String className)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> myClass = Class.forName(classSource + className);
        return myClass.getDeclaredConstructor().newInstance();
    }

    /**
     * Loads a class with a constructor that takes arguments.
     * Example inputs: when classSource is "java.awt.":
     * ("Rectangle", new Class[] {int.class, int.class}, new Object[] {5, 10})
     *
     * @param className the path of the class to load relative to the root of the project.
     * @param initArgsClasses the classes array that the constructor takes.
     * @param constructorArgs an array of object parameters that a loaded class may need passed into it's constructor.
     * @return an instance of the loaded class to be type casted by the caller.
     */
    public Object loadClass(String className, Class[] initArgsClasses, Object[] constructorArgs)
            throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class<?> definition;
        Constructor<?> initArgsConstructor;
        definition = Class.forName(classSource + className);
        initArgsConstructor = definition.getConstructor(initArgsClasses);
        return initArgsConstructor.newInstance(constructorArgs);
    }

    /**
     * Ensures that the source ends with a period for properly formatting the path.
     * For example, src/main/java/ 'my_package.my_other_package.class_name' is the path with the quoted part the full path used.
     *
     * @param source the package source string delimited with periods
     * @return the new source string
     */
    private String endsWithPeriod(String source) {
        if (!(source.substring(source.length() - 1)).equals(".")) {
            source = source + ".";
        }
        return source;
    }

    public String getClassSource() {
        return classSource;
    }

    public void setClassSource(String classSource) {
        this.classSource = endsWithPeriod(classSource);
    }
}
