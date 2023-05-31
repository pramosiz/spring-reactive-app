package com.springboot.webflux.springbootwebflux.controller;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;

import com.springboot.webflux.springbootwebflux.dao.ProductoDao;
import com.springboot.webflux.springbootwebflux.entity.Producto;

import reactor.core.publisher.Flux;

@Controller
public class ProductoController {

	@Autowired
	private ProductoDao productoDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@GetMapping({"/listar", "/"})
	public String listar(Model model) {
		
		Flux<Producto> productos = productoDao
				.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				});
		
		productos.doOnNext(producto -> LOGGER.info("{}", producto.getNombre()))
				.subscribe();
				
		model.addAttribute("productos", productos);
		model.addAttribute("titulo", "Listado de productos");
		return "listar";
	}
	
	@GetMapping("/listarDataDriver")
	public String listarDataDriver(Model model) {	// ReactiveDataDriver - Trabajar la contrapresión, 
													// cuando recibimos una gran cantidad de elementos
		
		Flux<Producto> productos = productoDao
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
													
		
		Flux<Producto> productos = productoDao
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
													
		
		Flux<Producto> productos = productoDao
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
