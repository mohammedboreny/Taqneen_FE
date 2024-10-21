package com.example.demo.services;

import reactor.core.publisher.Flux;

public interface ReactiveCrudService<T, ID> {

    Flux<T> getAll();             // Retrieve all items
    Flux<T> getById(ID id);       // Retrieve a single item by ID
    Flux<T> create(T entity);     // Create a new item
    Flux<T> update(ID id, T entity);  // Update an item
    Flux<?> delete(ID id);     // Delete an item
}
