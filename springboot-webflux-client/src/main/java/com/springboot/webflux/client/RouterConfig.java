package com.springboot.webflux.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.springboot.webflux.client.handler.ProductoHandler;

@Configuration
public class RouterConfig {

	private String defaultURI = "/client";
	private String withID = "/{id}";
	
	@Bean
	public RouterFunction<ServerResponse> routes(ProductoHandler handler) {
		
		return RouterFunctions.route(RequestPredicates.GET(defaultURI), handler::listar)
				.andRoute(RequestPredicates.GET(defaultURI.concat(withID)), handler::ver)
				.andRoute(RequestPredicates.POST(defaultURI), handler::crear)
				.andRoute(RequestPredicates.PUT(defaultURI.concat(withID)), handler::editar)
				.andRoute(RequestPredicates.DELETE(defaultURI.concat(withID)), handler::eliminar);
	}
}
