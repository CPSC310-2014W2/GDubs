package com.google.gwt.foodvendortracker.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import com.google.gwt.foodvendortracker.shared.FoodTruck;

public class FoodTruckParser {

	private String urlString;
	private List<FoodTruck> foodTruckList = new ArrayList<>();
	
	public FoodTruckParser(String urlString) {
	this.urlString = urlString;
	}

	public List<FoodTruck> getFoodTruckList(){
		return foodTruckList;
	}
	
	public void parse() {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try{
			SAXParser saxParser = saxParserFactory.newSAXParser();
			FoodTruckParserHandler handler = new FoodTruckParserHandler();
			URL sourceURL = new URL(urlString);
			saxParser.parse(new InputSource(sourceURL.openStream()), handler);
			foodTruckList = handler.getFoodTruckList();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}


}