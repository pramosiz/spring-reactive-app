package com.springboot.webflux.springbootwebflux.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.webflux.springbootwebflux.entity.Producto;

@Repository
public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {

}
