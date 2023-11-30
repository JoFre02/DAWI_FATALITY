package com.teatro.dawi.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="tb_funcion")
@Data
public class Funcion {
	
	@Id
	private int idfuncion;
	private int idevento;
	private Date fecha;
	private String horainicio;
	private String horafin;
	
	@ManyToOne
	@JoinColumn(name="idevento", insertable = false, updatable = false)
	private Evento objEvento;

}
