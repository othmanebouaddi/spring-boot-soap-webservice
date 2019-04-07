package com.soap.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.soap.entity.CityEntity;
import com.soap.gs_ws.AddCityRequest;
import com.soap.gs_ws.AddCityResponse;
import com.soap.gs_ws.CityType;
import com.soap.gs_ws.DeleteCityRequest;
import com.soap.gs_ws.DeleteCityResponse;
import com.soap.gs_ws.GetAllCitiesRequest;
import com.soap.gs_ws.GetAllCitiesResponse;
import com.soap.gs_ws.GetCityByNameRequest;
import com.soap.gs_ws.GetCityByNameResponse;
import com.soap.gs_ws.ServiceStatus;
import com.soap.gs_ws.UpdateCityRequest;
import com.soap.gs_ws.UpdateCityResponse;
import com.soap.service.CityService;

@Endpoint
public class CityEndpoint {

	
	public static final String NAMESPACE_URI = "http://www.soap.com/cities-ws";
	
	
	@Autowired
	private CityService service;

	public CityEndpoint() {

	}
	

	@Autowired
	public CityEndpoint(CityService service) {
		this.service = service;
	}

	

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCityByNameRequest")
	@ResponsePayload
	public GetCityByNameResponse getCityByName(@RequestPayload GetCityByNameRequest request) {
		GetCityByNameResponse response = new GetCityByNameResponse();
		CityEntity cityEntity = service.getEntityByName(request.getName());
		CityType cityType = new CityType();
		BeanUtils.copyProperties(cityEntity, cityType);
		response.setCityType(cityType);
		return response;

	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllCitiesRequest")
	@ResponsePayload
	public GetAllCitiesResponse getAllCities(@RequestPayload GetAllCitiesRequest request) {
		GetAllCitiesResponse response = new GetAllCitiesResponse();
		List<CityType> cityList = new ArrayList<CityType>();
		List<CityEntity> cityEntityList = service.getAllEntities();
		for (CityEntity entity : cityEntityList) {
			CityType city = new CityType();
			BeanUtils.copyProperties(entity, city);
			cityList.add(city);
		}
		response.getCityType().addAll(cityList);

		return response;

	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "addCityRequest")
	@ResponsePayload
	public AddCityResponse addMovie(@RequestPayload AddCityRequest request) {
		AddCityResponse response = new AddCityResponse();
		CityType newMovieType = new CityType();
		ServiceStatus serviceStatus = new ServiceStatus();

		CityEntity newMovieEntity = new CityEntity(request.getCityId(), request.getName(), request.getPopulation());
		CityEntity savedMovieEntity = service.addEntity(newMovieEntity);

		if (savedMovieEntity == null) {
			serviceStatus.setStatusCode("CONFLICT");
			serviceStatus.setMessage("Exception while adding Entity");
		} else {

			BeanUtils.copyProperties(savedMovieEntity, newMovieType);
			serviceStatus.setStatusCode("SUCCESS");
			serviceStatus.setMessage("Content Added Successfully");
		}

		response.setCityType(newMovieType);
		response.setServiceStatus(serviceStatus);
		return response;

	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateCityRequest")
	@ResponsePayload
	public UpdateCityResponse updateMovie(@RequestPayload UpdateCityRequest request) {
		UpdateCityResponse response = new UpdateCityResponse();
		ServiceStatus serviceStatus = new ServiceStatus();
		// 1. Find if movie available
		CityEntity movieFromDB = service.getEntityByName(request.getName());
		
		if(movieFromDB == null) {
			serviceStatus.setStatusCode("NOT FOUND");
			serviceStatus.setMessage("Movie = " + request.getName() + " not found");
		}else {
			
			// 2. Get updated movie information from the request
			movieFromDB.setName(request.getName());
			movieFromDB.setPopulation(request.getPopulation());
			// 3. update the movie in database
			
			boolean flag = service.updateEntity(movieFromDB);
			
			if(flag == false) {
				serviceStatus.setStatusCode("CONFLICT");
				serviceStatus.setMessage("Exception while updating Entity=" + request.getName());;
			}else {
				serviceStatus.setStatusCode("SUCCESS");
				serviceStatus.setMessage("Content updated Successfully");
			}
			
			
		}
		
		response.setServiceStatus(serviceStatus);
		return response;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteCityRequest")
	@ResponsePayload
	public DeleteCityResponse deleteMovie(@RequestPayload DeleteCityRequest request) {
		DeleteCityResponse response = new DeleteCityResponse();
		ServiceStatus serviceStatus = new ServiceStatus();

		boolean flag = service.deleteEntityById(request.getCityId());

		if (flag == false) {
			serviceStatus.setStatusCode("FAIL");
			serviceStatus.setMessage("Exception while deletint Entity id=" + request.getCityId());
		} else {
			serviceStatus.setStatusCode("SUCCESS");
			serviceStatus.setMessage("Content Deleted Successfully");
		}

		response.setServiceStatus(serviceStatus);
		return response;
	}
}
