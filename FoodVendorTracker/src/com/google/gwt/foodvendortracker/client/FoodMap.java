package com.google.gwt.foodvendortracker.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;

public class FoodMap implements EntryPoint {

	private static final String API_KEY                 	 = 		"AIzaSyBDGcnhtpy_BVkfa82aOb_mSPZezrQRiWs"	; 
	private ArrayList<LatLng> coordinates					 = 		new ArrayList<LatLng>()						;
	private final FoodTruckServiceAsync foodTruckService	 = 		GWT.create(FoodTruckService.class)			;
	private VerticalPanel foodTruckPanel 					 = 		new VerticalPanel()							;
	private FlexTable foodTruckFlexTable 			  		 = 		new FlexTable()								;
	private HorizontalPanel addPanel 				 		 = 		new HorizontalPanel()						;
	private TextBox searchedFoodTruck 	 					 = 		new TextBox()								;
	private Button searchButton 							 = 		new Button("Search")						;
	private Label lastUpdatedLabel 							 = 		new Label()									;
	  
	  
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
		loadMap();	
		
	    foodTruckFlexTable.setText(0, 0, "Name")				;		
	    foodTruckFlexTable.setText(0, 1, "Rating")				;
	    foodTruckFlexTable.setText(0, 2, "Favorite")			;
	    foodTruckFlexTable.setText(0, 3, "Description")			;
	    addPanel.add(searchedFoodTruck)							;
	    addPanel.add(searchButton)								;
	    foodTruckPanel.add(addPanel)							;
	    foodTruckPanel.add(foodTruckFlexTable)					;
	    foodTruckPanel.add(lastUpdatedLabel)					;
	    RootPanel.get("nameFieldContainer").add(foodTruckPanel)	;
	    searchedFoodTruck.setFocus(true)						;
	    
	    searchButton.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          addFoodTruck()	;
	        }
	    });
	    
	    searchedFoodTruck.addKeyDownHandler(new KeyDownHandler() {
	        public void onKeyDown(KeyDownEvent event) 
	        {
	          if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
	          {
	            addFoodTruck()	;
	          }
	       }
	    });  
	}
	
	private void addFoodTruck()
	{
		final String searchQuery = searchedFoodTruck.getText().toUpperCase().trim();
		searchedFoodTruck.setFocus(true);
		
		// TODO auto complete searchQuery??
	    // TODO Add a button to favorite this foodtruck in the table.
	    // TODO Get the food trucks name, description etc.
		// TODO only plot valid food trucks on the map (call function to update map)
		
	    // TODO add valid food trucks for food item entered..?
		int row = foodTruckFlexTable.getRowCount()			;
		foodTruckFlexTable.setText(row, 0, searchQuery)		;
	    
		
		
		searchedFoodTruck.setText("");
	}


	private void loadMap() 
	{
		foodTruckService.getFoodTrucks(new AsyncCallback<List<FoodTruck>>()
		{
			@Override
			public void onFailure(Throwable caught) 
			{
				return;			
			}

			@Override
			public void onSuccess(final List<FoodTruck> result) 
			{
				double vanLat = 49.2859026428645;
				double vanLng = -123.117533501725;
				LatLng Vancouver 		= 		LatLng.newInstance(vanLat, vanLng)		;
				final MapWidget map 	= 		new MapWidget(Vancouver, 17)			;
				map.setSize("100%", "100%")				;
				map.addControl(new LargeMapControl())	;

				for(FoodTruck tr : result)
				{
					double latitude 	= 	tr.getLatitude()							;
					double longitude 	= 	tr.getLongitude()							;
					LatLng cord 		= 	LatLng.newInstance(latitude, longitude)		;
					map.addOverlay(new Marker(cord))	;
					coordinates.add(cord)				;
				}
				
				final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX)		;
				dock.addNorth(map, 500)											;				
				RootPanel.get("mapContainer").add(dock)							;	 
				
				map.addMapClickHandler(new MapClickHandler()
				{
					public void onClick(MapClickEvent e) 
					{
						boolean success = false;
						LatLng mark_coords = null;
						int counter = 0;
						for(LatLng coord : coordinates)
						{
							double LatEvent 	= 	e.getLatLng().getLatitude()		;
							double LngEvent     =   e.getLatLng().getLongitude()	;
							double LatMarker    =	coord.getLatitude()				;
							double LngMarker    =   coord.getLongitude()			;
							// result >0 LatEvent is > than LatMarker if < 0 it is less else is is equal
							
							//+0003, +- 00015
							double LatMarker_larger 	 =	 LatMarker  +  0.0003   ;
							double LatMarker_smaller	 =	 LatMarker  -  0.0003   ;  
							double LngMarker_larger		 = 	 LngMarker  +  0.00008  ;
							double LngMarker_smaller	 = 	 LngMarker  -  0.00008  ;
							
							int result1	 	= 	  Double.compare(LatEvent, LatMarker_larger)	;
							int result2 	= 	  Double.compare(LatEvent, LatMarker_smaller)	;
							int result3	 	= 	  Double.compare(LngEvent, LngMarker_larger)	;
							int result4 	= 	  Double.compare(LngEvent, LngMarker_smaller)	;
							
							//1 1 -1 1
							if((result1 <= 0 && result2 >= 0) && (result3 <= 0 && result4 >= 0))
							{
								success 		= 	true	;	
								mark_coords 	= 	coord	;
								break;
							}
							counter ++;
						}
						String description = result.get(counter).getDescription()	;
						String name = result.get(counter).getName()					;
						
						if(success == true)
						{		
							map.getInfoWindow().open(mark_coords,
									new InfoWindowContent(name +  "<br />" + description));
							
						}						
					}
				});	
			}
		});
	}
}












