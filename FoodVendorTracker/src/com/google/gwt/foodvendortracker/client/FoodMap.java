package com.google.gwt.foodvendortracker.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;


public class FoodMap implements EntryPoint {

	
	private static final String API_KEY     = "AIzaSyBDGcnhtpy_BVkfa82aOb_mSPZezrQRiWs"			; 
	private ArrayList<LatLng> coordinates = 		new ArrayList<LatLng>()						;
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
		addTestCoordinates();
		LatLng Vancouver = LatLng.newInstance(49.2869026428645, -123.117533501725);

		final MapWidget map = new MapWidget(Vancouver, 11);
		map.setSize("500px", "500px");
		// Add some controls for the zoom level
		map.addControl(new LargeMapControl());

		for(LatLng c : coordinates)
		{
			map.addOverlay(new Marker(c));
		}
		
		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		dock.addNorth(map, 500);

		// Add the map to the HTML host page
		RootPanel.get("mapContainer").add(dock); 
		//RootLayoutPanel.get().add(signOutLink);	
		
		map.addMapClickHandler(new MapClickHandler()
		{
			public void onClick(MapClickEvent e) 
			{
				boolean success = false;
				LatLng mark_coords = null;
				for(LatLng coord : coordinates)
				{
					double LatEvent 	= 	e.getLatLng().getLatitude()		;
					double LngEvent     =   e.getLatLng().getLongitude()	;
					double LatMarker    =	coord.getLatitude()				;
					double LngMarker    =   coord.getLongitude()			;
					// result >0 LatEvent is > than LatMarker if < 0 it is less else is is equal
					
					//+0003, +- 00015
					double LatMarker_larger 	 =	 LatMarker  +  0.0003   ;
					double LatMarker_smaller	 =	 LatMarker  -  0.0003    ;  
					double LngMarker_larger		 = 	 LngMarker  +  0.00008  ;
					double LngMarker_smaller	 = 	 LngMarker  -  0.00008  ;
					
					int result1	 	= 	Double.compare(LatEvent, LatMarker_larger)	;
					int result2 	= 	Double.compare(LatEvent, LatMarker_smaller)	;
					int result3	 	= 	Double.compare(LngEvent, LngMarker_larger)	;
					int result4 	= 	Double.compare(LngEvent, LngMarker_smaller)	;
					
					//1 1 -1 1
					if((result1 <= 0 && result2 >= 0) && (result3 <= 0 && result4 >= 0))
					{
						success = true;
						mark_coords = coord;
						break;
					}
					
				}
				if(success == true)
				{
					map.getInfoWindow().open(mark_coords,
						new InfoWindowContent("MARKER INFO...TODO"));			
				}
					
			}
		});
	}
	
	private void addTestCoordinates()
	{
		LatLng t1 = LatLng.newInstance(49.2867, -123.117533501725);
		coordinates.add(t1);
		LatLng t2 = LatLng.newInstance(49.2862, -123.117533501725);
		coordinates.add(t2);
		LatLng t3 = LatLng.newInstance(49.2857, -123.117533501725);
		coordinates.add(t3);
		LatLng t4 = LatLng.newInstance(49.2850, -123.117533501725);
		coordinates.add(t4);
		LatLng t5 = LatLng.newInstance(49.2843, -123.117533501725);
		coordinates.add(t5);
		LatLng t6 = LatLng.newInstance(49.2835, -123.117533501725);
		coordinates.add(t6);
	
	}


}