package com.springboot.webflux.springbootwebflux.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "categorias")
public class Categoria {

	@Id
	@NotEmpty
	private String id;
	
	private String nombre;

	public Categoria(String nombre) {
		this.nombre = nombre;
	}
	
}
