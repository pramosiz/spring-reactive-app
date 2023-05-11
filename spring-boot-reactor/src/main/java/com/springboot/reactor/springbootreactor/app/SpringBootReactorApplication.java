package com.springboot.reactor.springbootreactor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.springboot.reactor.springbootreactor.app.models.User;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner{

	Logger logger = LoggerFactory.getLogger(SpringBootReactorApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<User> nombres = Flux.just("Andres Guzman", "Pedro Fulano", "Diego Martinez", "Juan Pedro", "Bruce Lee", "Bruce Willis")
				.map(nombre -> new User(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getName().equalsIgnoreCase("bruce"))
				.doOnNext(user -> {
					if(user == null) {
						throw new IllegalArgumentException("No podemos tratar con elementos vacíos");
					}
					logger.info("{} {}", user.getName(), user.getLastName());
				})
				.doOnComplete(
						() -> logger.info("Ha terminado el flujo")	// () -> 'is a Runnable expression'
				);
							
		nombres.subscribe();
	}

}
