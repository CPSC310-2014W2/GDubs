package com.google.gwt.foodvendortracker.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.gwt.foodvendortracker.client.FoodTruckService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FoodTruckServiceImpl extends RemoteServiceServlet implements 
FoodTruckService {
	private static final Logger LOG = Logger.getLogger(FoodTruckServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = 
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public FoodTruck foodTruck = new FoodTruck("C1", "test truck", "test description", 10.00, 11.00);
	
	@Override
	public void addFoodTruck(String id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(foodTruck);
		} finally {
			pm.close();	
		}
	}

	@Override
	public String getFoodTrucks() {
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
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
}
