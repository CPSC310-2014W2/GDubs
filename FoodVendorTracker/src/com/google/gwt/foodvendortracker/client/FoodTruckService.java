package com.google.gwt.foodvendortracker.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("FoodTruck")
public interface FoodTruckService extends RemoteService {
	public void addFoodTruck(String id);
	public String getFoodTrucks();
	public void loadFoodTrucks(String url);
}