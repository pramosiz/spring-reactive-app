package com.springboot.webflux.springbootwebflux.entity;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "productos")
public class Producto {
	
	@Id
	@Field("_id")
	private ObjectId id;
	
	@NotEmpty
	private String nombre;
	
	@NotNull
	private Double precio;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createAt;

	public Producto(String nombre, Double precio) {
		super();
		this.nombre = nombre;
		this.precio = precio;
	}

}
