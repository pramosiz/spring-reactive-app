package com.springboot.webflux.springbootwebflux.controller;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.webflux.springbootwebflux.dao.ProductoDao;
import com.springboot.webflux.springbootwebflux.entity.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

	@Autowired
	private ProductoDao productoDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductoRestController.class);
	
	@GetMapping
	public Flux<Producto> index() {
		
		return productoDao
				.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				})
				.doOnNext(producto -> LOGGER.info("{}", producto.getNombre()));
	}
	
//	@GetMapping("/{id}")
//	public Mono<Producto> show(@PathVariable String id) {
//		
//		Mono<Producto> producto = productoDao.findById(new ObjectId(id));
//		return producto;
//	}
	
	@GetMapping("/{id}")
	public Mono<Producto> show(@PathVariable String id) {
		
		Flux<Producto> productos = productoDao.findAll();
		return productos.filter(p -> p.getId().equals(new ObjectId(id)))
				.next()		// Convierte de Flux a Mono
				.doOnNext(producto -> LOGGER.info("{}", producto.getNombre()));
	}
	
}
