

import static org.junit.Assert.*;

import java.util.List;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;
import org.junit.Test;


import utilities.*;

public class TestHibernateUtilities extends TestCase{
	SessionFactory sessionFactory = null;
	
	@Override
	protected void setUp() throws Exception {
		sessionFactory = HibernateUtilities.getFactory();
	}

	@Test
	public void testTest() {
		assertEquals("test", 2, 1+1);
	}

	@Test
	public void testGetFactory() throws Exception {
		assertNotNull(sessionFactory);
	}

	@Test
	public void testSearchResults() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddRecord() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetId() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditNode() {
		List resList =  HibernateUtilities.getEditedData(String.valueOf(92));
		String resp =resList.get(0).toString()+",";
		System.out.println(resp);
		assertNotNull(HibernateUtilities.getEditedData(String.valueOf(92)));
	}

	@Test
	public void testDeleteRecord() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearchId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEditedData() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearchName() {
		fail("Not yet implemented");
	}

}
