package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.config.ConfigurationProperties;
import com.kirillova.gymcrmsystem.models.TrainingType;
import com.kirillova.gymcrmsystem.util.DataLoaderUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class TrainingTypeDAO implements InitializingBean {
    private static final Logger log = getLogger(TrainingTypeDAO.class);
    private final Map<Long, TrainingType> trainingTypeStorage;
    private final AtomicLong index = new AtomicLong(0L);

    private final ConfigurationProperties configurationProperties;

    @Autowired
    public TrainingTypeDAO(Map<Long, TrainingType> trainingTypeStorage, ConfigurationProperties configurationProperties) {
        this.trainingTypeStorage = trainingTypeStorage;
        this.configurationProperties = configurationProperties;
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
        DataLoaderUtil.loadData(configurationProperties.getTrainingTypeDataPath(), parts -> {
            // name
            save(new TrainingType(0, parts[0]));
        });
    }
}

