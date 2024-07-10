package com.kirillova.gymcrmsystem.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

public class DataLoaderUtil {
    private static final Logger log = LoggerFactory.getLogger(DataLoaderUtil.class);

    public static void loadData(String filePath, Consumer<String[]> lineProcessor) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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
