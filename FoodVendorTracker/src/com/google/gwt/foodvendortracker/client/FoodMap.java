package com.google.gwt.foodvendortracker.client;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

import org.cobogw.gwt.user.client.ui.Rating;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;

public class FoodMap {

	private static final String API_KEY                 	 = 		"AIzaSyBDGcnhtpy_BVkfa82aOb_mSPZezrQRiWs"	; 
	private final FoodTruckServiceAsync foodTruckService	 = 		GWT.create(FoodTruckService.class)			;
	private final UserRatingServiceAsync ratingService		 = 		GWT.create(UserRatingService.class)			;
	private VerticalPanel foodTruckPanel 					 = 		new VerticalPanel()							;
	private VerticalPanel headerPanel 						 = 		new VerticalPanel()							;
	private FlexTable foodTruckFlexTable 			  		 = 		new FlexTable()								;
	private FlexTable headerFlexTable	 			  		 = 		new FlexTable()								;
	private HorizontalPanel addPanel 				 		 = 		new HorizontalPanel()						;
	private SuggestBox searchedFoodTruck 	 				 = 		new SuggestBox(getVendorOracle())			;
	private Button searchButton 							 = 		new Button("Search")						;
	private Label lastUpdatedLabel 							 = 		new Label()									;
	private ScrollPanel scrollPanel							 = 		new ScrollPanel()							;
	private ArrayList<FoodTruck> allTruck 					 = 		new ArrayList<FoodTruck>()					;
	private ArrayList<FoodTruck> filterTruck				 = 		new ArrayList<FoodTruck>()					;
	private ArrayList<FoodTruck> showTruck 					 = 		new ArrayList<FoodTruck>()					; 
	private String searchText 								 =		""											;
	private MapWidget map																						;
	 private final UserFavoriteServiceAsync userFavoriteService    =         GWT.create(UserFavoriteService.class)       ;
	 private static String errorResult                                =      "Error adding foodTruck to favorites"       ;
	    private ArrayList<String> addedFavoriteNames     = new ArrayList<String>();
	    private static String currentName = "";
	
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
    	truckHandler(); 
    	
		searchedFoodTruck.setStyleName("searchBar");
		searchButton.setStyleName("searchButton");

		headerFlexTable.setText(0, 0, "Name")									;		
		headerFlexTable.setText(0, 1, "Description")							;
		headerFlexTable.setText(0, 2, "Favourite")								;
		headerFlexTable.setText(0, 3, "Rating")									;
		headerFlexTable.getRowFormatter().addStyleName(0, "tableHeader")		;
		headerFlexTable.getColumnFormatter().setWidth(0, "150px")				;
		headerFlexTable.getColumnFormatter().setWidth(1, "200px")				;
		headerFlexTable.getColumnFormatter().setWidth(2, "100px")				;	
		headerFlexTable.getColumnFormatter().setWidth(3, "150px")				;
		headerFlexTable.getColumnFormatter().setWidth(4, "100px")				;

		foodTruckFlexTable.getColumnFormatter().setWidth(0, "150px")			;
		foodTruckFlexTable.getColumnFormatter().setWidth(1, "200px")			;
		foodTruckFlexTable.getColumnFormatter().setWidth(2, "100px")			;
		foodTruckFlexTable.getColumnFormatter().setWidth(3, "150px")			;
		foodTruckFlexTable.getColumnFormatter().setWidth(4, "100px")			;

		
	    addPanel.add(searchedFoodTruck)							;
	    addPanel.add(searchButton)								;
	    headerPanel.add(headerFlexTable)						;
	    RootPanel.get("searchFieldContainer").add(addPanel)		;
	    RootPanel.get("textFieldContainer").add(headerPanel)	;

	    foodTruckPanel.add(foodTruckFlexTable)	;
	    foodTruckPanel.add(lastUpdatedLabel)	;
	    
