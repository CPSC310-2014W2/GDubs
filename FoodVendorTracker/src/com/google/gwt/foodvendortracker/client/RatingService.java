package com.google.gwt.foodvendortracker.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Rating")
public interface RatingService extends RemoteService {
	public void addRating(String foodTruckName, int rating) throws NotLoggedInException;
	public List<String> getAllRatingNames() throws NotLoggedInException;
	public int getRating(String ratingName) throws NotLoggedInException;
	public void removeRating(String foodTruckName) throws NotLoggedInException;
}