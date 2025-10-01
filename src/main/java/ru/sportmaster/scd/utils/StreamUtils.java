package ru.sportmaster.scd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StreamUtils {
    public static String readBytesToString(String path) throws IOException {
        return new String(readBytes(path), StandardCharsets.UTF_8);
    }

    public static byte[] readBytes(String path) throws IOException {
        try (InputStream stream = StreamUtils.class.getResourceAsStream(path)) {
            if (stream != null) {
                return stream.readAllBytes();
            }
        }
        return new byte[0];
    }
}
