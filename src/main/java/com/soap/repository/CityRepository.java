package com.soap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soap.entity.CityEntity;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Integer>{
	
	public CityEntity findByName(String name);
	public CityEntity findByCityId(Integer id);
	public CityEntity deleteByCityId(Integer id);

}
