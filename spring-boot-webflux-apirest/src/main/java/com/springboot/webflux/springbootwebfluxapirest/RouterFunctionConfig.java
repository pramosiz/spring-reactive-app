package com.springboot.webflux.springbootwebfluxapirest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.springboot.webflux.springbootwebfluxapirest.handler.ProductoHandler;

@Configuration
public class RouterFunctionConfig {
	
	private String defaultURI = "/v2/productos";
	private String withID = "/{id}";
	
	@Bean
	public RouterFunction<ServerResponse> routes(ProductoHandler handler) {
		
		return RouterFunctions.route(RequestPredicates.GET(defaultURI)
				.or(RequestPredicates.GET("/v3/productos")), handler::listar)
				.andRoute(RequestPredicates.GET(defaultURI.concat(withID)), handler::ver)
				.andRoute(RequestPredicates.POST(defaultURI), handler::crear)
				.andRoute(RequestPredicates.PUT(defaultURI.concat(withID)), handler::editar)
				.andRoute(RequestPredicates.DELETE(defaultURI.concat(withID)), handler::eliminar);
				
	}
	
}
