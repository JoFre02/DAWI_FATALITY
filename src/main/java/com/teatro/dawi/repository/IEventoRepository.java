package com.teatro.dawi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teatro.dawi.model.Evento;

@Repository
public interface IEventoRepository extends JpaRepository<Evento, Integer> {

}
