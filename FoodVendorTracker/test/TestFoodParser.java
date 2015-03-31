import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

import com.google.appengine.tools.util.Parser;
import com.google.gwt.foodvendortracker.server.FoodTruckParser;
import com.google.gwt.foodvendortracker.server.FoodTruckParserHandler;
import com.google.gwt.foodvendortracker.shared.FoodTruck;

public class TestFoodParser {

	private List<FoodTruck> reg;
	private FoodTruckParser parser = new FoodTruckParser();

	@Before
	public void setUp() {
		parser.parse();
		reg = parser.getFoodTruckList();
	}
	@Test
	public void testGetId() {
		assertEquals("C1", reg.get(0).getId());
	}
	
	@Test
	public void testFoodTruckSize()      
	{
		parser.parse();
		List<FoodTruck> list = parser.getFoodTruckList();
		assertEquals(111, list.size());
	}
	
	@Test
	public void testFoodTruckName()
	{
		parser.parse();
		List<FoodTruck> list = parser.getFoodTruckList();
		for(FoodTruck t: list)
		{
			assertNotNull(t.getName());
		}
	}

	@Test
	public void testFoodTruckDesciption()
	{
		parser.parse();
		List<FoodTruck> list = parser.getFoodTruckList();
		for(FoodTruck t: list)
		{
			assertNotNull(t.getDescription());
		}
	}
	
	@Test
	public void testFoodTruckLatitude()
	{
		parser.parse();
		List<FoodTruck> list = parser.getFoodTruckList();
		for(FoodTruck t: list)
		{
			assertNotNull(t.getLatitude());
		}
	}
	
	@Test
	public void testFoodTruckLongitude()
	{
		parser.parse();
		List<FoodTruck> list = parser.getFoodTruckList();
		for(FoodTruck t: list)
		{
			assertNotNull(t.getLongitude());
		}
	}
	
	@Test
	public void testFoodTruckID()
	{
		parser.parse();
		List<FoodTruck> list = parser.getFoodTruckList();
		for(FoodTruck t: list)
		{
			assertNotNull(t.getId());
		}
	}

}
