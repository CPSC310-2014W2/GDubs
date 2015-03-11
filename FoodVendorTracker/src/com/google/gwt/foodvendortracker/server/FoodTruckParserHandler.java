package com.google.gwt.foodvendortracker.server;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gwt.foodvendortracker.shared.FoodTruck;

public class FoodTruckParserHandler extends DefaultHandler {

	//List to hold FoodTruck objects
	StringBuffer accumulator;
	private List<FoodTruck> foodtrList = new ArrayList<>();

	String id;
	String name;
	String description;
	Double latitude;
	Double longitude;
	
	public List<FoodTruck> getFoodTruckList(){
		return foodtrList;
	}
	
	@Override 
	public void startDocument() {
		System.out.println("Start Document!");
		accumulator = new StringBuffer();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		if (qName.equalsIgnoreCase("placemark")) {
			id = attributes.getValue("id");
		}
		if (qName.equalsIgnoreCase("name")) {
			name = null;
		}
		if (qName.equalsIgnoreCase("description")) {
			description = null;
		}
		if (qName.equalsIgnoreCase("coordinates")) {
			latitude = null;
			longitude = null;
		}
		accumulator.setLength(0);
	}

	public void characters(char[] temp, int start, int length) {
		accumulator.append(temp, start, length);
	}


	public void endElement(String uri, String localName, String qName) {
		if (qName.equalsIgnoreCase("name")) {
			name = accumulator.toString();
		} 
		if (qName.equalsIgnoreCase("description")) {
			String unformattedDescription = accumulator.toString();
			String formattedDescription = unformattedDescription.replaceAll("<[^>]*>", "");
			description = formattedDescription;
		} 
		if (qName.equalsIgnoreCase("coordinates")) {
			String coord = accumulator.toString();
			String[] parts = coord.split(",");
			String part1 = parts[0];
			String part2 = parts[1];
			longitude = Double.parseDouble(part1);
			latitude = Double.parseDouble(part2);
		}
		if (qName.equalsIgnoreCase("placemark")) {
			FoodTruck ft = new FoodTruck(id, name, description, latitude, longitude);
			foodtrList.add(ft);
			System.out.println("id: " + ft.getId() + " name: " + ft.getName() + " description: " + ft.getDescription() + " Long: " + ft.getLongitude() + " Lat: " + ft.getLatitude());
		}
	}

	public void endDocument() {
		// Just let the user know as something to do
		System.out.println("End Document!");
		//System.out.println(foodtrList.get(0).getId());
		//System.out.println(foodtrList.get(1).getId());
	}


}


