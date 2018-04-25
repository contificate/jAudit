package org.colin.db;

import java.util.HashMap;

public class GlobalRegistry {
    private static GlobalRegistry instance;
    private HashMap<GlobalConstants, String> data;

    public static GlobalRegistry getInstance() {
        if (instance == null)
            instance = new GlobalRegistry();

        return instance;
    }

    public void put(GlobalConstants key, String value) {
        data.put(key, value);
    }

    public void remove(GlobalConstants key) {
        data.remove(key);
    }

    public String get(final GlobalConstants key) {
        return data.get(key);
    }

    private GlobalRegistry() {
        data = new HashMap<>();
    }

}
