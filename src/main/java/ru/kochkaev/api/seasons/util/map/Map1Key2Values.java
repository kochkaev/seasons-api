package ru.kochkaev.api.seasons.util.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Map1Key2Values <K, F, S> {

    private final Map<K,ValuesObject> map = new HashMap<>();

    public Map1Key2Values() {

    }

    public void put(K key, F firstValue, S secondValue) {
        map.put(key, new ValuesObject(firstValue, secondValue));
    }
    public void putFrom(Map1Key2Values<K, F, S> from) {
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

    public void setFirst(K key, F firstValue) {
        map.get(key).setFirst(firstValue);
    }
    public void setSecond(K key, S secondValue) {
        map.get(key).setSecond(secondValue);
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

    public void clear() {
        map.clear();
    }

    public Map1Key2Values<K, F, S> copy() {
        Map1Key2Values<K, F, S> copy = new Map1Key2Values<>();
        for (K key : map.keySet()) {
            copy.put(key, map.get(key).getFirst(), map.get(key).getSecond());
        }
        return copy;
    }

    private class ValuesObject {
        private F first;
        private S second;
        public ValuesObject(F first, S second) {
            this.first = first;
            this.second = second;
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
    }

}
