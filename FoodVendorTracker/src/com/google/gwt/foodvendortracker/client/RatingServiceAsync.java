package com.google.gwt.foodvendortracker.client;

import java.util.List;

import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RatingServiceAsync {
	public void addRating(FoodTruck foodTruck, int rating, AsyncCallback<Void> async);
	public void getAllRatings(AsyncCallback<List<Rating>> async);
	public void removeRating(FoodTruck foodTruck, AsyncCallback<Void> async);
}