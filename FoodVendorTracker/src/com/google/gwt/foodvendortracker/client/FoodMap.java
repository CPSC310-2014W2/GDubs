package com.google.gwt.foodvendortracker.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FoodMap implements EntryPoint {

	private static final String API_KEY     = "AIzaSyBDGcnhtpy_BVkfa82aOb_mSPZezrQRiWs"			; 
	 private LoginInfo loginInfo 			= 		null										;
	  private VerticalPanel loginPanel 		= 		new VerticalPanel()							;
	  private Anchor signInLink 			= 		new Anchor("Sign In")						;
	  private Anchor signOutLink 			= 		new Anchor("Sign Out")						;
	  private Label loginLabel 				= 		new Label(
	      "Please sign in to your Google Account to access the StockWatcher application.")		;


	  public void onModuleLoad()
	  {
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
	    
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() 
	    {
	    	public void onFailure(Throwable error) 
	    	{
	      
	    	}

	    	public void onSuccess(LoginInfo result) 
	    	{
	    	  loginInfo = result;
	    	  if(loginInfo.isLoggedIn()) 
	    	  {
	        	loadFoodTruck();
	    	  } 
	    	  else 
	    	  {
	    		  loadLogin();
	    	  }
	    	}
	    });
	  }

	  private void loadLogin() 
	  {
	    signInLink.setHref(loginInfo.getLoginUrl())				;
	    loginPanel.add(loginLabel)								;
	    loginPanel.add(signInLink)								;
	    RootPanel.get("foodvendortracker").add(loginPanel)		;
	  }

	private void loadFoodTruck() 
	{
		signOutLink.setHref(loginInfo.getLogoutUrl());
		Maps.loadMapsApi(API_KEY, "2", false, new Runnable() 
		{
			public void run() 
			{
				buildUi();
			}
		});
	}

	private void buildUi() 
	{
		// Open a map centered on Cawker City, KS USA
		LatLng cawkerCity = LatLng.newInstance(39.509, -98.434);

		final MapWidget map = new MapWidget(cawkerCity, 2);
		map.setSize("500px", "500px");
		// Add some controls for the zoom level
		map.addControl(new LargeMapControl());

		// Add a marker
		map.addOverlay(new Marker(cawkerCity));

		// Add an info window to highlight a point of interest
		map.getInfoWindow().open(map.getCenter(),
				new InfoWindowContent("World's Largest Ball of Sisal Twine"));

		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		dock.addNorth(map, 500);

		// Add the map to the HTML host page

		RootLayoutPanel.get().add(dock)				;
		RootLayoutPanel.get().add(signOutLink)		;	
	}

}
