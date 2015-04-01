package com.google.gwt.foodvendortracker.client;

import java.util.List;

import com.google.gwt.foodvendortracker.server.UserRating;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserRatingServiceAsync {
	public void addRating(String foodTruckName, int rating, AsyncCallback<Void> async);
	public void removeRating(String foodTruckName, AsyncCallback<Void> async);
	public void getCurrentUserRating(String foodTruckName, AsyncCallback<Integer> async);
	public void getRatings(AsyncCallback<List<ClientRating>> async);
}