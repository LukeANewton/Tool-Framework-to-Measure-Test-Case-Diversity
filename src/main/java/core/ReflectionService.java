package core;

import model.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
    public Object loadClass(String classPath, String interfacePath, Class<?>[] initArgsClasses, Object[] constructorArgs)
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
    public Object[] searchPackage(String packageName, String interfacePath) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException, IOException, URISyntaxException {
        if(Controller.class.getResource("Controller.class").toString().startsWith("jar")) {
            ArrayList<Object> objects = new ArrayList<>();
            ZipInputStream zip = new ZipInputStream(new FileInputStream(new File(ReflectionService.class.getProtectionDomain().getCodeSource().getLocation().toURI())));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class") && entry.getName().startsWith(packageName.replace('.', '/'))) {
                    // This ZipEntry represents a class. Now, what class does it represent?
                    String className = entry.getName().replace('/', '.');
                    Class<?> c = Class.forName(className.substring(0, className.length() - ".class".length()));
                    if(Class.forName(interfacePath).isAssignableFrom(c) &&
                            !c.isInterface()) //if this is true, the class in an implementation of the required interface
                        objects.add(c.getConstructor().newInstance());
                }
            }
            return objects.toArray();
        }
        String directoryName = "target/classes/" + packageName.replace('.', '/');

        File directory = new File(directoryName);
        if (!directory.exists())
            return null;

        //look at each file in the directory, and attempt to instantiate each class that implements the given interface
        File[] files = directory.listFiles();
        ArrayList<Object> objects = new ArrayList<>();
        for (File file : Objects.requireNonNull(files)) {
            if (file.getName().endsWith(".class")) {
                Class<?> c = Class.forName(packageName +
                        file.getName().substring(0, file.getName().length() - 6));
                if (Class.forName(packageName + interfacePath).isAssignableFrom(c) &&
                        !c.isInterface())
                    objects.add(c.getConstructor().newInstance());
            }
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
    public Method retrieveConfigSetter(Config c, Class<?> type, String fieldName) throws NoSuchMethodException {
        //change the first character of the fieldName to uppercase and prepend 'set' to get setter name
        String methodName = "set" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);

        return c.getClass().getMethod(methodName, type);
    }

    /**
     * Gets and checks the desired class and interface exists and that the class implements the interface.
     * @param classPath The full path and name of the class in the project.
     * @param interfacePath The full path and name of the interface in the project (Can be null or empty to bypass the interface check).
     * @return The desired class.
     * @throws InvalidFormatException when the classPath or interfacePath is malformed.
     * @throws InstantiationException when an object of the class cannot be instantiated.
     * @throws ClassNotFoundException when the class or interface doesn't exist.
     */
    private Class<?> loadClassHelper(String classPath, String interfacePath) throws InvalidFormatException, InstantiationException, ClassNotFoundException {
        if (!checkFormat(classPath)) {
            throw new InvalidFormatException("Invalid class path. Expected: <package>.<subPackage>.<className> with any number of subpackages. Actual: " + classPath);
        }
        Class<?> myClass = Class.forName(classPath);

        if (interfacePath == null || interfacePath.isEmpty()) {
            return myClass;
        }
        if (!checkFormat(interfacePath)) {
            throw new InvalidFormatException("Invalid interface path. Expected: <package>.<subPackage>.<interfaceName> with any number of subpackages. Actual: " + interfacePath);
        }
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
