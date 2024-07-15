package ru.kochkaev.Seasons4Fabric.util.functional;

import java.util.List;

@FunctionalInterface
public interface IFuncRet {
    List<Object> function(List<Object> args);
}
