package com.google.gwt.foodvendortracker.client;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import java.util.logging.Logger;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;

public class FoodMap {

	private static final String API_KEY                 	 = 		"AIzaSyBDGcnhtpy_BVkfa82aOb_mSPZezrQRiWs"	; 
	private final FoodTruckServiceAsync foodTruckService	 = 		GWT.create(FoodTruckService.class)			;
	private VerticalPanel foodTruckPanel 					 = 		new VerticalPanel()							;
	private VerticalPanel headerPanel 						 = 		new VerticalPanel()							;
	private FlexTable foodTruckFlexTable 			  		 = 		new FlexTable()								;
	private FlexTable headerFlexTable	 			  		 = 		new FlexTable()								;
	private HorizontalPanel addPanel 				 		 = 		new HorizontalPanel()						;
	private SuggestBox searchedFoodTruck 	 				 = 		new SuggestBox(getVendorOracle())			;
	private Button searchButton 							 = 		new Button("Search")						;
	private Label lastUpdatedLabel 							 = 		new Label()									;
	private ScrollPanel scrollPanel							 = 		new ScrollPanel()							;
	private MapWidget map;
	
	private Image Good = new Image("/images/Good.png"); 
	private Image Okay = new Image("/images/Okay.png"); 
	private Image Meh = new Image("/images/Meh.png"); 
	private Image FML = new Image("/images/FML.png"); 
	
	private ArrayList<FoodTruck> allTruck = new ArrayList<FoodTruck>();
	private ArrayList<FoodTruck> filterTruck = new ArrayList<FoodTruck>();
	private ArrayList<FoodTruck> showTruck = new ArrayList<FoodTruck>();
	private static final Logger LOG = Logger.getLogger(FoodMap.class.getName());
	
	private String searchText ="";

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
    	fetchTruck(allTruck);

		searchedFoodTruck.setStyleName("searchBar");
		searchButton.setStyleName("searchButton");

		headerFlexTable.setText(0, 0, "Name")					;		
		headerFlexTable.setText(0, 1, "Description")			;
		headerFlexTable.setText(0, 2, "Favourite")				;
		headerFlexTable.setText(0, 3, "Rating")					;
		headerFlexTable.getRowFormatter().addStyleName(0, "tableHeader");
		headerFlexTable.getColumnFormatter().setWidth(0, "100px");
		headerFlexTable.getColumnFormatter().setWidth(1, "200px");
		headerFlexTable.getColumnFormatter().setWidth(2, "50px");
		headerFlexTable.getColumnFormatter().setWidth(3, "250px");

		foodTruckFlexTable.getColumnFormatter().setWidth(0, "100px");
		foodTruckFlexTable.getColumnFormatter().setWidth(1, "200px");
		foodTruckFlexTable.getColumnFormatter().setWidth(2, "50px");
		foodTruckFlexTable.getColumnFormatter().setWidth(3, "250px");
		
	    addPanel.add(searchedFoodTruck);
	    addPanel.add(searchButton);
	    headerPanel.add(headerFlexTable);
	    RootPanel.get("searchFieldContainer").add(addPanel);
	    RootPanel.get("textFieldContainer").add(headerPanel);

	    foodTruckPanel.add(foodTruckFlexTable)	;
	    foodTruckPanel.add(lastUpdatedLabel);
	    
	    scrollPanel = new ScrollPanel(foodTruckFlexTable);
	    scrollPanel.setSize("100%", "450px");
	    RootPanel.get("mainContent").add(scrollPanel);
	    searchedFoodTruck.setFocus(true);
	    
