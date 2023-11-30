package com.teatro.dawi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.teatro.dawi.model.Area;

@Repository
public interface IAreaRepository extends JpaRepository<Area, Integer> {

}
