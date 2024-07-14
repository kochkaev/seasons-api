package ru.kochkaev.Seasons4Fabric.util;

import com.google.common.reflect.ClassPath;

import java.util.function.Consumer;

public class ParseClassesInPackage<O> {

    public ParseClassesInPackage(String packageName, Consumer<Object> regMethod) {
        try {
            ClassPath cp = ClassPath.from(ParseClassesInPackage.class.getClassLoader());
            for (ClassPath.ClassInfo classInfo : cp.getTopLevelClassesRecursive(packageName)) {
                Class<?> clazz = Class.forName(classInfo.getName());
                try {
                    //Class<O> obj = new Class<O>().newInstance();
                    //if (clazz.isAssignableFrom(obj.getClass())) {

//                    O object = (O) clazz.getDeclaredConstructor().newInstance();
                    regMethod.accept(clazz.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
