package com.google.gwt.foodvendortracker.client;

import com.google.gwt.foodvendortracker.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FoodVendorTracker implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);


	private VerticalPanel mainPanel = new VerticalPanel();  
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please sign in to your Google Account.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	
	private Button uploadButton = new Button("Upload");
	private Button deleteButton = new Button("Delete");
	
	private Label adminLabel = new Label("This is the admin page");
	private Label userLabel = new Label("This is the non-admin page");

	final FoodMap foodMap = new FoodMap();


	LoginInfo logInfo = new LoginInfo();
	
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
	        			loginInfo.emailAddress == "admin1@example.com")
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
	    
		uploadButton.addStyleName("uploadButton");
        RootPanel.get("sendButtonContainer").add(uploadButton);
        mainPanel.add(signOutLink);
        // TODO Associate the Main panel with the HTML host page. 
		RootPanel.get("mainContent").add(mainPanel);
		
        foodMap.loadFoodTruck(); 

	}
		
	public void loadAdmin() {	
		// Set up sign out hyperlink.
	    signOutLink.setHref(loginInfo.getLogoutUrl());
		
	    final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		uploadButton.addStyleName("uploadButton");
		deleteButton.addStyleName("deleteButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(adminLabel);
		RootPanel.get("nameFieldContainer").add(nameField);	
		RootPanel.get("sendButtonContainer").add(uploadButton);
		RootPanel.get("sendButtonContainer").add(deleteButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);
		
		mainPanel.add(signOutLink);
		
		// TODO Associate the Main panel with the HTML host page. 
		RootPanel.get("mainContent").add(mainPanel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll(); 
		
		// Listen for mouse events on the upload button.
	    uploadButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        foodMap.loadFoodTruck();
	      }
	    });

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				deleteButton.setEnabled(true);
				deleteButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				deleteButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
	    deleteButton.addClickHandler(handler); 
		nameField.addKeyUpHandler(handler);
	
	}
}
