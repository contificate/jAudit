package org.colin.util;

import java.awt.*;

/**
 * Generic class for utility methods related to {@link Color}.
 */
public class ColourUtil {

    /**
     * Get {@link Color} from hex string describing colour.
     *
     * @param hexColour hex colour value (potentially prepended with <b>#</b>) e.g. "#ff0000" (red)
     * @return {@link Color} colour instance
     */
    public static Color fromHex(String hexColour) {
        // if prepended with "#", skip it
        if (hexColour.startsWith("#"))
            hexColour = hexColour.substring(1);

        // base 16
        final int base = 0x10;
        return new Color(
                Integer.valueOf(hexColour.substring(0, 2), base),
                Integer.valueOf(hexColour.substring(2, 4), base),
                Integer.valueOf(hexColour.substring(4, 6), base));
    }

}
