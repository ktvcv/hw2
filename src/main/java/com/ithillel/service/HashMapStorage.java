package com.ithillel.service;

import com.ithillel.interfaces.CustomBean;
import com.ithillel.interfaces.Storage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;


@CustomBean(name = "storage", args = "")
public class HashMapStorage implements Storage {

    private final Map<String, String> map = new HashMap<>();

    @Override
    public void put(String key, String value) {
        map.put(key, value);
    }

    @Override
    public String get(String key) {
        return map.get(key);
    }

    @Override
    public String compute(String key, BiFunction remappingFunction) {
        return map.compute(key, remappingFunction);
    }
}
