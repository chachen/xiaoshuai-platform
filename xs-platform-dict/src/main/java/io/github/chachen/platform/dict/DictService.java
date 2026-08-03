package io.github.chachen.platform.dict;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DictService {
    private final Map<String, List<DictItem>> values = new ConcurrentHashMap<>();

    public List<DictItem> get(String type) {
        return values.getOrDefault(type, List.of()).stream().filter(DictItem::enabled).sorted(Comparator.comparing(DictItem::sort)).toList();
    }

    public void put(String type, List<DictItem> items) {
        values.put(type, List.copyOf(items));
    }

    public void refresh(String type) {
        if (type != null) values.remove(type);
        else values.clear();
    }
}
