package com.springboot.webflux.client.service;

import com.springboot.webflux.client.dto.ProductoDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

	public Flux<ProductoDTO> findAll();
	
	public Mono<ProductoDTO> findById(String id);
	
	public Mono<ProductoDTO> save(ProductoDTO productoDTO);
	
	public Mono<ProductoDTO> update(ProductoDTO productoDTO, String id);
	
	public Mono<Void> delete(String id);
}
