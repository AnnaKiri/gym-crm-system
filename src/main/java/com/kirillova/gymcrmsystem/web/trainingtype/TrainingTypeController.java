package com.kirillova.gymcrmsystem.web.trainingtype;

import com.kirillova.gymcrmsystem.dao.TrainingTypeDAO;
import com.kirillova.gymcrmsystem.models.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = TrainingTypeController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainingTypeController {

    static final String REST_URL = "/training_type";

    @Autowired
    protected TrainingTypeDAO trainingTypeDAO;

    @GetMapping("/{id}")
    public List<TrainingType> get(@PathVariable int id) {
        log.debug("Get training types");
        return trainingTypeDAO.get();
    }
}
