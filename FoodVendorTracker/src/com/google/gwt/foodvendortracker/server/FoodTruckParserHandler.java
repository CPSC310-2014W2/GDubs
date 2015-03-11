package com.google.gwt.foodvendortracker.server;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FoodTruckParserHandler extends DefaultHandler {

	//List to hold FoodTruck objects
	StringBuffer accumulator;
	private List<FoodTruck> foodtrList;
	private FoodTruck foodtr;

	//	//Getter method for employee list
	//	public List<FoodTruck> getFoodTrList() {
	//		return foodtrList;
	//	}
	String id;
	String name;
	String description;
	Double latitude;
	Double longitude;

	@Override 
	public void startDocument() {
		System.out.println("Start Document!");
		accumulator = new StringBuffer();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {

		if (qName.equalsIgnoreCase("Placemark")) {
			// create new FoodTruck and put it in Map
			String id = attributes.getValue("id");
			//initialize Employee object and set id attribute
			foodtr.setId(id); 
			System.out.print("Id = " + attributes.getValue("id"));
//			foodtr = new FoodTruck(attributes.getValue("Id"));
			//intialize list
			if (foodtrList == null) 
				foodtrList = new ArrayList<>();
		}
		accumulator.setLength(0);
	}

	public void characters(char[] temp, int start, int length) {
		// Remember the value parsed
		accumulator.append(temp, start, length);
		System.out.println("Value of accumulator: " + accumulator.toString());
	}


	public void endElement(String uri, String localName, String qName) {
		// Print out that we have seen the end of an element
		System.out.println("EndElement: " + qName + " value: " + accumulator.toString().trim());	

		if (qName.equalsIgnoreCase("name")) {
			//set boolean values for fields, will be used in setting
			foodtr.setName(accumulator.toString());
		} 
		if (qName.equalsIgnoreCase("description")) {
			foodtr.setDescription(accumulator.toString());
		} 
//		if (qName.equalsIgnoreCase("Point")) {
//			String coord = attributes.getValue("coordinates");	
//			String[] parts = coord.split(",");
//			String part1 = parts[0];
//			String part2 = parts[1];
//			foodtr.setLongitude(Double.parseDouble(part1));
//			foodtr.setLatitude(Double.parseDouble(part2));	
//		}
	}


	//	@Override
	//	public void endElement(String uri, String localName, String qName)
	//			throws SAXException {
	//		if (qName.equalsIgnoreCase("Placemark")) {
	//			//add FoodTruck object to list
	//			foodtrList.add(foodtr);
	//		}
	//	}
	public void endDocument() {
		// Just let the user know as something to do
		System.out.println("End Document!");
	}


}


