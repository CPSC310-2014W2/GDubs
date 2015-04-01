//package com.google.gwt.foodvendortracker.server;
//
//import java.io.Serializable;
//
//import javax.jdo.annotations.IdGeneratorStrategy;
//import javax.jdo.annotations.IdentityType;
//import javax.jdo.annotations.PersistenceCapable;
//import javax.jdo.annotations.Persistent;
//import javax.jdo.annotations.PrimaryKey;
//import com.google.appengine.api.users.User;
//import com.google.gwt.foodvendortracker.shared.FoodTruck;
//
//@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
//public class Rating {
//	
//		@PrimaryKey
//		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
//		private Long id;
//		@Persistent
//		private String rating;
//		@Persistent
//		private User user;
//		@Persistent
//		private FoodTruck foodTruck;
//		
//		public Rating(){
//			
//		}
//		
//		public Rating(User user, FoodTruck foodTruck, String rating)
//		{
//			this();
//			this.user 		= 	user		;
//			this.foodTruck 	= 	foodTruck	;
//			this.rating     =   rating		;
//		}	
//		
//		public User getUser()
//		{
//			return this.user;
//		}
//		public Long getId()
//		{
//			return this.id;
//		}
//		public FoodTruck getFoodTruck()
//		{ 
//			return this.foodTruck;
//		}
//		
//		public void setUser(User user)
//		{
//			this.user = user;
//		}
//		
//		public void setFoodTruck(FoodTruck foodTruck)
//		{
//			this.foodTruck = foodTruck;
//		}
//		public String getRating()
//		{
//			return this.rating;
//		}
//		public void setRating(String rating)
//		{
//			this.rating = rating;
//		}
//}


package com.google.gwt.foodvendortracker.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Rating {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private User user;
	@Persistent
	private String foodTruckID;
	@Persistent
	private int rating;
	
	public Rating(){
		
	}
	
	public Rating(User user, String foodTruckID, int rating){
		this();
		this.user = user;
		this.foodTruckID = foodTruckID;
		this.rating = rating;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFoodTruckID() {
		return foodTruckID;
	}

	public void setFoodTruckID(String foodTruckID) {
		this.foodTruckID = foodTruckID;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
}