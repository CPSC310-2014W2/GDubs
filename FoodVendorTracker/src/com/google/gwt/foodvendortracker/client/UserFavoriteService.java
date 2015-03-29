package com.google.gwt.foodvendortracker.client;

import java.util.ArrayList;

import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("favorite")
public interface UserFavoriteService extends RemoteService{
	public void addFavorite(FoodTruck foodtruck) throws NotLoggedInException		   ;
	public void removeFavorite(FoodTruck foodtruck) throws NotLoggedInException		   ;
	public ArrayList<FoodTruck> getFavoriteFoodTrucks() throws NotLoggedInException	   ;
}
