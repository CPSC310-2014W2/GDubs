package com.google.gwt.foodvendortracker.server;

import java.util.List;

import com.google.gwt.foodvendortracker.client.Rating;
import com.google.gwt.foodvendortracker.client.RatingService;
import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RatingServiceImpl extends RemoteServiceServlet implements RatingService {
	public void addRating(FoodTruck foodTruck, int rating) {
		//TODO
	}
	public List<Rating> getAllRatings(){
		//TODO		
		return null;
	};
	public void removeRating(FoodTruck foodTruck){
		//TODO
	};
}
