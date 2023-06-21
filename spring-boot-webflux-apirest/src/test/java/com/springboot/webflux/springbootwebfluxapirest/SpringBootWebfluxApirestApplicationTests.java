package com.springboot.webflux.springbootwebfluxapirest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.springboot.webflux.springbootwebfluxapirest.entity.Categoria;
import com.springboot.webflux.springbootwebfluxapirest.entity.Producto;
import com.springboot.webflux.springbootwebfluxapirest.service.ProductoService;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootWebfluxApirestApplicationTests {

	@Autowired
	private WebTestClient client;
	
	@Autowired
	private ProductoService productoService;
	
	@Test
	void testListar() {
		
		client.get()
		.uri("/v2/productos")
		.exchange()
		.expectStatus().isOk()
		.expectBodyList(Producto.class)
		.hasSize(9);
	}
	
	@Test
	void testListar_consumeWith() {
		
		client.get()
		.uri("/v2/productos")
		.exchange()
		.expectStatus().isOk()
		.expectBodyList(Producto.class)
		.consumeWith(response -> {
			List<Producto> productos = response.getResponseBody();
			productos.forEach(p -> System.out.println(p.getNombre()));
			assertEquals(9, productos.size());
		});
	}
	
	@Test
	void testVer() {
		
		Mono<Producto> producto = productoService.findByNombre("TV Panasonic Pantalla LCD");
		client.get()
		.uri("/v2/productos/{id}", Collections.singletonMap("id", producto.block().getId()))
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("TV Panasonic Pantalla LCD");
	}
	
	@Test
	void testCrear() {
		
		Mono<Categoria> categoria = productoService.findCategoriaByNombre("Muebles");
		Producto producto = new Producto("Mesa comedor", 100.0, categoria.block());
		client.post()
		.uri("/v2/productos")
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Mesa comedor")
		.jsonPath("$.categoria.nombre").isEqualTo("Muebles");
	}
	
	@Test
	void testEditar() {
		
		Mono<Producto> producto = productoService.findByNombre("TV Panasonic Pantalla LCD");
		Mono<Categoria> categoria = productoService.findCategoriaByNombre("Electrónico");
		Producto productoEditado = new Producto("TV Samsung Pantalla LED", 700.0, categoria.block());
		
		client.put()
		.uri("/v2/productos/{id}", Collections.singletonMap("id", producto.block().getId()))
		.body(Mono.just(productoEditado), Producto.class)
		.exchange()		// con exchange enviamos
		.expectStatus().isCreated()
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("TV Samsung Pantalla LED")
		.jsonPath("$.categoria.nombre").isEqualTo("Electrónico");
	}

	@Test
	void testEliminar() {
		
		Mono<Producto> producto = productoService.findByNombre("TV Panasonic Pantalla LCD");
		
		client.delete()
		.uri("/v2/productos/{id}", Collections.singletonMap("id", producto.block().getId()))
		.exchange()
		.expectStatus().isNoContent()
		.expectBody().isEmpty();
	}
}
