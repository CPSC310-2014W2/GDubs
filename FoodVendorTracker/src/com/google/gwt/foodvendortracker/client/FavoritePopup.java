package com.google.gwt.foodvendortracker.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FavoritePopup extends PopupPanel
{
	private VerticalPanel headerPanel = new VerticalPanel();
	private VerticalPanel ratingPanel = new VerticalPanel();
	private ScrollPanel scrollPanel = new ScrollPanel();
    private VerticalPanel PopupPanelContent = new VerticalPanel(); 
    private HashMap<String, Integer> placehold = new HashMap<String, Integer>();
 private final UserFavoriteServiceAsync userFavoriteService    =         GWT.create(UserFavoriteService.class)       ;
    
    private FlexTable ratingFlexTable 			  		 = 		new FlexTable();	
	private FlexTable ratingHeader	 			  		 = 		new FlexTable();
	
		
		public FavoritePopup(final ArrayList<FoodTruck> favoriteNames) {
			super(true);


		ratingHeader.setText(0, 0, "Food Truck Name")				;		
		ratingHeader.setText(0, 1, "Remove")						;

		ratingHeader.getRowFormatter().addStyleName(0, "tableHeader")		;
		
		ratingHeader.getColumnFormatter().setWidth(0, "250px")				;
		ratingHeader.getColumnFormatter().setWidth(1, "250px")				;
		ratingHeader.getColumnFormatter().setWidth(2, "250px")				;
		ratingHeader.getColumnFormatter().setWidth(3, "250px")				;
		
		for(FoodTruck name :  favoriteNames)
		{
			displayfavo(name);
		}


		ratingFlexTable.getColumnFormatter().setWidth(0, "250px")			;
		ratingFlexTable.getColumnFormatter().setWidth(1, "250px")			;		
							
	    headerPanel.add(ratingHeader);
	    RootPanel.get("textFieldContainer").add(headerPanel);

	    ratingPanel.add(ratingFlexTable);
	    
	    scrollPanel = new ScrollPanel(ratingFlexTable);
	    scrollPanel.setSize("100%", "100%");
	    RootPanel.get("mainContent").add(scrollPanel);
	    PopupPanelContent.add(headerPanel);
	    PopupPanelContent.add(scrollPanel);
	    RootPanel.get().add(PopupPanelContent);
		setGlassEnabled(true); 
		setWidget(PopupPanelContent);
	}
	        
	        public void displayfavo(final FoodTruck foodtruck)
	        {
	        	int row = ratingFlexTable.getRowCount();
	        	
	        	ratingFlexTable.setText(row, 0, foodtruck.getName());
	        	placehold.put(foodtruck.getName(), row);
	        	
	   		 Button removeFavoriteButton = new Button("x");
	   		removeFavoriteButton.addClickHandler(new ClickHandler(){

				public void onClick(ClickEvent event) 
				{
					 removeFav(foodtruck.getId());
					 int removedIndex = placehold.get(foodtruck.getName());
					// int removedIndex = duplicate.indexOf(foodtruck);
					 ratingFlexTable.removeRow(removedIndex);
				}
	   			
	   		});
	   		ratingFlexTable.setWidget(row, 1, removeFavoriteButton); 	
	        }
	        

	        public void removeFav(final String foodtruckID)
	        {
	            userFavoriteService.removeFavorite(foodtruckID, new AsyncCallback<Void>()
	                    {
	                        public void onFailure(Throwable error) 
	                        {
	                            handleError(error);
	                            
	                        }
	     
	                        public void onSuccess(Void result) {
	                            
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

}
