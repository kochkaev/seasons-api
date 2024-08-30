package ru.kochkaev.api.seasons.util.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Map1Key3Values<K, F, S, T> {

    protected final Map<K,ValuesObject> map;

    protected Map1Key3Values(Map<K, ValuesObject> map) {
        this.map = map;
    }

    public void put(K key, F firstValue, S secondValue, T thirdValue) {
        map.put(key, new ValuesObject(firstValue, secondValue, thirdValue));
    }
    public void putFrom(Map1Key3Values<K, F, S, T> from) {
        map.putAll(from.map);
    }
    public void remove(K key) {
        map.remove(key);
    }

    public F getFirst(K key) {
        return map.get(key).getFirst();
    }
    public S getSecond(K key) {
        return map.get(key).getSecond();
    }
    public T getThird(K key) {
        return map.get(key).getThird();
    }

    public void setFirst(K key, F firstValue) {
        map.get(key).setFirst(firstValue);
    }
    public void setSecond(K key, S secondValue) {
        map.get(key).setSecond(secondValue);
    }
    public void setThird(K key, T thirdValue) {
        map.get(key).setThird(thirdValue);
    }

    public Set<K> getKeySet() {
        return map.keySet();
    }
    public Set<F> getFirstValuesSet() {
        return map.values().stream().map(ValuesObject::getFirst).collect(Collectors.toSet());
    }
    public Set<S> getSecondValuesSet() {
        return map.values().stream().map(ValuesObject::getSecond).collect(Collectors.toSet());
    }
    public Set<T> getThirdValuesSet() {
        return map.values().stream().map(ValuesObject::getThird).collect(Collectors.toSet());
    }

    public void clear() {
        map.clear();
    }

    public Map1Key3Values<K, F, S, T> copy() {
        Map1Key3Values<K, F, S, T> copy = new Map1Key3Values<>(map instanceof HashMap ? new HashMap<>() : new TreeMap<>());
        for (K key : map.keySet()) {
            copy.put(key, map.get(key).getFirst(), map.get(key).getSecond(), map.get(key).getThird());
        }
        return copy;
    }

    protected class ValuesObject {
        private F first;
        private S second;
        private T third;
        public ValuesObject(F first, S second, T third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
        public F getFirst() {
            return first;
        }
        public void setFirst(F first) {
            this.first = first;
        }
        public S getSecond() {
            return second;
        }
        public void setSecond(S second) {
            this.second = second;
        }
        public T getThird() {
            return third;
        }
        public void setThird(T third) {
            this.third = third;
        }
    }
    public static class HashMap1Key3Values<K, F, S, T> extends Map1Key3Values<K, F, S, T> {
        public HashMap1Key3Values() {
            super(new HashMap<>());
        }
    }
    public static class TreeMap1Key3Values<K, F, S, T> extends Map1Key3Values<K, F, S, T> {
        public TreeMap1Key3Values() {
            super(new TreeMap<>());
        }
    }

}
