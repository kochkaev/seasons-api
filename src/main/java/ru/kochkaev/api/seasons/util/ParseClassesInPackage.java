package ru.kochkaev.api.seasons.util;

import com.google.common.reflect.ClassPath;
import ru.kochkaev.api.seasons.util.functional.IFuncObjectReg;

import java.util.ArrayList;
import java.util.List;

public class ParseClassesInPackage {

    public static List<Object> getAllClassesInPackage(String packageName) {
        try {
            ClassPath cp = ClassPath.from(ParseClassesInPackage.class.getClassLoader());
            List<Object> objects = new ArrayList<>();
            for (ClassPath.ClassInfo classInfo : cp.getTopLevelClassesRecursive(packageName)) {
                Class<?> clazz = Class.forName(classInfo.getName());
                objects.add(clazz.getDeclaredConstructor().newInstance());
//                if (object.getClass().getSuperclass() == superClass);
                //regMethod.function(superClass.cast(object));
            }
            return objects;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
