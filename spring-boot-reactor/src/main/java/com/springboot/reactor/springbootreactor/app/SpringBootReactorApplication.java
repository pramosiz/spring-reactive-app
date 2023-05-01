package com.springboot.reactor.springbootreactor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner{

	Logger logger = LoggerFactory.getLogger(SpringBootReactorApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<String> nombres = Flux.just("Andres", "Pedro", "Diego", "Juan")
				.doOnNext(logger::info);									// doOnNext: Método evento que parte del ciclo de 
																			// vida del observable cada vez que llega un elemento
		
		// Si ejecutamos sólo la línea de comandos de arriba no ocurre nada
		// porque debemos estar suscritos para ver qué está ocurriendo
		nombres.subscribe();
		// Por cada elemento del flujo "Flux", el método doOnNext se ejecuta cada vez que el observador
		// (el flujo nombres) notifica que ha llegado un nuevo elemento
	}

}
