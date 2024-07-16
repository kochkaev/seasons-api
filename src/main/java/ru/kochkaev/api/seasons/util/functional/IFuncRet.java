package ru.kochkaev.api.seasons.util.functional;

import java.util.List;

@FunctionalInterface
public interface IFuncRet {
    List<Object> function(List<Object> args);
}
