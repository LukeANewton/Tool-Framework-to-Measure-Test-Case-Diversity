package reflector;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Reflector<C> {

    /**
     * Loads a class with a no-args constructor.
     * TODO: Use varargs instead of Object[] and destructure the args when calling a constructor like what can be done
     *          in javascript ES6. Java doesn't currently support this.
     * @param classPath the path of the class to load relative to the root of the project.
     * @return an instance of the loaded class.
     */
    public Object loadClass(String classPath)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> myClass = Class.forName(classPath);
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
        Class<?> myClass = Class.forName(classPath);
        Constructor<?> constructor = myClass.getConstructor(Object.class);
        return constructor.newInstance(constructorArgs);
    }


//    public C LoadClass(String directory, String classpath, Class<C> parentClass) throws ClassNotFoundException {
//        File pluginsDir = new File(System.getProperty("user.dir") + directory);
//        for (File jar : pluginsDir.listFiles()) {
//            try {
//                ClassLoader loader = URLClassLoader.newInstance(
//                        new URL[] { jar.toURL() },
//                        getClass().getClassLoader()
//                );
//                Class<?> clazz = Class.forName(classpath, true, loader);
//                Class<? extends C> newClass = clazz.asSubclass(parentClass);
//                // Apparently its bad to use Class.newInstance, so we use
//                // newClass.getConstructor() instead
//                Constructor<? extends C> constructor = newClass.getConstructor();
//                return constructor.newInstance();
//
//            } catch (ClassNotFoundException e) {
//                // There might be multiple JARs in the directory,
//                // so keep looking
//                continue;
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
//        throw new ClassNotFoundException("Class " + classpath
//                + " wasn't found in directory " + System.getProperty("user.dir") + directory);
//    }

}
