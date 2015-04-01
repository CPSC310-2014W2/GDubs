package com.google.gwt.foodvendortracker.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.foodvendortracker.shared.FoodTruck;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FoodVendorTracker implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	// private final GreetingServiceAsync greetingService = GWT
	// .create(GreetingService.class);

	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel sidebarPanel = new VerticalPanel();
	
	private FlexTable ratingFlexTable = new FlexTable();
	
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private Label displayLabel = new Label("");

	private Button uploadButton = new Button("Upload");

	private Label adminLabel = new Label("This is the admin page");
	private Label userLabel = new Label("This is the non-admin page");

	final FoodMap foodMap = new FoodMap();

	LoginInfo logInfo = new LoginInfo();

	private final FoodTruckServiceAsync foodTruckService = GWT
			.create(FoodTruckService.class);
	private final UserFavoriteServiceAsync userFavoriteService = GWT
			.create(UserFavoriteService.class);
	private final RatingServiceAsync ratingService = GWT
			.create(RatingService.class);
	// creating links on sidebar
	private Anchor favLink = new Anchor("Favourite" + "\n\r" + "\u2665");// heart
																			// icon
	private Anchor ratingLink = new Anchor("Rating" + "\n\r" + "\u2605"); // star
																			// icon

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
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
						handleError(error);
					}

					public void onSuccess(LoginInfo result) {

						loginInfo = result;
						if (loginInfo.isLoggedIn()) {
							if (loginInfo.emailAddress == "admin@example.com"
									|| loginInfo.emailAddress == "cherry.teiwee@gmail.com"
									|| loginInfo.emailAddress == "tyler0310@gmail.com")
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

	public void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("mainContent").add(loginPanel);
	}

	public void loadMain() {
		signOutLink.setHref(loginInfo.getLogoutUrl());

		RootPanel.get("textFieldContainer").add(userLabel);
		signOutLink.setStyleName("navLink");
		favLink.setStyleName("navLink");
		ratingLink.setStyleName("navLink");

		sidebarPanel.add(signOutLink);
		sidebarPanel.add(favLink);
		sidebarPanel.add(ratingLink);

		// TODO Associate the Main panel with the HTML host page.
		RootPanel.get("navigation").add(sidebarPanel);
		RootPanel.get("mapContainer").add(mainPanel);
		foodMap.loadFoodTruck();

	}

	public void loadAdmin() {
		// Set up sign out hyperlink.
		signOutLink.setHref(loginInfo.getLogoutUrl());

		// We can add style names to widgets
		signOutLink.setStyleName("navLink");
		uploadButton.addStyleName("uploadButton");

		// Use RootPanel.get() to get the entire body element
		RootPanel.get("textFieldContainer").add(adminLabel);
		RootPanel.get("textFieldContainer").add(displayLabel);
		RootPanel.get("uploadButtonContainer").add(uploadButton);
		sidebarPanel.add(signOutLink);

		// TODO Associate the Main panel with the HTML host page.
		RootPanel.get("navigation").add(sidebarPanel);

		// Listen for mouse events on the upload button.
		uploadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				foodTruckService.parseFoodTruckData(new AsyncCallback<Void>() {
					public void onFailure(Throwable error) {
						displayLabel.setText("Failure to add food truck: "
								+ error.toString());
					}

					public void onSuccess(Void ignore) {
						foodTruckService
								.persistFoodTruckData(new AsyncCallback<Void>() {
									public void onFailure(Throwable error) {
										displayLabel.setText("Fail");
									}

									public void onSuccess(Void ignore) {
										displayLabel.setText("SUCCESS!");
									}
								});
					}
				});
			}
		});

		ratingLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final List<String> ratedFoodTrucks = new ArrayList<String>();
				final List<Integer> foodTruckRatings = new ArrayList<Integer>();
				ratingService
						.getAllRatingNames(new AsyncCallback<List<String>>() {
							@Override
							public void onFailure(Throwable caught) {
								System.out.println(caught);
							}

							@Override
							public void onSuccess(final List<String> result) {
								for (String s : result) {
									ratedFoodTrucks.add(s);
									ratingService.getRating(s,
											new AsyncCallback<Integer>() {

												@Override
												public void onFailure(
														Throwable caught) {
													return;
												}

												@Override
												public void onSuccess(
														final Integer i) {
													foodTruckRatings.add(i);
												}
											});
								}
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
}