package com.google.gwt.foodvendortracker.client;

import java.util.ArrayList;
import com.google.gwt.foodvendortracker.server.Rating;
import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RatingServiceAsync {
	public void addRating(String rating, FoodTruck foodtruck, AsyncCallback<Void> async)		;
	public void removeRating(FoodTruck foodtruck, AsyncCallback<Void> async)					;
	public void getRatings(AsyncCallback<ArrayList<FoodTruck>> async)			   				;
}	
