package ru.kochkaev.api.seasons.provider.config.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TXTConfigAutoAddMeta {
    boolean addType() default true;
    boolean addDefault() default true;
}
