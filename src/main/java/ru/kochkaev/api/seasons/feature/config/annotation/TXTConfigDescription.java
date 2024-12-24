package ru.kochkaev.api.seasons.feature.config.annotation;

import java.lang.annotation.*;

@Repeatable(TXTConfigDescriptions.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TXTConfigDescription {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface TXTConfigDescriptions {
    TXTConfigDescription[] value();
}