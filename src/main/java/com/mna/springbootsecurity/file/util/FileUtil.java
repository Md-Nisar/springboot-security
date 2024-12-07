package com.mna.springbootsecurity.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class FileUtil {

    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");

    public static InputStream fileToInputStream(File file) throws IOException {
        return new FileInputStream(file);
    }

    public static boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        return FILE_NAME_PATTERN.matcher(fileName).matches();
    }

    public static boolean isValidFile(File file) {
        return isValidFileName(file.getName());
    }
}