	    searchButton.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	        	searchTruck();
	        }
	    });
	    
	    searchedFoodTruck.addKeyDownHandler(new KeyDownHandler() {
	        public void onKeyDown(KeyDownEvent event) 
	        {
	          if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
	          {
	            searchTruck();
	          }
	       }
	    });  
	}
	
	
	private void fetchTruck(List<FoodTruck> foodTruck) {
		foodTruckService.getFoodTrucks(new AsyncCallback<List<FoodTruck>>(){
				@Override
	  			public void onFailure(Throwable error){
	  				return; 
	  			}
	  			
				@Override
				public void onSuccess(List<FoodTruck> trucks) {	
					allTruck = new ArrayList<FoodTruck>(trucks);
					setSearchTrucks(allTruck);
					displayTrucks();
				}
		});
	}
	
	private void searchTruck() 
	{
		searchedFoodTruck.setFocus(true);
		setSearch(searchedFoodTruck.getText().toLowerCase().trim());
		filterTruck();
		setSearchTrucks(filterTruck); 
		displayTrucks();
	}
	
	private void setSearch(String searchText) {
		this.searchText = searchText; 
		searchedFoodTruck.setText(searchText);
		
		searchedFoodTruck.setFocus(true);
		
		VerticalPanel formPanel = new VerticalPanel();
        formPanel.add(addPanel);
       
        RootPanel.get("searchFieldContainer").add(formPanel);
	}
	
	private void filterTruck() {
		if (searchText.equals("")) {
			filterTruck = new ArrayList<FoodTruck>(allTruck);
			return; 
		}
		
		ArrayList<FoodTruck> match = new ArrayList<FoodTruck>();
		for (FoodTruck foodTruck: allTruck) {
			String name = null;
			String description = null; 
			try {
				name = foodTruck.getName().toLowerCase();
				description = foodTruck.getDescription().toLowerCase(); 
			}
			catch (Throwable e) {
				return; 
			}
			if (name!=null && name.contains(searchText)) {
				match.add(foodTruck);
			}
			else if (description != null && description.contains(searchText)) {
				match.add(foodTruck);
			}
		}
		filterTruck = new ArrayList<FoodTruck>(match); 		
	}
	
	private void setSearchTrucks(ArrayList<FoodTruck> showTrucks) {
		this.showTruck = new ArrayList<FoodTruck>(showTrucks);
	}
	
	private void displayTrucks() {
		displayFoodTrucks(showTruck);
	}
	
	private void displayFoodTruck(FoodTruck foodTruck) 
	{
		int row = foodTruckFlexTable.getRowCount(); 
			HTML images = new HTML("<img src ='/images/Good.png'></img> "
					+ "<img src ='/images/Okay.png'></img>"
					+ "<img src ='/images/Meh.png'></img>"
					+ "<img src ='/images/FML.png'></img>", true);
			
			Good.setStyleName("Good");
			Okay.setStyleName("Okay");
			Meh.setStyleName("Meh");
			FML.setStyleName("FML");
			
			final HTML star = new HTML("<img src ='/images/star.png'>", true);
			
			
			star.addClickHandler(new ClickHandler() 
			{
	            public void onClick(ClickEvent event) 
	            {
	            	star.setStyleName("navLink");
	            }
			
	        });
			
			
			foodTruckFlexTable.setText(row, 0, foodTruck.getName());
			foodTruckFlexTable.setText(row, 1, foodTruck.getDescription());	
			foodTruckFlexTable.setWidget(row, 2, star);	
			foodTruckFlexTable.setWidget(row, 3, images);	
	}
	
	private void displayFoodTrucks(List<FoodTruck> foodTruck) {
		int row = foodTruckFlexTable.getRowCount();
		for (int i=0; i < row; i++) {
			foodTruckFlexTable.removeRow(0);
		}
		
		for (FoodTruck trucks: foodTruck)
			displayFoodTruck(trucks); 
		
		loadQueryMarkers(foodTruck);
		
	}	
	
	private MultiWordSuggestOracle getVendorOracle() {
		final MultiWordSuggestOracle vendorOracle = new MultiWordSuggestOracle();

		foodTruckService.getFoodTrucks(new AsyncCallback<List<FoodTruck>>(){
			@Override
			public void onFailure(Throwable error){
				return; 
			}

			@Override
			public void onSuccess(List<FoodTruck> trucks) {	
				allTruck = new ArrayList<FoodTruck>(trucks);
				for (FoodTruck foodTruck: allTruck) { 
					String name = foodTruck.getName();
					vendorOracle.add(name);
				}
			}
		});
        return vendorOracle;
	}

	private void loadMap() 
	{
		double vanLat = 49.2859026428645;
		double vanLng = -123.117533501725;
		LatLng Vancouver 		= 		LatLng.newInstance(vanLat, vanLng)		;
		map 	= 		new MapWidget(Vancouver, 17)			;
		map.setSize("100%", "100%")				;
		map.addControl(new LargeMapControl())	;
		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX)		;
		dock.addNorth(map, 500)											;				
		RootPanel.get("mapContainer").add(dock)							;	 
	}

	
	public void loadQueryMarkers(final List<FoodTruck> result)
	{
		map.clearOverlays()			;
		addCoords(result)			;
		Random randomGenerator = new Random()							;
		int randomInt = randomGenerator.nextInt(result.size())			;
		double Lat = result.get(randomInt).getLatitude()				;
		double Lng = result.get(randomInt).getLongitude()				;	
		LatLng center 		= 		LatLng.newInstance(Lat, Lng)		;
		map.setCenter(center);
		
		map.addMapClickHandler(new MapClickHandler()
		{
			public void onClick(MapClickEvent e) 
			{
				boolean success = false;
				LatLng mark_coords = null;
				int counter = 0;
				for(FoodTruck foodtruck : result)
				{
					
					double LatEvent 	= 	e.getLatLng().getLatitude()		;
					double LngEvent     =   e.getLatLng().getLongitude()	;
					double LatMarker    =	foodtruck.getLatitude()				;
					double LngMarker    =   foodtruck.getLongitude()			;
					LatLng coord        =   LatLng.newInstance(LatMarker, LngMarker)		;
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
	
	public void addCoords(List<FoodTruck> result)
	{
		for(FoodTruck tr : result)
		{
			double latitude 	= 	tr.getLatitude()							;
			double longitude 	= 	tr.getLongitude()							;
			LatLng cord 		= 	LatLng.newInstance(latitude, longitude)		;
			map.addOverlay(new Marker(cord))	;
			coordinates.add(cord)				;
		}	
	}
}