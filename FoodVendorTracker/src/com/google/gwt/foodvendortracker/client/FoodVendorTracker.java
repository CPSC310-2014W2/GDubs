package com.google.gwt.foodvendortracker.client;
import java.util.ArrayList; 
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.foodvendortracker.shared.FoodTruck; 

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FoodVendorTracker implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	//private final GreetingServiceAsync greetingService = GWT
	//		.create(GreetingService.class);


	private VerticalPanel mainPanel = new VerticalPanel();  
	private VerticalPanel sidebarPanel = new VerticalPanel();  

	private static ArrayList<FoodTruck> favoriteNames  = new ArrayList<FoodTruck>();
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please sign in to your Google Account.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private Label displayLabel = new Label("");
	
	private Button uploadButton = new Button("Upload");
	
	private Label adminLabel = new Label("This is the admin page");

	private TextBox urlText = new TextBox();

	final FoodMap foodMap = new FoodMap();

	LoginInfo logInfo = new LoginInfo();
		
	private final FoodTruckServiceAsync foodTruckService = GWT.create(FoodTruckService.class);
	private final UserFavoriteServiceAsync userFavoriteService = GWT.create(UserFavoriteService.class);
	
	// creating links on sidebar
	final Anchor favLink = new Anchor("Favourite");
	final Anchor ratingLink = new Anchor("Rating");

	private void handleError(Throwable error) {
        Window.alert(error.getMessage());
        if (error instanceof NotLoggedInException) {
          Window.Location.replace(loginInfo.getLogoutUrl());
        }
      }	
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Check login status using login service.
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	    	  handleError(error); 
	      }

	      public void onSuccess(LoginInfo result) {
	    	  
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	        	if (loginInfo.emailAddress == "admin@example.com" || 
	        			loginInfo.emailAddress == "cherry.teiwee@gmail.com")
	        		loadAdmin();
	        	else {
	        		loadMain(); 
	        	}
	        } else {
	          loadLogin();
	        }
	      }
	    });
	}
	
	public void displayRating() {

	}

	public void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("mainContent").add(loginPanel);
	}

	public void loadMain() {
		loadNavigation(); 
		popUpNavigation(); 
        foodMap.loadFoodTruck(); 
	}
		
	public void loadAdmin() {	
		// Set up sign out hyperlink.
	    signOutLink.setHref(loginInfo.getLogoutUrl());
	    
		// We can add style names to widgets
	    signOutLink.setStyleName("navLink");
		uploadButton.addStyleName("uploadButton");
		adminLabel.addStyleName("adminLabel");
		urlText.addStyleName("urlText");
		displayLabel.addStyleName("displayLabel");

		// Use RootPanel.get() to get the entire body element
		RootPanel.get("textFieldContainer").add(adminLabel);
		// loading url
		RootPanel.get("textFieldContainer").add(urlText);
		RootPanel.get("uploadButtonContainer").add(uploadButton);
		RootPanel.get("uploadButtonContainer").add(displayLabel);

		sidebarPanel.add(signOutLink);
		
		RootPanel.get("navigation").add(sidebarPanel);
				
		urlText.setText("http://m.uploadedit.com/ba3a/1426033445441.txt");
		urlText.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	        	urlText.selectAll();
	        }
	    });
	    
	    // Listen for mouse events on the upload button.
	    uploadButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	    	  String urlString = urlText.getText();
	    	  foodTruckService.parseFoodTruckData(urlString, new AsyncCallback<Void>() {
	  			public void onFailure(Throwable error){
	  				displayLabel.setText("Failure to add food truck: " + error.toString());
	  			}
	  			public void onSuccess(Void ignore){
	  	    	  foodTruckService.persistFoodTruckData(new AsyncCallback<Void>(){
		    		  public void onFailure(Throwable error){
		    			  displayLabel.setText("Fail.");
		    		  }
		    		  public void onSuccess(Void ignore){
		    			  displayLabel.setText("Success! Data has been implemented.");
		    		  }
		    	  });
	  			}
	  		}); 

	      }
	    });
	}
	
	public void loadNavigation() {
	    signOutLink.setHref(loginInfo.getLogoutUrl());
	    
		signOutLink.setStyleName("navLink");
		favLink.setStyleName("navLink");
		ratingLink.setStyleName("navLink");
		
        sidebarPanel.add(signOutLink);
        sidebarPanel.add(favLink);
        sidebarPanel.add(ratingLink);
        

		RootPanel.get("navigation").add(sidebarPanel);
        RootPanel.get("mapContainer").add(mainPanel);
	}
	
	public void popUpNavigation() {
		
		favLink.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	        	loadFavorites(); 
	        	new FavoritePopup(favoriteNames).center();   
	        }
	    });
		
		ratingLink.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	        	new RatingPopup().center(); 
	        }
	    });
	}
	
	
	private void loadFavorites()
	{
		userFavoriteService.getFavoriteFoodTrucks(new AsyncCallback<ArrayList<FoodTruck>>() {
	        public void onFailure(Throwable error) 
	        {
	        	handleErrorFav(error);
	        }

			@Override
			public void onSuccess(ArrayList<FoodTruck> result) 
			{
				favoriteNames.clear();
				for(FoodTruck t : result)
				{
					if(!favoriteNames.contains(t))
					{
						favoriteNames.add(t);		
					}
				}
				
			}
	      });
	   }
	
	private void handleErrorFav(Throwable error)
	{
		Window.alert(error.getMessage());
		if(error instanceof NotLoggedInException)
		{
			Window.Location.replace("ERROR");
		}
	}

}