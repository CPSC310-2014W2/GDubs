package com.google.gwt.foodvendortracker.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.gwt.foodvendortracker.client.FoodTruckService;
import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FoodTruckServiceImpl extends RemoteServiceServlet implements 
FoodTruckService {
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
			pm.makePersistent(foodTrucks);
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
	public List<FoodTruck> getFoodTrucks() {
		List<FoodTruck> list = null;
		PersistenceManager pm = getPersistenceManager();
		Query query = pm.newQuery(FoodTruck.class);
		list = (List<FoodTruck>) query.execute();
		return list;
	}
	
	@Override
	public void deleteFoodTruckData() {
		PersistenceManager pm = getPersistenceManager();
		pm.deletePersistent(foodTrucks);
	}
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
}
