package com.kirillova.gymcrmsystem.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

@Slf4j
public class DataLoaderUtil {

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
