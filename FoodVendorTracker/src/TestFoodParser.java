import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.gwt.foodvendortracker.server.FoodTruck;
import com.google.gwt.foodvendortracker.server.FoodTruckParserHandler;



public class TestFoodParser {

	private List<FoodTruck> reg;
	private FoodTruck first;

	@Before
	public void setUp() {
		reg = new ArrayList<FoodTruck>();

		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new FoodTruckParserHandler());
			reader.parse("parse.xml");

			first = reg.get(0);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetId() {
		assertEquals("C1", ((FoodTruck) reg).getId());
	}

}
