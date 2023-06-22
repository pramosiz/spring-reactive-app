package com.springboot.webflux.springbootwebfluxapirest.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.webflux.springbootwebfluxapirest.entity.Producto;

import reactor.core.publisher.Mono;

@Repository
public interface ProductoRepository extends ReactiveMongoRepository<Producto, String> {

	public Mono<Producto> findByNombre(String nombre);
}
