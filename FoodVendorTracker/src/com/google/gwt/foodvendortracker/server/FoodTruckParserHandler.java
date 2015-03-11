package com.google.gwt.foodvendortracker.server;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FoodTruckParserHandler extends DefaultHandler {
	
	//List to hold FoodTruck objects
	private List<FoodTruck> foodtrList = null;
	private FoodTruck foodtr = null;
	
//	//Getter method for employee list
//	public List<FoodTruck> getFoodTrList() {
//		return foodtrList;
//	}

	String name;
	String description;
	Double latitude;
	Double longitude;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
	throws SAXException {
		
		if (qName.equalsIgnoreCase("Placemark")) {
			// create new FoodTruck and put it in Map
			String id = attributes.getValue("id");
			//initialize Employee object and set id attribute
			foodtr.setId(id); 
			//intialize list
			if (foodtrList == null) 
				foodtrList = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("name")) {
			//set boolean values for fields, will be used in setting
			name = attributes.getValue("name");
			foodtr.setName(name);
		} else if (qName.equalsIgnoreCase("description")) {
			description = attributes.getValue("description");
			foodtr.setDescription(description);
		} else if (qName.equalsIgnoreCase("Point")) {
			String coord = attributes.getValue("coordinates");	
			String[] parts = coord.split(",");
			String part1 = parts[0];
			String part2 = parts[1];
			foodtr.setLongitude(Double.parseDouble(part1));
			foodtr.setLatitude(Double.parseDouble(part2));	
		}
	}
		
	
	@Override
	public void endElement(String uri, String localName, String qName)
	throws SAXException {
		if (qName.equalsIgnoreCase("Placemark")) {
			//add FoodTruck object to list
			foodtrList.add(foodtr);
		}
	}
	
}
	

