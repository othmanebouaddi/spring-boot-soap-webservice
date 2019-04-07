package com.soap.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cities")
public class CityEntity  implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	@Id
	private Integer cityId;
	private String name;
	private Integer population;
       
        public CityEntity(){
		
	}
	
	public CityEntity(Integer cityId, String name, Integer population) {
		this.cityId = cityId;
		this.name = name;
		this.population = population;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPopulation() {
		return population;
	}

	public void setPopulation(Integer population) {
		this.population = population;
	}

}
