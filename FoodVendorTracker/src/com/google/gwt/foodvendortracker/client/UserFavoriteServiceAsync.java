package com.google.gwt.foodvendortracker.client;

import java.util.ArrayList;

import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserFavoriteServiceAsync {
	public void addFavorite(String foodTruckID, AsyncCallback<String> async)			;
	public void removeFavorite(String foodTruckID, AsyncCallback<Void> async)		;
	public void getFavoriteFoodTrucks(AsyncCallback<ArrayList<FoodTruck>> async)	;
}	
