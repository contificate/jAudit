package org.colin.db;

import java.util.HashMap;

/**
 * Global registry using for mapping constant keys to String values.
 * Could potentially use Generics for this class in future.
 */
public class GlobalRegistry {
    /**
     * Singleton instance of the registry.
     */
    private static GlobalRegistry instance;

    /**
     * Internal data structure used to store mappings.
     */
    private HashMap<GlobalConstants, String> data;

    /**
     * Get the instance of the registry
     *
     * @return singleton instance
     */
    public static GlobalRegistry getInstance() {
        // if no instance, create an instance
        if (instance == null)
            instance = new GlobalRegistry();

        // return the instance
        return instance;
    }

    /**
     * Insert a mapping into the registry
     *
     * @param key   constant key
     * @param value string value mapped to key
     */
    public void put(GlobalConstants key, String value) {
        data.put(key, value);
    }

    /**
     * Remove a string entry for a key
     *
     * @param key key to remove string mapping for
     */
    public void remove(GlobalConstants key) {
        data.remove(key);
    }

    /**
     * Get the value mapped for key
     *
     * @param key key for which value must be found
     * @return mapped string value
     */
    public String get(final GlobalConstants key) {
        return data.get(key);
    }

    /**
     * Private constructor, initialises internal data structure.
     */
    private GlobalRegistry() {
        data = new HashMap<>();
    }

}
