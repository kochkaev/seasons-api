package ru.kochkaev.Seasons4Fabric.util;

import com.google.common.reflect.ClassPath;
import ru.kochkaev.Seasons4Fabric.util.functional.IFuncObjectReg;

public class ParseClassesInPackage<O> {

    public ParseClassesInPackage(String packageName, Class<O> superClass, IFuncObjectReg<O> regMethod) {
        try {
            ClassPath cp = ClassPath.from(ParseClassesInPackage.class.getClassLoader());
            for (ClassPath.ClassInfo classInfo : cp.getTopLevelClassesRecursive(packageName)) {
                Class<?> clazz = Class.forName(classInfo.getName());
                Object object = clazz.getDeclaredConstructor().newInstance();
//                if (object.getClass().getSuperclass() == superClass);
                regMethod.function(superClass.cast(object));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
