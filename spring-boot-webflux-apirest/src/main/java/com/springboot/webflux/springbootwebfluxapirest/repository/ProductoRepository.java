package com.springboot.webflux.springbootwebfluxapirest.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.webflux.springbootwebfluxapirest.entity.Producto;

@Repository
public interface ProductoRepository extends ReactiveMongoRepository<Producto, String> {

}
