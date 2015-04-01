package com.google.gwt.foodvendortracker.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.foodvendortracker.client.NotLoggedInException;
import com.google.gwt.foodvendortracker.client.RatingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RatingServiceImpl extends RemoteServiceServlet implements RatingService {
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public void addRating(String foodTruckName, int rating) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(new UserRating(foodTruckName, getUser(), rating));
			System.out.println("Successfully persisted rating: " + rating);
		}
		catch (Exception e){
			System.out.println(e);
		}
		finally {
			pm.close();
		}
	}
	
	public List<String> getAllRatingNames() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<String> ratingIds = new ArrayList<String>();
		try {
			Query q = pm.newQuery(UserRating.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<UserRating> ratings = (List<UserRating>) q.execute(getUser());
			for (UserRating rating : ratings){
				ratingIds.add(rating.getFoodTruckName());
			}
		} finally {
			pm.close();
		}
		return ratingIds;
	}
	
	
	public int getRating(String ratingName)throws NotLoggedInException{
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		int rating;
		try {
			Query q = pm.newQuery(UserRating.class, "user == u && foodTruckName == name");
			q.declareParameters("com.google.appengine.api.users.User u, String name");
			Integer val = (Integer) q.execute(getUser(), ratingName);
			rating = val;
		} finally {
			pm.close();
		}
		return rating;
	}
	
	public void removeRating(String foodTruckName) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(UserRating.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<UserRating> ratings = (List<UserRating>) q.execute(getUser());
			for (UserRating rating : ratings){
				if (foodTruckName.equals(rating.getFoodTruckName())){
					pm.deletePersistent(rating);
				}
			}
		} finally {
			pm.close();
		}
	}
	
	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {
			throw new NotLoggedInException("Not logged in.");
		}
	}
	
	private User getUser(){
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}
	
  private PersistenceManager getPersistenceManager() {
	    return PMF.getPersistenceManager();
    }
}
