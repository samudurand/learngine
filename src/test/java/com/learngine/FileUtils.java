package com.learngine;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static String readFile(String filePath) throws IOException {
        return IOUtils.toString(
                FileUtils.class.getResourceAsStream(filePath),
                StandardCharsets.UTF_8
        );
    }
}
