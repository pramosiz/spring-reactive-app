package com.springboot.webflux.springbootwebfluxapirest.service;

import com.springboot.webflux.springbootwebfluxapirest.entity.Categoria;
import com.springboot.webflux.springbootwebfluxapirest.entity.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

	public Flux<Producto> findAll();
	
	public Mono<Producto> findById(String id);
	
	public Mono<Producto> save(Producto producto);
	
	public Mono<Void> delete(Producto producto);
	
	public Flux<Categoria> findAllCategoria();
	
	public Mono<Categoria> findCategoriaById(String id);
	
	public Mono<Categoria> saveCategoria(Categoria categoria);
	
}
