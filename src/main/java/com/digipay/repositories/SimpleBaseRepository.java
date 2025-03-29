package com.digipay.repositories;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimpleBaseRepository<T, ID> implements BaseRepository<T, ID> {

    private final Map<ID, T> store = new ConcurrentHashMap<>();

    @Override
    public T save(T entity) {
        try {
            ID id = extractId(entity);
            store.put(id, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract ID", e);
        }
    }

    @Override
    public void saveAll(List<T> entities) {
        for (T entity : entities) {
            save(entity);
        }
    }

    @Override
    public T getById(ID id) {
        return store.get(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @SuppressWarnings("unchecked")
    private ID extractId(T entity) throws Exception {
        return (ID) entity.getClass().getMethod("getId").invoke(entity);
    }
}
