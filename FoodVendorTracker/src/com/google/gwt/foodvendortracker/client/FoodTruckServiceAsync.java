package com.google.gwt.foodvendortracker.client;

import java.util.List;

import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FoodTruckServiceAsync {
	public void parseFoodTruckData(AsyncCallback<Void> async);
	public void persistFoodTruckData(AsyncCallback<Void> async);
	public void queryFoodTrucks(AsyncCallback<String> async);
	public void getFoodTrucks(AsyncCallback<List<FoodTruck>> async);
	public void deleteFoodTruckData(AsyncCallback<Void> async);
}
