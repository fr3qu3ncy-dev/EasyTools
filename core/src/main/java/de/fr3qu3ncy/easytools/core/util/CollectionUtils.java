package de.fr3qu3ncy.easytools.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollectionUtils {

    private CollectionUtils() {}

    public static <K, V> void addToListInMap(Map<K, List<V>> map, K key, V value) {
        List<V> list = map.getOrDefault(key, new ArrayList<>());
        list.add(value);
        map.put(key, list);
    }

    public static <K, V> void removeFromListInMap(Map<K, List<V>> map, K key, V value) {
        List<V> list = map.getOrDefault(key, new ArrayList<>());
        list.remove(value);
        map.put(key, list);
    }
}
