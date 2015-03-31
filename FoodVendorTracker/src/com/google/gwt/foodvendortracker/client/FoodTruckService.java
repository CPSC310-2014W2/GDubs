package com.google.gwt.foodvendortracker.client;

import java.util.List;

import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("FoodTruck")
public interface FoodTruckService extends RemoteService {
	public void parseFoodTruckData(String urlString);
	public void persistFoodTruckData();
	public String queryFoodTrucks();
	public List<FoodTruck> getFoodTrucks();
	public void deleteFoodTruckData();
}