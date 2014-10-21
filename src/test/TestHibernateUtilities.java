package test;

import static org.junit.Assert.*;

import org.hibernate.SessionFactory;
import org.junit.Test;

import utilities.HibernateUtilities;

public class TestHibernateUtilities {
	
	@Test
	public void testTest() {
		assertEquals("test", 2, 1+1);
	}

	@Test
	public void testGetFactory() throws Exception {
		SessionFactory sessionFactory = HibernateUtilities.getFactory();
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
		fail("Not yet implemented");
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
