package com.springboot.webflux.springbootwebflux.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.springboot.webflux.springbootwebflux.entity.Producto;

public interface ProductoDao extends ReactiveMongoRepository<Producto, ObjectId> {

}
