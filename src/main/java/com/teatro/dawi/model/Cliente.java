package com.teatro.dawi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_cliente")
@Data
public class Cliente {

	@Id
	private int idcli;
	private String nomcli;
	private String apecli;
	private String dni;
	private String username;
	private String clave;
	
}
