package com.google.gwt.foodvendortracker.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class FoodTruckParser {

	public FoodTruckParser() {

	}

	public List<FoodTruck> parse(String url) {
		List<FoodTruck> foodTrucks = new ArrayList<FoodTruck>();
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try{
			SAXParser sp = spf.newSAXParser();
			FoodTruckParserHandler handler = new FoodTruckParserHandler();
			URL sourceURL = new URL(url);
			sp.parse(new InputSource(sourceURL.openStream()), handler);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return foodTrucks;
	}


}

