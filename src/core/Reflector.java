package core;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Reflector {

    private String classSource;

    public Reflector() {
    }

    public Reflector(String source) {
        classSource = source;
    }

    /**
     * Loads a class with a no-args constructor.
     * TODO: Use varargs instead of Object[] and destructure the args when calling a constructor like what can be done
     *          in javascript ES6. Java doesn't currently support this.
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
     * @param classPath the path of the class to load relative to the root of the project.
     * @param constructorArgs an array of parameters that a loaded class may need passed into its constructor.
     * @return an instance of the loaded class.
     */
    public Object loadClass(String classPath, Object[] constructorArgs)
            throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> myClass = Class.forName(classSource + classPath);
        Constructor<?> constructor = myClass.getConstructor(Object.class);
        return constructor.newInstance(constructorArgs);
    }

    public String getClassSource() {
        return classSource;
    }

    public void setClassSource(String classSource) {
        this.classSource = classSource;
    }
}
