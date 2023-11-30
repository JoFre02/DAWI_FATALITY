package com.teatro.dawi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teatro.dawi.model.Funcion;

@Repository
public interface IFuncionRepository extends JpaRepository<Funcion, Integer> {
	
	List<Funcion> findByIdeventoEquals(int idevento);

}
