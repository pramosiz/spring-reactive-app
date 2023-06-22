package com.springboot.reactor.springbootreactor.app.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Comentarios {

	private List<String> comentarios;
	
	public Comentarios() {
		comentarios = new ArrayList<>();
	}
	
	public void addComentario(String comentario) {
		this.comentarios.add(comentario);
	}
	
}
