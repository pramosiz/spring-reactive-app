package com.springboot.webflux.client.handler;

import java.net.URI;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.springboot.webflux.client.dto.ProductoDTO;
import com.springboot.webflux.client.service.ProductoService;

import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {

	@Autowired
	private ProductoService productoService;
	
	public Mono<ServerResponse> listar(ServerRequest request) {
		return ServerResponse.ok()
				.body(productoService.findAll(), ProductoDTO.class);
	}
	
	public Mono<ServerResponse> ver(ServerRequest request) {
		
		String id = request.pathVariable("id");
		return productoService.findById(id)
				.flatMap(p ->
					ServerResponse.ok()
					.bodyValue(p)
					.switchIfEmpty(ServerResponse.notFound().build()))
				.onErrorResume(error -> {
					WebClientResponseException errorResponse = (WebClientResponseException) error;
					if(errorResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
						return ServerResponse.notFound().build();
					}
					else {
						return Mono.error(errorResponse);
					}
				});
	}
	
	public Mono<ServerResponse> crear(ServerRequest request) {
		
		Mono<ProductoDTO> productoDTO = request.bodyToMono(ProductoDTO.class);
		return productoDTO.flatMap(p -> {
			if(p.getCreateAt() == null) {
				p.setCreateAt(new Date());
			}
			return productoService.save(p);
		})
		.flatMap(p -> ServerResponse.created(URI.create("/client/".concat(p.getId())))
				.bodyValue(p))
		.onErrorResume(error -> {
			WebClientResponseException errorResponse = (WebClientResponseException) error;
			if(errorResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
				return ServerResponse.badRequest()
						.bodyValue(errorResponse.getResponseBodyAsString());
			}
			else {
				return Mono.error(errorResponse);
			}
		});
	}
	
	public Mono<ServerResponse> editar(ServerRequest request) {
		
		Mono<ProductoDTO> productoDTO = request.bodyToMono(ProductoDTO.class);
		String id = request.pathVariable("id");
		return productoDTO.flatMap(p -> productoService.update(p,id))
				.flatMap(p -> ServerResponse.created(URI.create("/client/".concat(p.getId())))
				.bodyValue(p))
				.onErrorResume(error -> {
					WebClientResponseException errorResponse = (WebClientResponseException) error;
					if(errorResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
						return ServerResponse.notFound().build();
					}
					else {
						return Mono.error(errorResponse);
					}
				});
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest request) {
			
		String id = request.pathVariable("id");
		return productoService.delete(id)
				.then(ServerResponse.noContent().build())
				.onErrorResume(error -> {
					WebClientResponseException errorResponse = (WebClientResponseException) error;
					if(errorResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
						return ServerResponse.notFound().build();
					}
					else {
						return Mono.error(errorResponse);
					}
				});
	}

}
