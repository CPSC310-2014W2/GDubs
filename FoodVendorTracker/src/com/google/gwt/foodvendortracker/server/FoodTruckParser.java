package com.google.gwt.foodvendortracker.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

public class FoodTruckParser {

	private String urlString = "http://m.uploadedit.com/ba3a/1426033445441.txt";
	public FoodTruckParser() {

	}

	public List<FoodTruck> parse(String url) {
		List<FoodTruck> foodTruckList = new ArrayList<FoodTruck>();
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try{
			SAXParser saxParser = saxParserFactory.newSAXParser();
			FoodTruckParserHandler handler = new FoodTruckParserHandler();
			URL sourceURL = new URL(urlString);
			saxParser.parse(new InputSource(sourceURL.openStream()), handler);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return foodTruckList;
	}


}

