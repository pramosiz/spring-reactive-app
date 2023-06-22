package com.springboot.webflux.client.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductoDTO {

	private String id;
	private String nombre;
	private Double precio;
	private Date createAt;
	private CategoriaDTO categoria;
}
