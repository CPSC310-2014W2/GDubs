package com.google.gwt.foodvendortracker.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.foodvendortracker.server.FoodTruck;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class FoodMap implements EntryPoint {
	
	private static final String API_KEY = "AIzaSyBDGcnhtpy_BVkfa82aOb_mSPZezrQRiWs";
	private final FoodTruckServiceAsync foodTruckService = GWT.create(FoodTruckService.class);
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private Label displayLabel = new Label();
	private Button showDisplayButton = new Button("Click me");
	
	// GWT module entry point method.
	public void onModuleLoad() {
		/*
		 * Asynchronously loads the Maps API.
		 *
		 * The first parameter should be a valid Maps API Key to deploy this
		 * application on a public server, but a blank key will work for an
		 * application served from localhost.
		 */
		Maps.loadMapsApi(API_KEY, "2", false, new Runnable() {
			public void run() {
				buildUi();
			}
		});
	}

	private void buildUi() {
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
		//RootLayoutPanel.get().add(dock);
		
		addPanel.add(displayLabel);
		addPanel.add(showDisplayButton);
		
		mainPanel.add(addPanel);
		
		RootLayoutPanel.get().add(mainPanel);
		
		showDisplayButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				addTruck();
				showDisplay();
			}
		});
	}
	
	private void addTruck(){
		foodTruckService.addFoodTruck("C1", new AsyncCallback<Void>(){
			public void onFailure(Throwable error){
				displayLabel.setText("Failure to add food truck: " + error.toString());
			}
			public void onSuccess(Void ignore){
				return;
			}
		});
	}
	
	private void showDisplay() {
		foodTruckService.getFoodTrucks(new AsyncCallback<String>(){
		      public void onFailure(Throwable error) {
		    	  displayLabel.setText("Failure to display food truck: " + error.toString());
		      }
		      public void onSuccess(String point) {
		        displayLabel.setText(point);
		      }
		});
	}
}
