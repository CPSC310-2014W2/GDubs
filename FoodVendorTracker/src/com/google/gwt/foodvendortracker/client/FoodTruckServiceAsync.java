package com.google.gwt.foodvendortracker.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FoodTruckServiceAsync {
	public void addFoodTruck(String id, AsyncCallback<Void> async);
	public void getFoodTrucks(AsyncCallback<String> async);
	public void loadFoodTrucks(String url, AsyncCallback<Void> async);
}
