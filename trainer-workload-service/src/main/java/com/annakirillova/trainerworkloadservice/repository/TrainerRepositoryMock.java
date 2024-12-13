package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.trainerworkloadservice.model.TrainerSummary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
@Profile("dev")
public class TrainerRepositoryMock implements TrainerRepository {

    @Override
    public Optional<TrainerSummary> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public <S extends TrainerSummary> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends TrainerSummary> List<S> insert(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public <S extends TrainerSummary> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends TrainerSummary> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends TrainerSummary> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends TrainerSummary> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends TrainerSummary> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends TrainerSummary> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends TrainerSummary, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends TrainerSummary> S save(S entity) {
        return null;
    }

    @Override
    public <S extends TrainerSummary> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<TrainerSummary> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<TrainerSummary> findAll() {
        return List.of();
    }

    @Override
    public List<TrainerSummary> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(TrainerSummary entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends TrainerSummary> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<TrainerSummary> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<TrainerSummary> findAll(Pageable pageable) {
        return null;
    }
}
