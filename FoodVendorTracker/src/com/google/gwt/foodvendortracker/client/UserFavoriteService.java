package com.google.gwt.foodvendortracker.client;

import java.util.ArrayList;

import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("favorite")
public interface UserFavoriteService extends RemoteService{
	public String addFavorite(String foodTruckID) throws NotLoggedInException		   ;
	public void removeFavorite(String foodTruckID) throws NotLoggedInException		   ;
	public ArrayList<FoodTruck> getFavoriteFoodTrucks() throws NotLoggedInException	   ;
}