	    scrollPanel = new ScrollPanel(foodTruckFlexTable)	;
	    scrollPanel.setSize("100%", "100%")				;
	    RootPanel.get("mainContent").add(scrollPanel)		;
	    searchedFoodTruck.setFocus(true)					;
	}
	
	private void truckHandler() {
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
		searchedFoodTruck.setFocus(true)							;
		setSearch(searchedFoodTruck.getText().toLowerCase().trim())	;
		filterTruck()												;
		setSearchTrucks(filterTruck)								; 
		displayTrucks()												;
	}
	
	private void setSearch(String searchText) 
	{
		this.searchText = searchText							; 
		searchedFoodTruck.setText(searchText)					;	
		VerticalPanel formPanel = new VerticalPanel()			;
        formPanel.add(addPanel)									;  
        RootPanel.get("searchFieldContainer").add(formPanel)	;
		searchedFoodTruck.setFocus(true)						;

	}
	
	private void filterTruck() 
	{
		if (searchText.equals("")) 
		{
			filterTruck = new ArrayList<FoodTruck>(allTruck);
			return; 
		}
		
		ArrayList<FoodTruck> match = new ArrayList<FoodTruck>();
		for (FoodTruck foodTruck: allTruck) 
		{
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
	
	private void setSearchTrucks(ArrayList<FoodTruck> showTrucks) 
	{
		this.showTruck = new ArrayList<FoodTruck>(showTrucks);
	}
	
	private void displayTrucks() 
	{
		displayFoodTrucks(showTruck);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void displayFoodTruck(final FoodTruck foodTruck) 
	{
		final int row = foodTruckFlexTable.getRowCount(); 

			Rating rating = new Rating(0, 5, 1, "/images/cbg-star.png", "/images/cbg-stardeselected.png", "/images/cbg-starhover.png", 16, 16);
			final Label lbl2 = new Label("selected: " + rating.getValue());
			final HTML clickedStar = new HTML("<img src ='/images/star-clicked.png'>", true);
			final String name = foodTruck.getName();
			
					
			clickedStar.addClickHandler(new ClickHandler() 
            {
                public void onClick(ClickEvent event) 
                {
                	foodTruckFlexTable.setWidget(row, 2, clickedStar);
                    currentName = foodTruck.getName();
                    addFavorite(foodTruck.getId());
                    clickedStar.setStyleName("navLink");
                    
                }
            
            });

			foodTruckFlexTable.setText(row, 0, foodTruck.getName());
			foodTruckFlexTable.setText(row, 1, foodTruck.getDescription());	
			foodTruckFlexTable.setWidget(row, 2, clickedStar);	
			foodTruckFlexTable.setWidget(row, 3, rating);
			foodTruckFlexTable.setWidget(row, 4, lbl2);
			rating.addValueChangeHandler(new ValueChangeHandler() {
				  public void onValueChange(ValueChangeEvent event) {
					String tempString = (String) event.getValue().toString();
					int ratingInt = Integer.parseInt(tempString);
				    lbl2.setText("you selected: " + tempString);
				    System.out.println(event.getValue());
				    ratingService.addRating(name, ratingInt, new AsyncCallback<Void>(){
				    	@Override
				    	public void onFailure(Throwable caught){
				    		System.out.println(caught);
				    	}
				    	@Override	
				    	public void onSuccess(Void e){
				    		System.out.println("successfully returned ratings");
				    	}
				    });
				  }
				  });
	}
	
	private void displayFoodTrucks(List<FoodTruck> foodTruck) 
	{
		int row = foodTruckFlexTable.getRowCount();
		for (int i=0; i < row; i++) {
			foodTruckFlexTable.removeRow(0);
		}
		
		for (FoodTruck trucks: foodTruck)
			displayFoodTruck(trucks); 
		
		loadQueryMarkers(foodTruck);
		
	}	
	
	private MultiWordSuggestOracle getVendorOracle() 
	{
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
	
	public void addFavorite(final String foodtruckID)
    {
        
        userFavoriteService.addFavorite(foodtruckID, new AsyncCallback<String>()
                {
                    public void onFailure(Throwable error)
                    {
                        new errorpop().center();
                        handleError(error);
                    }
                    public void onSuccess(String result)
                    {
                        if(result.equals("added"))
                        {
                            addedFavoriteNames.add(currentName);
                            new pop().center();
                        }
                        else
                        {                       
                            new existspop().center();
                        }
 
                    }
                });
    }
    
    public void removeFav(final String foodtruckID)
    {
        userFavoriteService.removeFavorite(foodtruckID, new AsyncCallback<Void>()
                {
                    public void onFailure(Throwable error) 
                    {
                        new errorpop().center();
                        handleError(error);
                        
                    }
 
                    public void onSuccess(Void result) {
                        for(int i=0; i<addedFavoriteNames.size(); i++)
                        {
                            String cur = addedFavoriteNames.get(i);
                            
                            if(cur.equals(currentName))
                            {
                                addedFavoriteNames.remove(i);
                            }
                        }
                    }
            
                });
    }
    
    
    private void handleError(Throwable error)
    {
        Window.alert(error.getMessage());
        if(error instanceof NotLoggedInException)
        {
            Window.Location.replace("ERROR");
        }
    }

	private void loadMap() 
	{
		double vanLat 	 = 		49.2859026428645						;
		double vanLng 	 = 		-123.117533501725						;
		LatLng Vancouver = 		LatLng.newInstance(vanLat, vanLng)		;
		map 			 = 		new MapWidget(Vancouver, 17)			;
		map.setSize("100%", "100%")										;
		map.addControl(new LargeMapControl())							;
		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX)		;
		dock.addNorth(map, 600)											;				
		RootPanel.get("mapContainer").add(dock)							;	 
	}

	
	public void loadQueryMarkers(final List<FoodTruck> result)
	{
		map.clearOverlays()			;
		addCoords(result)			;
		Random randomGenerator = new Random()							;
		int randomInt = randomGenerator.nextInt(result.size())			;
		double lat = result.get(randomInt).getLatitude()				;
		double lng = result.get(randomInt).getLongitude()				;	
		LatLng center 		= 		LatLng.newInstance(lat, lng)		;
		map.setCenter(center);
		
		map.addMapClickHandler(new MapClickHandler()
		{
			public void onClick(MapClickEvent e) 
			{
				boolean success 	=	 false	;
				LatLng mark_coords 	=	 null	;
				int counter 		= 	 0		;
				
				for(FoodTruck foodtruck : result)
				{
					
					double LatEvent 	= 	e.getLatLng().getLatitude()						;
					double LngEvent     =   e.getLatLng().getLongitude()					;
					double LatMarker    =	foodtruck.getLatitude()							;
					double LngMarker    =   foodtruck.getLongitude()						;
					double lateral      =   0.0003											;
					double vertical     =   0.00008											;
					LatLng coord        =   LatLng.newInstance(LatMarker, LngMarker)		;
					int zero			=   0												;
					
					
					double LatMarker_larger 	 =	 LatMarker  +  lateral   ;
					double LatMarker_smaller	 =	 LatMarker  -  lateral   ;  
					double LngMarker_larger		 = 	 LngMarker  +  vertical  ;
					double LngMarker_smaller	 = 	 LngMarker  -  vertical  ;
					
					
					int result1	 	= 	  Double.compare(LatEvent, LatMarker_larger)	;
					int result2 	= 	  Double.compare(LatEvent, LatMarker_smaller)	;
					int result3	 	= 	  Double.compare(LngEvent, LngMarker_larger)	;
					int result4 	= 	  Double.compare(LngEvent, LngMarker_smaller)	;
					
			
					if((result1 <= zero && result2 >= zero) && (result3 <= zero && result4 >= zero))
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
			map.addOverlay(new Marker(cord))									;
		}	
	}


private static class pop extends PopupPanel {
    public pop() {
        super(true);
        setWidget(new HTML("Added " + currentName + " to your favorites!!"));
    }
}

private static class errorpop extends PopupPanel {
    public errorpop() {
        super(true);
        setWidget(new HTML(errorResult));
    }
}

private static class existspop extends PopupPanel {
    public existspop() {
        super(true);
        setWidget(new HTML(currentName + " already exists as a favorite!"));
    }
}
}