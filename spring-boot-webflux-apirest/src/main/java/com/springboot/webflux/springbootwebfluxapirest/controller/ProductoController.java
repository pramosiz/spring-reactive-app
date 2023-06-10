package com.springboot.webflux.springbootwebfluxapirest.controller;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.springboot.webflux.springbootwebfluxapirest.entity.Producto;
import com.springboot.webflux.springbootwebfluxapirest.service.ProductoService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
public class ProductoController {
	
	@Autowired
	private ProductoService productoService;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Producto>>> lista() {
		return Mono.just(ResponseEntity.ok(productoService.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Producto>> ver(@PathVariable String id) {
		return productoService.findById(id)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> crear(@Valid @RequestBody Mono<Producto> monoProducto) {
		
		Map<String, Object> respuesta = new HashMap<>();
		return monoProducto.flatMap(producto -> {
			
			if(producto.getCreateAt() == null) {
				producto.setCreateAt(new Date());
			}
			
			return productoService.save(producto)
					.map(p -> {
						respuesta.put("producto", p);
						respuesta.put("mensaje", "Mensaje creado con éxito");
						respuesta.put("timestamp", new Date());
						return ResponseEntity.created(URI.create("/productos/".concat(producto.getId())))
							.body(respuesta);
						});
		
			}).onErrorResume(t -> {
				return Mono.just(t).cast(WebExchangeBindException.class)
						.flatMap(e -> Mono.just(e.getFieldErrors()))
						.flatMapMany(Flux::fromIterable)
						.map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
						.collectList()
						.flatMap(list -> {
							respuesta.put("errors", list);
							respuesta.put("timestamp", new Date());
							respuesta.put("status", HttpStatus.BAD_REQUEST.value());
							return Mono.just(ResponseEntity.badRequest().body(respuesta));
						});
			});
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Producto>> editar(@PathVariable String id, @RequestBody Producto producto) {
		return productoService.findById(id)
				.flatMap(p -> {
					p.setNombre(producto.getNombre());
					p.setPrecio(producto.getPrecio());
					p.setCategoria(producto.getCategoria());
					return productoService.save(p);
					})
				.map(p -> ResponseEntity.created(URI.create("/productos/".concat(p.getId())))
						.body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Object>> eliminar(@PathVariable String id) {
		return productoService.findById(id)
				.flatMap(p -> 
					productoService.delete(p).then(Mono.just(ResponseEntity.noContent().build())))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
}
