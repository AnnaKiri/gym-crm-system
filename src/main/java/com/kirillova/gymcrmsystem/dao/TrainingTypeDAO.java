package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.config.AppConfig;
import com.kirillova.gymcrmsystem.models.TrainingType;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class TrainingTypeDAO implements InitializingBean {
    private static final Logger log = getLogger(TrainingTypeDAO.class);
    private final Map<Long, TrainingType> trainingTypeStorage;
    private final AtomicLong index = new AtomicLong(0L);

    private final AppConfig appConfig;

    @Autowired
    public TrainingTypeDAO(Map<Long, TrainingType> trainingTypeStorage, AppConfig appConfig) {
        this.trainingTypeStorage = trainingTypeStorage;
        this.appConfig = appConfig;
    }

    public TrainingType save(TrainingType trainingType) {
        long newId = index.incrementAndGet();
        trainingType.setId(newId);
        trainingTypeStorage.put(newId, trainingType);
        log.debug("New training tyre with id = " + newId + " saved");
        return trainingType;
    }

    @Override
    public void afterPropertiesSet() {
        try (BufferedReader reader = new BufferedReader(new FileReader(appConfig.getTrainingTypeDataPath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                TrainingType trainingType = new TrainingType();
                trainingType.setName(line);
                save(trainingType);
            }
        } catch (IOException e) {
            log.debug("Error reading training type init data file");
        }
    }
}

