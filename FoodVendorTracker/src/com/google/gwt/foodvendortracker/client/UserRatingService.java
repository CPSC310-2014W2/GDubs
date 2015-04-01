package com.google.gwt.foodvendortracker.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("UserRating")
public interface UserRatingService extends RemoteService {
	public void addRating(String foodTruckName, int rating) throws NotLoggedInException;
	public void removeRating(String foodTruckName) throws NotLoggedInException;
	public int getCurrentUserRating(String foodTruckName) throws NotLoggedInException;
	public List<ClientRating> getRatings() throws NotLoggedInException;
}