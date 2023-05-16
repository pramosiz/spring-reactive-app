package com.springboot.reactor.springbootreactor.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioComentarios {

	private User user;
	
	private Comentarios comentarios;
	
}
