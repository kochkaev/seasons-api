package ru.kochkaev.api.seasons.feature.config.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TXTConfigAutoAddMeta {
    boolean addType() default true;
    boolean addDefault() default true;
}
