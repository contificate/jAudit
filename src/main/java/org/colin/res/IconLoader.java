package org.colin.res;

import javax.swing.ImageIcon;
import java.util.HashMap;

/**
 * Icon loader that caches already-loaded icons into an efficient hash-map
 */
public class IconLoader {
    /**
     * HashMap of loaded icons (loaded once used)
     */
    private static HashMap<String, ImageIcon> icons = new HashMap<>();

    /**
     * Load icon resource and cache in hash-map for faster access once loaded
     * @param name name of icon
     * @return (possibly cached) icon
     */
    public static ImageIcon loadIcon(String name) {
        // try to load icon from hash-ap
        ImageIcon icon;
        if((icon = icons.get(name)) == null) { // if icon not in hash-map,
            icon = new ImageIcon(IconLoader.class.getResource("/" + name)); // load the icon
            icons.put(name, icon); // insert into hash-map
        }

        // return icon
        return icon;
    }

}
