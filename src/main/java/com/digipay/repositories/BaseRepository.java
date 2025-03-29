package com.digipay.repositories;

import java.util.List;

public interface BaseRepository<T, ID> {
    T save(T entity);
    void saveAll(List<T> entities);
    T getById(ID id);
    List<T> findAll();
}
