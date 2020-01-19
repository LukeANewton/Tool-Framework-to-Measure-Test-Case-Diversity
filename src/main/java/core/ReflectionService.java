package core;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

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
     * instantiates an object from each file in a package that matches a specified interface
     *
     * @param packageName the name of the package to instantiate objects from
     * @param interfaceName the name of the interface which classes must implement to be instantiated
     * @return a list of objects from the specified package that implement the specified interface
     */
    public Object[] searchPackage(String packageName, String interfaceName) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException {
        String directoryName = "target/classes/" + packageName.replace('.', '/');

        File directory = new File(directoryName);
        if (!directory.exists())
            return null;

        //look at each file in the directory, and attempt to instantiate each class that implements the given interface
        File[] files = directory.listFiles();
        ArrayList<Object> objects = new ArrayList<Object>();
        for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".class")) {
                    Class c = Class.forName(packageName + '.' +
                            files[i].getName().substring(0, files[i].getName().length() - 6));
                    if(Class.forName(packageName + '.' + interfaceName).isAssignableFrom(c) &&
                            !c.isInterface())
                        objects.add(c.getConstructor().newInstance());

                }
        }
        return objects.toArray();
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
        String regex = "([[A-Za-z0-9_-]]+\\.?)+";
        if (pathToClass.matches(regex)) {
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
