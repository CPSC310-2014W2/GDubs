package com.google.gwt.foodvendortracker.server;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;
import com.google.gwt.foodvendortracker.shared.FoodTruck;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Rating {
	
		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		private String id;
		@Persistent
		private String rating;
		@Persistent
		private User user;
		@Persistent
		private FoodTruck foodTruck;
		
		public Rating(){
			
		}
		
		public Rating(User user, FoodTruck foodTruck, String rating)
		{
			this();
			this.user 		= 	user		;
			this.foodTruck 	= 	foodTruck	;
			this.rating     =   rating		;
		}	
		
		public User getUser()
		{
			return this.user;
		}
		public String getId()
		{
			return this.id;
		}
		public FoodTruck getFoodTruck()
		{ 
			return this.foodTruck;
		}
		
		public void setUser(User user)
		{
			this.user = user;
		}
		
		public void setFoodTruck(FoodTruck foodTruck)
		{
			this.foodTruck = foodTruck;
		}
		public String getRating()
		{
			return this.rating;
		}
		public void setRating(String rating)
		{
			this.rating = rating;
		}
}
