package com.teatro.dawi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table (name = "tb_area")
@Data
public class Area {
	
	@Id
	private int idarea;
	private String nomarea;
	private double precio;
	

}
