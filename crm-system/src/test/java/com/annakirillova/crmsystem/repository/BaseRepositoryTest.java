package com.annakirillova.crmsystem.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("unit-tests")
public abstract class BaseRepositoryTest {

    @PersistenceContext
    protected EntityManager entityManager;
}
