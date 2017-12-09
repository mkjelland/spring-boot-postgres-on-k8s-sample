package com.example.postgresdemo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ValueRepository extends CrudRepository<Value, Long>{
    List<Value> findByValue(String value);
}