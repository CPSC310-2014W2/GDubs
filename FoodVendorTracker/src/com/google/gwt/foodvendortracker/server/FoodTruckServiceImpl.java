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
import com.google.gwt.foodvendortracker.client.RatingService;
import com.google.gwt.foodvendortracker.client.UserFavoriteService;
import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.foodvendortracker.shared.Favorite;


public class FoodTruckServiceImpl extends RemoteServiceServlet implements FoodTruckService, UserFavoriteService {
	private static final PersistenceManagerFactory PMF = 
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
//    private String urlString = "http://m.uploadedit.com/ba3a/1426033445441.txt";
	private List<FoodTruck> foodTrucks = new ArrayList<>();
	private FoodTruckParser foodTruckParser; 
	
	@Override
	public void parseFoodTruckData(String urlString) {
		foodTruckParser = new FoodTruckParser(urlString);
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
		System.out.println(list.size());
		List<FoodTruck> temp = new ArrayList<FoodTruck>(list);
		return temp;
	}
	
	@Override
	public void deleteFoodTruckData() 
	{
		PersistenceManager pm = getPersistenceManager();
		pm.deletePersistent(foodTrucks);
	}
	
	public String addFavorite(String foodtruckID) throws NotLoggedInException
	{
		String result = "false";
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try
		{
			boolean exists = false;
			Query q = pm.newQuery(Favorite.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<Favorite> favorites = (List<Favorite>) q.execute(getUser());
			for(Favorite f : favorites)
			{
				if(f.getFoodTruckID().equals(foodtruckID))
				{
					exists = true;
					break;
				}
			}
			if(exists == false)
			{
				Favorite favorite = new Favorite();
				favorite.setFoodTruckID(foodtruckID);
				favorite.setUser(getUser());
				pm.makePersistent(favorite);
				result = "added";
			}
		}
		finally
		{
			pm.close();
		}
		return result;
	}
	
	public void removeFavorite(String foodtruckID) throws NotLoggedInException
	{
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try
		{
			Query q = pm.newQuery(Favorite.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<Favorite> favorites = (List<Favorite>) q.execute(getUser());
			for(Favorite f : favorites)
			{
				if(f.getFoodTruckID().equals(foodtruckID))
				{
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
			List<Favorite> ids = (List<Favorite>) q.execute(getUser());
			FoodTruck foodtruck;
			for(Favorite id : ids)
			{
				foodtruck = getFoodTruckFromID(id.getFoodTruckID());
				trucks.add(foodtruck);
			}
		}
		finally
		{
			pm.close();
		}
		return trucks;
	}

	
	public void addRating(String rating, FoodTruck foodTruck) throws NotLoggedInException
	{
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try 
		{
			pm.makePersistent(new Rating(getUser(), foodTruck, rating));
		}
		finally
		{
			pm.close();
		}
	}
	
	public void removeRating(FoodTruck foodTruck) throws NotLoggedInException
	{
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try
		{
			long deleteCount = 0;
			Query q = pm.newQuery();
			q.declareParameters("com.google.appengine.api.users.User u");
			List<Rating> ratings = (List<Rating>) q.execute(getUser());
			for(Rating f : ratings)
			{
				if(f.getFoodTruck().getName().equals(foodTruck.getName()))
				{
					deleteCount++;
					pm.deletePersistent(f);
				}
			}
		}
		finally
		{
			pm.close();
		}
	}
	
	  public ArrayList<FoodTruck> getRatings() throws NotLoggedInException
	  {
		    checkLoggedIn();
			PersistenceManager pm = getPersistenceManager();
			ArrayList<FoodTruck> ratedTrucks = new ArrayList<FoodTruck>();
			try
			{
				Query q = pm.newQuery(Favorite.class, "user == u");
				q.declareParameters("com.google.appengine.api.users.User u");
				q.setOrdering("createDate");
				List<Rating> ratings = (List<Rating>) q.execute(getUser());
				for(Rating f : ratings)
				{
					ratedTrucks.add(f.getFoodTruck());
				}
			}
			finally
			{
				pm.close();
			}
			return ratedTrucks;
		
	  }
	  
      private FoodTruck getFoodTruckFromID(String id)
	        {
	                PersistenceManager pm = getPersistenceManager();
	                FoodTruck result = pm.getObjectById(FoodTruck.class, id);
	                return result;
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
