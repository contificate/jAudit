package org.colin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.CRC32;

/**
 * General file utilities
 */
public class FileUtil {

    /**
     * Calculate <a href="https://en.wikipedia.org/wiki/Cyclic_redundancy_check#CRC-32_algorithm">CRC-32</a> checksum for a file.<br>
     *
     * @param file file for which a checksum will be calculated
     * @return crc32 checksum
     * @throws IOException exception thrown if file fails to get mapped
     */
    public static long calculateCRC32(final File file) throws IOException {
        // initialise CRC32 object
        CRC32 crc = new CRC32();
        // get file channel
        FileChannel channel = new FileInputStream(file).getChannel();
        // map file into memory
        MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        // run CRC-32 on mapped buffer
        crc.update(buf);
        // return calculated checksum
        return crc.getValue();
    }

}
