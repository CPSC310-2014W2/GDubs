import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.gwt.foodvendortracker.server.FoodTruck;
import com.google.gwt.foodvendortracker.server.FoodTruckParserHandler;



public class TestFoodParser {

	private List<FoodTruck> reg;
	private FoodTruck first;
	private String urlString = "http://m.uploadedit.com/ba3a/1426033445441.txt";

	@Before
	public void setUp() {
		reg = new ArrayList<FoodTruck>();
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try{
			SAXParser saxParser = saxParserFactory.newSAXParser();
			FoodTruckParserHandler handler = new FoodTruckParserHandler();
			URL sourceURL = new URL(urlString);
			saxParser.parse(new InputSource(sourceURL.openStream()), handler);
			reg = handler.getFoodTruckList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetId() {
		assertEquals("C1", reg.get(0).getId());
	}

}
