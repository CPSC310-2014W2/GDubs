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
import com.google.gwt.user.client.ui.RootPanel;


public class FoodMap implements EntryPoint {

	
	private static final String API_KEY     = "AIzaSyBDGcnhtpy_BVkfa82aOb_mSPZezrQRiWs"			; 
	
	 public void onModuleLoad()
	  {
		 loadFoodTruck();
	  }



	public void loadFoodTruck() 
	{
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
		// Open a map centered on Pizza place 
		LatLng cawkerCity = LatLng.newInstance(49.2869026428645, -123.117533501725);

		final MapWidget map = new MapWidget(cawkerCity, 2);
		map.setSize("100%", "100%");
		// Add some controls for the zoom level
		map.addControl(new LargeMapControl());

		// Add a marker
		map.addOverlay(new Marker(cawkerCity));

		// Add an info window to highlight a point of interest
		map.getInfoWindow().open(map.getCenter(),
				new InfoWindowContent("testInfoWindow"));

		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		dock.addNorth(map, 500);

		// Add the map to the HTML host page
		RootPanel.get("mapContainer").add(dock); 
	}

}