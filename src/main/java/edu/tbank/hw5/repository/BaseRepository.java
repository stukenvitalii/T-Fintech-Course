package edu.tbank.hw5.repository;

import edu.tbank.hw5.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseRepository<ID, T> {
    protected final Storage<ID, T> storage;

    public List<T> findAll() {
        log.info("Fetching all entities");
        return storage.getAll();
    }

    public T findById(ID id) {
        log.info("Fetching entity with id: {}", id);
        return storage.get(id);
    }

    public void save(T entity, ID id) {
        log.info("Saving entity with id: {}", id);
        storage.put(id, entity);
    }

    public void deleteById(ID id) {
        log.info("Deleting entity with id: {}", id);
        storage.remove(id);
    }

    public void saveAll(List<T> entities, List<ID> ids) {
        log.info("Saving all entities");
        for (int i = 0; i < entities.size(); i++) {
            storage.put(ids.get(i), entities.get(i));
        }
    }

    public void update(T entity, ID id) {
        log.info("Updating entity with id: {}", id);
        storage.put(id, entity);
    }
}