package com.google.gwt.foodvendortracker.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.foodvendortracker.client.FoodTruckService;
import com.google.gwt.foodvendortracker.client.NotLoggedInException;
import com.google.gwt.foodvendortracker.client.UserFavoriteService;
import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FoodTruckServiceImpl extends RemoteServiceServlet implements FoodTruckService, UserFavoriteService {
	private static final PersistenceManagerFactory PMF = 
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	private List<FoodTruck> foodTrucks = new ArrayList<>();
	private FoodTruckParser foodTruckParser = new FoodTruckParser();
	
	@Override
	public void parseFoodTruckData() {
		foodTruckParser.parse();
		foodTrucks = foodTruckParser.getFoodTruckList();
	}
	
	@Override
	public void persistFoodTruckData() {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistentAll(foodTrucks);
		} finally {
			pm.close();
		}
	}

	@Override
	public String queryFoodTrucks() {
		PersistenceManager pm = getPersistenceManager();
		FoodTruck foodTrucks;
		String returnString;
		try {
			Query q = pm.newQuery(FoodTruck.class, "id == QueryId");
			q.declareParameters("String QueryId");
			List<FoodTruck> list = (List<FoodTruck>) q.execute("C1");
			System.out.println(list.toString());
			foodTrucks = list.get(0);
			returnString = foodTrucks.getId();
		} finally {
			pm.close();
		}
		return returnString;
	}
	@Override
	public List<FoodTruck> getFoodTrucks() 
	{
		List<FoodTruck> list;
		PersistenceManager pm = getPersistenceManager();
		Query query = pm.newQuery(FoodTruck.class);
		list = (List<FoodTruck>) query.execute();
		System.out.println("Size of food truck list: " + list.size());
		List<FoodTruck> temp = new ArrayList<FoodTruck>(list);
		return temp;
	}
	
	@Override
	public void deleteFoodTruckData() 
	{
		PersistenceManager pm = getPersistenceManager();
		pm.deletePersistent(foodTrucks);
	}
	
	public void addFavorite(FoodTruck foodtruck) throws NotLoggedInException
	{
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try
		{
			Favorite favorite = new Favorite();
			favorite.setFoodTruck(foodtruck);
			favorite.setUser(getUser());
			pm.makePersistent(favorite);
		}
		finally
		{
			pm.close();
		}
	}
	
	public void removeFavorite(FoodTruck foodtruck) throws NotLoggedInException
	{
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try
		{
			long deleteCount = 0;
			Query q = pm.newQuery(Favorite.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<Favorite> favorites = (List<Favorite>) q.execute(getUser());
			for(Favorite f : favorites)
			{
				if(f.getFoodTruck().getName().equals(foodtruck.getName()))
				{
					deleteCount ++;
					pm.deletePersistent(f);
				}
			}
		}
		finally
		{
			pm.close();
		}
	}
	
	public ArrayList<FoodTruck> getFavoriteFoodTrucks() throws NotLoggedInException
	{
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		ArrayList<FoodTruck> trucks = new ArrayList<FoodTruck>();
		try
		{
			Query q = pm.newQuery(Favorite.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			q.setOrdering("createDate");
			List<Favorite> favorites = (List<Favorite>) q.execute(getUser());
			for(Favorite f : favorites)
			{
				trucks.add(f.getFoodTruck());
			}
		}
		finally
		{
			pm.close();
		}
		return trucks;
	}
	
	private void checkLoggedIn() throws NotLoggedInException 
	  {
		    if (getUser() == null) {
		      throw new NotLoggedInException("Not logged in.");
		    }
		  }

	private User getUser() 
	{
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}

	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
	
	
}
