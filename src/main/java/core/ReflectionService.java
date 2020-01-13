package core;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates instances of classes using their constructor and arguments provided by the caller.
 *
 * @author crushton
 */
public class ReflectionService {

    private String classSource;

    public ReflectionService() {
        classSource = "";
    }

    public ReflectionService(String source) {
        classSource = source;
    }

    /**
     * Loads a class with a no-args constructor.
     *
     * @param className the path of the class to load relative to the root of the project.
     * @return an instance of the loaded class.
     */
    public Object loadClass(String className)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, InvalidFormatException {

        Class<?> myClass = Class.forName(checkFormat(className));
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
            throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, InvalidFormatException {

        Class<?> definition;
        Constructor<?> initArgsConstructor;
        definition = Class.forName(checkFormat(className));
        initArgsConstructor = definition.getConstructor(initArgsClasses);
        return initArgsConstructor.newInstance(constructorArgs);
    }

    /**
     * Ensures that the path is the correct format.
     * For example, src/main/java/ 'my_package.my_other_package.class_name' is the path with the quoted part the full path used.
     *
     * @param className the package source string delimited with periods
     * @return the new source string
     * @throws InvalidFormatException when the path to the class isn't specified like above
     */
    private String checkFormat(String className) throws InvalidFormatException {
        String pathToClass = classSource + className;
        Pattern pathPatternToClass = Pattern.compile("([\\w]+\\.?)+");
        Matcher matcher = pathPatternToClass.matcher(pathToClass);
        if (matcher.find()) {
            return pathToClass;
        } else {
            throw new InvalidFormatException();
        }

    }

    public String getClassSource() {
        return classSource;
    }

    public void setClassSource(String classSource) {
        this.classSource = classSource;
    }
}
