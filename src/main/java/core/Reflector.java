package core;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

//TODO: be able to take a file path url that leads to the class
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
     * @param className the path of the class to load relative to the root of the project.
     * @return an instance of the loaded class.
     */
    public Object loadClass(String className)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> myClass = Class.forName(classSource + className);
        System.out.println("Successfully loaded " + myClass.getName());
        return myClass.newInstance();
    }

    /**
     * Loads a class with a constructor that takes one Object argument.
     * TODO: Use varargs instead of Object[] and destructure the args when calling a constructor like what can be done
     *          in javascript ES6. Java doesn't currently support this.
     * @param className the path of the class to load relative to the root of the project.
     * @param constructorArgs an array of parameters that a loaded class may need passed into its constructor.
     * @return an instance of the loaded class.
     */
    public Object loadClass(String className, Object[] constructorArgs)
            throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> myClass = Class.forName(classSource + className);
        Constructor<?> constructor = myClass.getConstructor(Object.class);
        return constructor.newInstance(constructorArgs);
    }

    /**
     * Loads a class at the given root directory
     * @param systemRootPath the folder the class is in
     * @param className the class name
     * @return an instance of the class
     * @throws MalformedURLException when the url isn't formatted properly
     * @throws ClassNotFoundException when the class doesn't exist
     * @throws IllegalAccessException when there isn't sufficient access privilege to the class
     * @throws InstantiationException when the class fails to be instantiated
     */
    public Object loadClass(String systemRootPath, String className)
            throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        File file = new File(systemRootPath);

        // Convert File to a URL
        // C:/myclasses -> file:/C:/myclasses/ for example
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};
        ClassLoader cl = new URLClassLoader(urls);
        Class cls = cl.loadClass(className);
        return cls.newInstance();
    }

    /**
     * Ensures that the source ends with a period for proper formatting the path
     * @param source the package source string delimited with periods
     * @return the new source string
     */
    private String endsWithPeriod(String source) {
        if ((source.substring(source.length() - 1)).equals(".")) {
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
