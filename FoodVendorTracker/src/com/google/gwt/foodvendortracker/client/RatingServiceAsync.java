package com.google.gwt.foodvendortracker.client;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.foodvendortracker.shared.Rating;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

public interface RatingServiceAsync {
	public void addRating(String foodTruckName, int rating, AsyncCallback<Void> async);
	public void getAllRatings(AsyncCallback<ArrayList<FoodTruck>> async);
	public void removeRating(String foodTruckID, AsyncCallback<Void> async);
}