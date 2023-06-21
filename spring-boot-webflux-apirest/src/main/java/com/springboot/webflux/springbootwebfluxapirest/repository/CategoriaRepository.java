package com.springboot.webflux.springbootwebfluxapirest.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.webflux.springbootwebfluxapirest.entity.Categoria;

import reactor.core.publisher.Mono;

@Repository
public interface CategoriaRepository extends ReactiveMongoRepository<Categoria, String>{

	public Mono<Categoria> findByNombre(String nombre);
}
