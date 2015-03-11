import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

import com.google.gwt.foodvendortracker.server.FoodTruck;
import com.google.gwt.foodvendortracker.server.FoodTruckParser;
import com.google.gwt.foodvendortracker.server.FoodTruckParserHandler;



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

}
