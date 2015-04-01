package com.google.gwt.foodvendortracker.client;

import java.util.List;

import com.google.gwt.foodvendortracker.server.UserRating;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RatingServiceAsync {
	public void addRating(String foodTruckName, int rating, AsyncCallback<Void> async);
	public void getAllRatingNames(AsyncCallback<List<String>> async);
	public void getRating(String ratingName, AsyncCallback<Integer> async);
	public void removeRating(String foodTruckName, AsyncCallback<Void> async);
}