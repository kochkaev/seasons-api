package ru.kochkaev.api.seasons.util;

import com.google.common.reflect.ClassPath;
import ru.kochkaev.api.seasons.util.functional.IFuncObjectReg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class ParseClassesInPackage {

//    public static List<Object> getAllClassesInPackage(String packageName) {
//        try {
//            ClassPath cp = ClassPath.from(ParseClassesInPackage.class.getClassLoader());
//            List<Object> objects = new ArrayList<>();
//            for (ClassPath.ClassInfo classInfo : cp.getTopLevelClassesRecursive(packageName)) {
//                Class<?> clazz = Class.forName(classInfo.getName());
//                objects.add(clazz.getDeclaredConstructor().newInstance());
////                if (object.getClass().getSuperclass() == superClass);
//                //regMethod.function(superClass.cast(object));
//            }
//            return objects;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ArrayList<>();
//    }

    public static Set<Class<?>> getAllClassesInPackage(String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                if (directory.exists()) {
                    findClassesInDirectory(classes, directory, packageName);
                }
                else {
                    JarURLConnection connection = (JarURLConnection) resource.openConnection();
                    File file = new File(connection.getJarFileURL().toURI());
                    if (file.getName().endsWith(".jar")) try (JarFile jar = new JarFile(file)) {
                        getClassesInJar(classes, jar, packageName);
                    }
                }
//                InputStream jarStream = jar.getInputStream(jar.getEntry(path));
//                JarEntry jarStream = jar.getJarEntry(path);

            }
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    private static void findClassesInDirectory(Set<Class<?>> classes, File directory, String packageName) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findClassesInDirectory(classes, file, packageName + "." + file.getName());
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (!Modifier.isAbstract(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers())) {
                            classes.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        //noinspection CallToPrintStackTrace
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void getClassesInJar(Set<Class<?>> classes, JarFile jarFile, String packageName) throws IOException {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }
            if (name.startsWith(packageName.replace(".", "/")) && name.endsWith(".class")) {
                String className = name.replace("/", ".").substring(0, name.length() - 6);
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    //noinspection CallToPrintStackTrace
                    e.printStackTrace();
                }
            }
        }
    }
}

