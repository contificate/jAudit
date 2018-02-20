package org.colin.res;

import javax.swing.*;
import java.util.HashMap;

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
        ImageIcon icon;
        if((icon = icons.get(name)) == null) {
            icon = new ImageIcon(IconLoader.class.getResource("/" + name));
            icons.put(name, icon);
        }

        return icon;
    }

}
