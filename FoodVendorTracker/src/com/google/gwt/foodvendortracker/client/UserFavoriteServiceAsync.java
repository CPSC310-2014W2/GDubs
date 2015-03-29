package com.google.gwt.foodvendortracker.client;

import java.util.ArrayList;

import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserFavoriteServiceAsync {
	public void addFavorite(FoodTruck foodtruck, AsyncCallback<Void> async)			;
	public void removeFavorite(FoodTruck foodtruck, AsyncCallback<Void> async)		;
	public void getFavoriteFoodTrucks(AsyncCallback<ArrayList<FoodTruck>> async)	;
}	
