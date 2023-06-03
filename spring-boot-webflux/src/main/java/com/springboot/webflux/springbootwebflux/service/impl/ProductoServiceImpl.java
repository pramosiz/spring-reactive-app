package com.springboot.webflux.springbootwebflux.service.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.webflux.springbootwebflux.dao.CategoriaDao;
import com.springboot.webflux.springbootwebflux.dao.ProductoDao;
import com.springboot.webflux.springbootwebflux.entity.Categoria;
import com.springboot.webflux.springbootwebflux.entity.Producto;
import com.springboot.webflux.springbootwebflux.service.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private ProductoDao productoDao;
	
	@Autowired
	private CategoriaDao categoriaDao;
	 
	@Override
	public Flux<Producto> findAll() {
		return productoDao.findAll();
	}

	@Override
	public Mono<Producto> findById(String id) {
		return productoDao.findById(new ObjectId(id));
	}

	@Override
	public Mono<Producto> save(Producto producto) {
		return productoDao.save(producto);
	}

	@Override
	public Mono<Void> delete(Producto producto) {
		return productoDao.delete(producto);
	}

	@Override
	public Flux<Categoria> findAllCategoria() {
		return categoriaDao.findAll();
	}

	@Override
	public Mono<Categoria> findCategoriaById(String id) {
		return categoriaDao.findById(new ObjectId(id));
	}

	@Override
	public Mono<Categoria> saveCategoria(Categoria categoria) {
		return categoriaDao.save(categoria);
	}

}
