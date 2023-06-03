package com.springboot.webflux.springbootwebflux.controller;

import java.time.Duration;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;

import com.springboot.webflux.springbootwebflux.entity.Producto;
import com.springboot.webflux.springbootwebflux.service.ProductoService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class ProductoController {

	@Autowired
	private ProductoService productoService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@GetMapping({"/listar", "/"})
	public Mono<String> listar(Model model) {
		
		Flux<Producto> productos = productoService
				.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				});
		
		productos.doOnNext(producto -> LOGGER.info("{}", producto.getNombre()))
				.subscribe();
				
		model.addAttribute("productos", productos);
		model.addAttribute("titulo", "Listado de productos");
		return Mono.just("listar");
	}
	
	@GetMapping("/form")
	public Mono<String> crear(Model model) {
		model.addAttribute("producto", new Producto());
		model.addAttribute("titulo", "Formulario de producto");
		return Mono.just("form");
	}
	
	@GetMapping("/form/{id}")
	public Mono<String> editar(@PathVariable String id, Model model) {
		Mono<Producto> productoMono = productoService.findById(id)
				.doOnNext(p -> LOGGER.info("Producto: {}", p.getNombre()))
				.defaultIfEmpty(new Producto());
		
		model.addAttribute("titulo", "Editar Producto");
		model.addAttribute("producto", productoMono);
		
		return Mono.just("/form");
	}
	
	@PostMapping("/form")
	public Mono<String> guardar(@Valid Producto producto, BindingResult result, 
			SessionStatus status, Model model) {		// Estos atributos deben ir juntos siempre, el orden importa
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Errores en formulario producto");
			model.addAttribute("boton", "Guardar");
			return Mono.just("/form");
		} else {
			status.setComplete();
			if(producto.getCreateAt() == null) {
				producto.setCreateAt(new Date());
			}
			return productoService.save(producto)
			.doOnNext(p -> LOGGER.info("Producto guardado: {}  ID: {}", p.getNombre(), p.getId()))
			.thenReturn("redirect:/listar?success=producto+guardado+con+exito");
		}
		
	}
	
	@GetMapping("/listarDataDriver")
	public String listarDataDriver(Model model) {	// ReactiveDataDriver - Trabajar la contrapresión, 
													// cuando recibimos una gran cantidad de elementos
		
		Flux<Producto> productos = productoService
				.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				})
				.delayElements(Duration.ofSeconds(1));
		
		productos.doOnNext(producto -> LOGGER.info("{}", producto.getNombre()))
				.subscribe();
				
		model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 2));
		model.addAttribute("titulo", "Listado de productos");
		return "listar";
	}
	
	@GetMapping("/listarFull")
	public String listarFull(Model model) {	 
													
		
		Flux<Producto> productos = productoService
				.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				})
				.repeat(5000);
		
		productos.doOnNext(producto -> LOGGER.info("{}", producto.getNombre()))
				.subscribe();
				
		model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 2));
		model.addAttribute("titulo", "Listado de productos");
		return "listar";
	}
	
	@GetMapping("/listarChunked")
	public String listarChunked(Model model) {	 
													
		
		Flux<Producto> productos = productoService
				.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				})
				.repeat(5000);
		
		productos.doOnNext(producto -> LOGGER.info("{}", producto.getNombre()))
				.subscribe();
				
		model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 2));
		model.addAttribute("titulo", "Listado de productos");
		return "listar-chunked";
	}
	
}
