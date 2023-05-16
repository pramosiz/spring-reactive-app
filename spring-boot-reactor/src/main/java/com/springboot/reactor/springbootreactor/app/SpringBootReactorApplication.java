package com.springboot.reactor.springbootreactor.app;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.Timer;
import java.util.TimerTask;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.springboot.reactor.springbootreactor.app.models.Comentarios;
import com.springboot.reactor.springbootreactor.app.models.User;
import com.springboot.reactor.springbootreactor.app.models.UsuarioComentarios;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner{

	Logger logger = LoggerFactory.getLogger(SpringBootReactorApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ejemploContraPresion2();
	}
	
	////////////////////////
	// Retrasos de tiempo //
	////////////////////////
	public void ejemploContraPresion2() {
		
		Flux.range(1, 10)
			.log()
			.limitRate(5)
			.subscribe();
		
	}
	
	public void ejemploContraPresion1() {
		
		Flux.range(1, 10)
			.log()
			.subscribe(new Subscriber<Integer>() {
				
				private Subscription s;
				
				private Integer limite = 5;
				private Integer consumido = 0;

				@Override
				public void onSubscribe(Subscription s) {
					this.s = s;		
//					s.request(Long.MAX_VALUE);
					s.request(limite);
				}

				@Override
				public void onNext(Integer t) {
					logger.info(t.toString());
					++consumido;
					if(consumido.equals(limite)) {
						consumido = 0;
						s.request(limite);
					}
				}

				@Override
				public void onError(Throwable t) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onComplete() {
					// TODO Auto-generated method stub
					
				}
			});
		
	}
	
	public void ejemploDesdeCreate() throws InterruptedException {
		
		Flux.create(emitter -> {
			Timer time = new Timer();
			time.schedule(new TimerTask() {
				
				private int contador = 0;
				
				@Override
				public void run() {
					emitter.next(++contador);
					if(contador == 10) {
						time.cancel();
						emitter.complete();
					}
				}
				
			}, 1000, 1000);
		})
		.doOnNext(next -> logger.info(next.toString()))
		.doOnComplete(() -> logger.info("Hemos terminado"))
		.subscribe();
		
	}
	
	public void ejemploIntervaloInfinito() throws InterruptedException {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		Flux.interval(Duration.ofSeconds(1))
			.doOnTerminate(latch::countDown)
			.flatMap(i -> {
				if(i >= 5) {
					return Flux.error(new InterruptedException("Solo hasta 5"));
				}
				return Flux.just(i);
			})
			.map(i -> "Hola " + i)
			.retry(2)
			.subscribe(s -> logger.info(s), e -> logger.error(e.getMessage()));
		
		latch.await();
		
	}
	
	public void ejemploDelayElements() {
		Flux<Integer> rango = Flux
				.range(1, 12)
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(i -> logger.info(i.toString()));
		
		rango.subscribe();	
		
		try {
			Thread.sleep(13000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void ejemploIntervalo() {
		Flux<Integer> rango = Flux.range(1, 12);
		Flux<Long> retraso = Flux.interval(Duration.ofSeconds(1));
		
		rango
			.zipWith(retraso, (ra, re) -> ra)
			.doOnNext(i -> logger.info(i.toString()))
			.blockLast();	// No recomendable porque produce cuello de botella
			
	}
	
	///////////
	// Rango //
	///////////
	public void ejemploZipWithRangos() {
		Flux.just(1, 2, 3, 4)
			.map(i -> (i*2))
			.zipWith(Flux.range(0, 4), (uno, dos) -> String.format("Primer Flux: %d, Segundo Flux: %d", uno, dos))
			.subscribe(texto -> logger.info(texto));
	}
	
	///////////////////////
	// Combinar 2 flujos //
	///////////////////////
	public void ejemploUsuarioComentariosZipWithForma2() {
		Mono<User> usuarioMono = Mono.fromCallable(() -> new User("John", "Doe"));
		Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("Hola pepe, qué tal!");
			comentarios.addComentario("Mañana voy a la playa!");
			comentarios.addComentario("Prueba número 3");
			return comentarios;
		});
		
		Mono<UsuarioComentarios> usuarioComentariosMono = usuarioMono
				.zipWith(comentariosUsuarioMono)
				.map(tuple -> {
					   User u = tuple.getT1();
					   Comentarios c = tuple.getT2();
					   return new UsuarioComentarios(u, c);
				});
		
		usuarioComentariosMono.subscribe(uc -> logger.info(uc.toString()));
	}
	
	public void ejemploUsuarioComentariosZipWith() {
		Mono<User> usuarioMono = Mono.fromCallable(() -> new User("John", "Doe"));
		Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("Hola pepe, qué tal!");
			comentarios.addComentario("Mañana voy a la playa!");
			comentarios.addComentario("Prueba número 3");
			return comentarios;
		});
		
		Mono<UsuarioComentarios> usuarioComentariosMono = 
		usuarioMono.zipWith(comentariosUsuarioMono, UsuarioComentarios::new);
		
		usuarioComentariosMono.subscribe(uc -> logger.info(uc.toString()));
	}
	
	public void ejemploUsuarioComentariosflatMap() {
		Mono<User> usuarioMono = Mono.fromCallable(() -> new User("John", "Doe"));
		Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("Hola pepe, qué tal!");
			comentarios.addComentario("Mañana voy a la playa!");
			comentarios.addComentario("Prueba número 3");
			return comentarios;
		});
		
		usuarioMono.flatMap(u -> comentariosUsuarioMono.map(c -> new UsuarioComentarios(u, c)))
				   .subscribe(uc -> logger.info(uc.toString()));
	}
	
	/////////////
	// FlatMap //
	/////////////
	public void ejemploFlatMap() {
		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Andres Guzman");
		usuariosList.add("Pedro Fulano");
		usuariosList.add("Diego Martinez");
		usuariosList.add("Juan Pedro");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");
		
		
		Flux.fromIterable(usuariosList)
				.map(nombre -> new User(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.flatMap(usuario -> {
					if(usuario.getName().equalsIgnoreCase("bruce")) {
						return Mono.just(usuario);
					}
					else {
						return Mono.empty();
					}
				})
				.map(user -> {
					String nombre = user.getName().toLowerCase();
					user.setName(nombre);
					return user;
				})
				.subscribe(u -> logger.info(u.toString()));
							
	}
	
	public void ejemploToString() {
		List<User> usuariosList = new ArrayList<>();
		usuariosList.add(new User("Andres", "Guzman"));
		usuariosList.add(new User("Pedro", "Fulano"));
		usuariosList.add(new User("Diego", "Martinez"));
		usuariosList.add(new User("Juan", "Pedro"));
		usuariosList.add(new User("Bruce", "Lee"));
		usuariosList.add(new User("Bruce", "Willis"));
		
		Flux.fromIterable(usuariosList)
				.map(usuario -> usuario.getName().toUpperCase().concat(" ").concat(usuario.getLastName().toUpperCase()))
				.flatMap(nombre -> {
					if(nombre.contains("bruce".toUpperCase())) {
						return Mono.just(nombre);
					}
					else {
						return Mono.empty();
					}
				})
				.map(String::toLowerCase)
				.subscribe(u -> logger.info(u.toString()));
							
		
	}
	
	/////////////////////////
	// Métodos principales //
	/////////////////////////
	public void ejemploIterable() throws Exception {
		List<User> usuariosList = new ArrayList<>();
		usuariosList.add(new User("Andres", "Guzman"));
		usuariosList.add(new User("Pedro", "Fulano"));
		usuariosList.add(new User("Diego", "Martinez"));
		usuariosList.add(new User("Juan", "Pedro"));
		usuariosList.add(new User("Bruce", "Lee"));
		usuariosList.add(new User("Bruce", "Willis"));
		
		//Flux<User> nombres = Flux.just("Andres Guzman", "Pedro Fulano", "Diego Martinez", "Juan Pedro", "Bruce Lee", "Bruce Willis")
		Flux<User> usuarios = Flux.fromIterable(usuariosList)
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
							
		usuarios.subscribe();
	}
	
	public void ejemploCollectionList() {
		List<User> usuariosList = new ArrayList<>();
		usuariosList.add(new User("Andres", "Guzman"));
		usuariosList.add(new User("Pedro", "Fulano"));
		usuariosList.add(new User("Diego", "Martinez"));
		usuariosList.add(new User("Juan", "Pedro"));
		usuariosList.add(new User("Bruce", "Lee"));
		usuariosList.add(new User("Bruce", "Willis"));
		
		Flux.fromIterable(usuariosList)
				.collectList()
				.subscribe(u -> {
					u.forEach(item -> logger.info(item.toString()));
				});
							
		
	}
	
}
