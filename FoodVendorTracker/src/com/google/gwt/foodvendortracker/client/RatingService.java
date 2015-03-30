package com.google.gwt.foodvendortracker.client;
import java.util.ArrayList;

import com.google.appengine.api.datastore.Rating;
import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Rating")
public interface RatingService extends RemoteService 
{
	  public void addRating(String rating, FoodTruck foodTruck) throws NotLoggedInException		;
	  public void removeRating(FoodTruck foodTruck) throws NotLoggedInException					;	
	  public ArrayList<FoodTruck> getRatings() throws NotLoggedInException							;
}	
