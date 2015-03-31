package com.google.gwt.foodvendortracker.client;

import java.util.List;

import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Rating")
public interface RatingService extends RemoteService {
	public void addRating(FoodTruck foodTruck, int rating);
	public List<Rating> getAllRatings();
	public void removeRating(FoodTruck foodTruck);
}