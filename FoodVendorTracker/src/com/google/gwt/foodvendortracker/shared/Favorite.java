package com.google.gwt.foodvendortracker.shared;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Favorite{
	
		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		private Long id;
		@Persistent
		private User user;
		@Persistent
		private String foodTruckID;
		
		public Favorite(){
			
		}
		
		public Favorite(User user, String foodTruckID)
		{
			this();
			this.user 			= 	user		;
			this.foodTruckID 	= 	foodTruckID	;
		}	
		
		public User getUser()
		{
			return this.user;
		}
		public Long getId()
		{
			return this.id;
		}
			
		public void setUser(User user)
		{
			this.user = user;
		}

		public String getFoodTruckID() {
			return foodTruckID;
		}

		public void setFoodTruckID(String foodTruckID) {
			this.foodTruckID = foodTruckID;
		}
		
}
