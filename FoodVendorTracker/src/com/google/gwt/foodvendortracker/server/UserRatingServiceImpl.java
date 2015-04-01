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
import com.google.gwt.foodvendortracker.client.ClientRating;
import com.google.gwt.foodvendortracker.client.NotLoggedInException;
import com.google.gwt.foodvendortracker.client.UserRatingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class UserRatingServiceImpl extends RemoteServiceServlet implements UserRatingService {
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public void addRating(String foodTruckName, int rating) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(new UserRating(foodTruckName, getUser(), rating));
			System.out.println("Successfully persisted " + rating + "-star rating for " + foodTruckName);
		}
		finally {
			pm.close();
		}
	}
	
//	public List<String> getAllRatingNames() throws NotLoggedInException {
//		checkLoggedIn();
//		PersistenceManager pm = getPersistenceManager();
//		List<String> ratingIds = new ArrayList<String>();
//		try {
//			Query q = pm.newQuery(UserRating.class, "user == u");
//			q.declareParameters("com.google.appengine.api.users.User u");
//			List<UserRating> ratings = (List<UserRating>) q.execute(getUser());
//			for (UserRating rating : ratings){
//				ratingIds.add(rating.getFoodTruckName());
//			}
//		} finally {
//			pm.close();
//		}
//		return ratingIds;
//	}
	
	
//	public int getRating(String ratingName)throws NotLoggedInException{
//		checkLoggedIn();
//		PersistenceManager pm = getPersistenceManager();
//		int rating;
//		try {
//			Query q = pm.newQuery(UserRating.class, "user == u && foodTruckName == name");
//			q.declareParameters("com.google.appengine.api.users.User u, String name");
//			Integer val = (Integer) q.execute(getUser(), ratingName);
//			rating = val;
//		} finally {
//			pm.close();
//		}
//		return rating;
//	}
	
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
	
	public int getCurrentUserRating(String foodTruckName) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		int returnRating = 0;
		try {
			Query q = pm.newQuery(UserRating.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<UserRating> ratings = (List<UserRating>) q.execute(getUser());
			for (UserRating rating : ratings){
				if (foodTruckName.equals(rating.getFoodTruckName())){
					returnRating = rating.getRating();
				}
			}
		} finally {
			pm.close();
		}
		System.out.println("The current rating for " + foodTruckName + " is " + returnRating);
		return returnRating;
	}
	
	public List<ClientRating> getRatings(List<String> foodTruckName) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<ClientRating> clientRatings = new ArrayList<ClientRating>();
		try{
			Query q = pm.newQuery(UserRating.class, "rating > rating_threshold");
			q.declareParameters("int rating_threshold");
			List<UserRating> ratings = (List<UserRating>) q.execute(0);
			for (String name : foodTruckName){
				for (UserRating rating : ratings){
					if (name.equals(rating.getFoodTruckName())){
					ClientRating clientRating = new ClientRating(name, rating.getRating());
					clientRatings.add(clientRating);
					}
				}
			}
		} finally {
			pm.close();
		}
		return clientRatings;
	}
	
//	private List<UserRating> queryForRatingsByUser() {
//		PersistenceManager pm = getPersistenceManager();
//		Query q = pm.newQuery(UserRating.class, "user == u");
//		q.declareParameters("com.google.appengine.api.users.User u");
//		List<UserRating> ratings = (List<UserRating>) q.execute(getUser());
//		pm.close();
//		return ratings;
//	}
	
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
