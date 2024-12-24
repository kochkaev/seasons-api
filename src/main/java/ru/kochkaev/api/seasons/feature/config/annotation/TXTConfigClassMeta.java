package ru.kochkaev.api.seasons.feature.config.annotation;

import ru.kochkaev.api.seasons.feature.config.ConfigTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TXTConfigClassMeta {
    String modName();
    String filename() default "config";
    ConfigTypes type();
}
