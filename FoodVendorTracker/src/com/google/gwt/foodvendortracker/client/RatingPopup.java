package com.google.gwt.foodvendortracker.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RatingPopup extends PopupPanel{
	private VerticalPanel headerPanel = new VerticalPanel();
	private VerticalPanel ratingPanel = new VerticalPanel();
	private ScrollPanel scrollPanel = new ScrollPanel();
    private VerticalPanel PopupPanelContent = new VerticalPanel(); 
    
    private FlexTable ratingFlexTable 			  		 = 		new FlexTable();							
	private FlexTable ratingHeader	 			  		 = 		new FlexTable();
		
		public RatingPopup() {
			super(true);

		ratingHeader.setText(0, 0, "Name")									;		
		ratingHeader.setText(0, 1, "Average Rating")						;
		ratingHeader.setText(0, 2, "Your Rating")							;
		ratingHeader.setText(0, 3, "Change Rating")							;

		ratingHeader.getRowFormatter().addStyleName(0, "tableHeader")		;
		
		ratingHeader.getColumnFormatter().setWidth(0, "250px")				;
		ratingHeader.getColumnFormatter().setWidth(1, "250px")				;
		ratingHeader.getColumnFormatter().setWidth(2, "250px")				;
		ratingHeader.getColumnFormatter().setWidth(3, "250px")				;
		
		ratingFlexTable.setText(0, 0, "test1")								;		
		ratingFlexTable.setText(0, 1, "value1")								;
		ratingFlexTable.setText(1, 0, "test2")								;		
		ratingFlexTable.setText(1, 1, "value2")								;
		
		ratingFlexTable.getColumnFormatter().setWidth(0, "250px")			;
		ratingFlexTable.getColumnFormatter().setWidth(1, "250px")			;
		ratingFlexTable.getColumnFormatter().setWidth(2, "250px")			;
		ratingFlexTable.getColumnFormatter().setWidth(3, "250px")			;				
							
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

}
