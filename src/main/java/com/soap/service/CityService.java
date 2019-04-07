package com.soap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soap.entity.CityEntity;
import com.soap.repository.CityRepository;

@Service
@Transactional
public class CityService {
	
	@Autowired
	private CityRepository repository;

	 public CityService() {

    }

    @Autowired
    public CityService(CityRepository repository) {
        this.repository = repository;
    }
	    
	    
    public CityEntity getEntityById(Integer id) {
        return repository.findByCityId(id);
    }

    public CityEntity getEntityByName(String name) {
        return repository.findByName(name);
    }

    public List < CityEntity > getAllEntities() {
        List < CityEntity > list = new ArrayList < > ();
        repository.findAll().forEach(e -> list.add(e));
        return list;
    }

    public CityEntity addEntity(CityEntity entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean updateEntity(CityEntity entity) {
        try {
            repository.save(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEntityById(Integer id) {
        try {
            repository.deleteByCityId(id);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
