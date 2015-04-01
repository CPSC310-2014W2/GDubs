

package com.google.gwt.foodvendortracker.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.foodvendortracker.shared.Rating;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Rating")
public interface RatingService extends RemoteService {
	public void addRating(String foodTruckID, int rating) throws NotLoggedInException;
	public ArrayList<FoodTruck> getAllRatings() throws NotLoggedInException;
	public void removeRating(String foodTruckID) throws NotLoggedInException;
}