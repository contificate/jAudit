package org.colin.util;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import javafx.geometry.Pos;

public class RangeFactory {

    public static Range defaultRange() {
        return Range.range(0, 0, 0, 0);
    }

}
