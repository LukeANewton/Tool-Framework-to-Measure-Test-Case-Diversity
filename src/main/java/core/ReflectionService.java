package core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
     * Gets and checks the desired class and interface exists and that the class implements the interface.
     *
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

        if (myInterface.isAssignableFrom(myClass)) {
            return myClass;
        }
        throw new InstantiationException("Class '" + classPath + "' doesn't implement interface '" + interfacePath + "'.");
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
