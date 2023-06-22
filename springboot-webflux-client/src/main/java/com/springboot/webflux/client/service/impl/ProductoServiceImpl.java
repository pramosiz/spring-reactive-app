package com.springboot.webflux.client.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.springboot.webflux.client.dto.ProductoDTO;
import com.springboot.webflux.client.service.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private WebClient client;
	
	@Override
	public Flux<ProductoDTO> findAll() {
		return client.get()
				.retrieve()
				.bodyToFlux(ProductoDTO.class);
	}

	@Override
	public Mono<ProductoDTO> findById(String id) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return client.get()
				.uri("/{id}", params)
				.retrieve()
				.bodyToMono(ProductoDTO.class);
	}

	@Override
	public Mono<ProductoDTO> save(ProductoDTO productoDTO) {
		return client.post()
				.body(BodyInserters.fromValue(productoDTO))
				.retrieve()
				.bodyToMono(ProductoDTO.class);
	}

	@Override
	public Mono<ProductoDTO> update(ProductoDTO productoDTO, String id) {
		return client.put()
				.uri("/{id}", Collections.singletonMap("id", id))
				.body(BodyInserters.fromValue(productoDTO))
				.retrieve()
				.bodyToMono(ProductoDTO.class);
	}

	@Override
	public Mono<Void> delete(String id) {
		return client.delete()
				.uri("/{id}", Collections.singletonMap("id", id))
				.retrieve()
				.bodyToMono(Void.class);
	}

}
