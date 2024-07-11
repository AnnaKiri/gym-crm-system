package com.kirillova.gymcrmsystem.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class DataLoaderUtil {
    private static final Logger log = LoggerFactory.getLogger(DataLoaderUtil.class);

    public static void loadData(String filePath, Consumer<String[]> lineProcessor) {
        ClassLoader classLoader = DataLoaderUtil.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                lineProcessor.accept(parts);
            }
        } catch (IOException e) {
            log.debug("Error reading data file: " + filePath, e);
        }
    }
}
