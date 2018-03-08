package org.colin.util;

import java.awt.*;

public class ColourUtil {

    public static Color fromHex(String hexColour) {
        if (hexColour.startsWith("#"))
            hexColour = hexColour.substring(1);

        final int base = 0x10;
        return new Color(
                Integer.valueOf(hexColour.substring(0, 2), base),
                Integer.valueOf(hexColour.substring(2, 4), base),
                Integer.valueOf(hexColour.substring(4, 6), base));
    }

}
