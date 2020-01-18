package core;

import model.Config;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Creates instances of classes using their constructor and arguments provided by the caller.
 *
 * @author crushton
 */
public class ReflectionService {

    public ReflectionService() {
    }

    /**
     * Instantiate a class with a no-args constructor.
     *
     * @param classPath the path and name of the class to lead relative to the root of the project.
     * @param interfacePath the path and name of the interface that the class is to implement.
     * @return an instance of the loaded class.
     * @throws NoSuchMethodException when the constructor can't be found.
     * @throws ClassNotFoundException when the class or interface doesn't exist.
     * @throws IllegalAccessException when the class or interface are in a read protected space.
     * @throws InvocationTargetException when the class or interface cannot be invoked.
     * @throws InstantiationException when an object of the class cannot be instantiated.
     * @throws InvalidFormatException when the classPath or interfacePath is malformed.
     */
    public Object loadClass(String classPath, String interfacePath)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, InvalidFormatException {
        return loadClassHelper(classPath, interfacePath).getConstructor().newInstance();
    }

    /**
     * Instantiate a class with a constructor that takes arguments.
     * Example inputs:
     * ("java.awt.Rectangle", "java.io.Serializable", new Class[] {int.class, int.class}, new Object[] {5, 10})
     *
     * @param classPath the path and name of the class to lead relative to the root of the project.
     * @param interfacePath the path and name of the interface that the class is to implement.
     * @param initArgsClasses the classes array that the constructor takes.
     * @param constructorArgs an array of object parameters that a loaded class may need passed into it's constructor.
     * @return an instance of the loaded class to be type casted by the caller.
     * @throws NoSuchMethodException when the constructor can't be found.
     * @throws ClassNotFoundException when the class or interface doesn't exist.
     * @throws IllegalAccessException when the class or interface are in a read protected space.
     * @throws InvocationTargetException when the class or interface cannot be invoked.
     * @throws InstantiationException when an object of the class cannot be instantiated.
     * @throws InvalidFormatException when the classPath or interfacePath is malformed.
     */
    public Object loadClass(String classPath, String interfacePath, Class[] initArgsClasses, Object[] constructorArgs)
            throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, InvalidFormatException {
        Class<?> myClass = loadClassHelper(classPath, interfacePath);
        Constructor<?> initArgsConstructor = myClass.getConstructor(initArgsClasses);
        return initArgsConstructor.newInstance(constructorArgs);
    }

    /**
     * instantiates an object from each file in a package that matches a specified interface
     *
     * @param packageName the name of the package to instantiate objects from
     * @param interfacePath the path and name of the interface which classes must implement to be instantiated
     * @return a list of objects from the specified package that implement the specified interface
     */
    public Object[] searchPackage(String packageName, String interfacePath)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvalidFormatException {

        ArrayList<Object> objects = new ArrayList<>();
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class).stream().filter(c -> !c.isInterface()).collect(Collectors.toSet());

        if (!checkFormat(interfacePath)) {
            throw new InvalidFormatException("Invalid class path when retrieving all classes. Expected: <package>.<subPackage>.<interfaceName> with any number of subpackages. Actual: " + interfacePath);
        }
        Class<?> myInterface = Class.forName(interfacePath);
        for (Class<?> myClass : allClasses) {
            objects.add(checkClassTypes(myClass, myInterface).getConstructor().newInstance());
        }
        return objects.toArray();
    }

    /**
     * attempt to retrieve the setter for a field in the config file, it it exists
     *
     * @param c the config file to get the setter from
     * @param fieldName the name of the field that we want to find a setter for
     * @return a method for setting the passed fieldName to a new value
     */
    public Method retrieveConfigSetter(Config c, Class type, String fieldName) throws NoSuchMethodException {
        //change the first character of the fieldName to uppercase and prepend 'set' to get setter name
        String methodName = "set" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);

        return c.getClass().getMethod(methodName, type);
    }

    /**
     * Gets and checks the desired class and interface exists and that the class implements the interface.
     * @param classPath The full path and name of the class in the project.
     * @param interfacePath The full path and name of the interface in the project.
     * @return The desired class.
     * @throws InvalidFormatException when the classPath or interfacePath is malformed.
     * @throws InstantiationException when an object of the class cannot be instantiated.
     * @throws ClassNotFoundException when the class or interface doesn't exist.
     */
    private Class<?> loadClassHelper(String classPath, String interfacePath) throws InvalidFormatException, InstantiationException, ClassNotFoundException {
        if (!checkFormat(classPath)) {
            throw new InvalidFormatException("Invalid class path. Expected: <package>.<subPackage>.<className> with any number of subpackages. Actual: " + classPath);
        }
        if (!checkFormat(interfacePath)) {
            throw new InvalidFormatException("Invalid class path. Expected: <package>.<subPackage>.<interfaceName> with any number of subpackages. Actual: " + interfacePath);
        }
        Class<?> myClass = Class.forName(classPath);
        Class<?> myInterface = Class.forName(interfacePath);

        return checkClassTypes(myClass, myInterface);
    }

    /**
     * Checks that a class directly implements an interface and that the class is a class and interface is an interface.
     *
     * @param myClass a class that implements the given interface
     * @param myInterface the interface that myClass implements
     * @return myClass when it's confirmed that myClass implements myInterface
     * @throws InstantiationException when the check fails
     */
    private Class<?> checkClassTypes(Class<?> myClass, Class<?> myInterface) throws InstantiationException {
        if (myClass.isInterface()) {
            throw new InstantiationException(myClass.getSimpleName() + " is a " + myClass.getTypeName() + ". Expected a class.");
        }
        if (!myInterface.isInterface()) {
            throw new InstantiationException(myInterface.getSimpleName() + " is a " + myInterface.getTypeName() + ". Expected an interface.");
        }

        if (myInterface.isAssignableFrom(myClass)) {
            return myClass;
        }
        throw new InstantiationException("Class '" + myClass.getSimpleName() + "' doesn't implement interface '" + myInterface.getSimpleName() + "'.");
    }

    /**
     * Ensures that the path is the correct format.
     * For example, src/main/java/ 'my_package.my_other_package.class_name' is the path with the quoted part the full path used.
     *
     * @param fullClassPath the package source string delimited with periods plus the class name
     * @return true if the format is correct, otherwise false.
     */
    private boolean checkFormat(String fullClassPath) {
        return fullClassPath.matches("([A-Za-z0-9_-]+\\.?)+");
    }
}
