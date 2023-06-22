package com.springboot.webflux.springbootwebfluxapirest.handler;

import java.net.URI;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.springboot.webflux.springbootwebfluxapirest.entity.Producto;
import com.springboot.webflux.springbootwebfluxapirest.service.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private Validator validator;
	
	public Mono<ServerResponse> listar(ServerRequest request) {
		return ServerResponse.ok().body(productoService.findAll(), Producto.class);
	}
	
	public Mono<ServerResponse> ver(ServerRequest request) {
		String id = request.pathVariable("id");
		return productoService.findById(id)
				.flatMap(p -> ServerResponse.ok()
						.body(BodyInserters.fromValue(p))
						.switchIfEmpty(ServerResponse.notFound().build()));
	}
	
	public Mono<ServerResponse> crear(ServerRequest request) {
		
		Mono<Producto> producto = request.bodyToMono(Producto.class);
		return producto.flatMap(p -> {
			Errors errors = new BeanPropertyBindingResult(p, Producto.class.getName());
			validator.validate(p, errors);
			if(errors.hasErrors()) {
				return Flux.fromIterable(errors.getFieldErrors())
						.map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
						.collectList()
						.flatMap(lista -> ServerResponse.badRequest().body(BodyInserters.fromValue(lista)));
				
			} else {
				if(p.getCreateAt() == null) {
					p.setCreateAt(new Date());
				}
				return productoService.save(p)
						.flatMap(pdb -> ServerResponse.created(URI.create("/v2/productos/".concat(p.getId())))
								.body(BodyInserters.fromValue(pdb)));
			}
			
		});
	}
	
	public Mono<ServerResponse> editar(ServerRequest request) {
		Mono<Producto> producto = request.bodyToMono(Producto.class);
		String id = request.pathVariable("id");
		Mono<Producto> productoDb = productoService.findById(id);
		
		return productoDb.zipWith(producto, (db, req) -> {
			db.setNombre(req.getNombre());
			db.setPrecio(req.getPrecio());
			db.setCategoria(req.getCategoria());
			return db;
		}).flatMap(p -> ServerResponse.created(URI.create("/v2/productos/".concat(p.getId())))
				.body(productoService.save(p), Producto.class))
		.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest request) {
		
		String id = request.pathVariable("id");
		Mono<Producto> productoDb = productoService.findById(id);
		
		return productoDb.flatMap(p -> productoService.delete(p).then(ServerResponse.noContent().build()))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
}
