package com.springboot.webflux.springbootwebfluxapirest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.webflux.springbootwebfluxapirest.entity.Categoria;
import com.springboot.webflux.springbootwebfluxapirest.entity.Producto;
import com.springboot.webflux.springbootwebfluxapirest.repository.CategoriaRepository;
import com.springboot.webflux.springbootwebfluxapirest.repository.ProductoRepository;
import com.springboot.webflux.springbootwebfluxapirest.service.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private ProductoRepository productoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	 
	@Override
	public Flux<Producto> findAll() {
		return productoRepository.findAll();
	}

	@Override
	public Mono<Producto> findById(String id) {
		return productoRepository.findById(id);
	}

	@Override
	public Mono<Producto> save(Producto producto) {
		return productoRepository.save(producto);
	}

	@Override
	public Mono<Void> delete(Producto producto) {
		return productoRepository.delete(producto);
	}

	@Override
	public Flux<Categoria> findAllCategoria() {
		return categoriaRepository.findAll();
	}

	@Override
	public Mono<Categoria> findCategoriaById(String id) {
		return categoriaRepository.findById(id);
	}

	@Override
	public Mono<Categoria> saveCategoria(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}
	
	public Mono<Producto> findByNombre(String nombre) {
		return productoRepository.findByNombre(nombre);
	}
	
	public Mono<Categoria> findCategoriaByNombre(String nombre) {
		return categoriaRepository.findByNombre(nombre);
	}

}
