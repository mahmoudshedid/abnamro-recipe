package com.abnamro.recipe.domain.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Util class to load json content from resource files
 */
public class ResourceLoader {

    public static String getStringFromFile(String filePath) throws IOException {
        String ret;
        final InputStream stream = ClassLoader.getSystemResourceAsStream(filePath);
        try {
            ret = convertStreamToString(stream);
        } finally {
            stream.close();
        }
        //Make sure you close all streams.
        return ret;
    }

    @SuppressWarnings("PMD.AssignmentInOperand")
    private static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

}
