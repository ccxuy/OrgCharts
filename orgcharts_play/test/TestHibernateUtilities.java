

import static org.junit.Assert.*;

import java.util.List;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;



import utilities.*;

public class TestHibernateUtilities extends TestCase{
	SessionFactory sessionFactory = null;
	
	@Before
	protected void setUp() throws Exception {
		sessionFactory = HibernateUtilities.getFactory();
	}

	@Test
	public void testEditNode() {
		List resList =  HibernateUtilities.getEditedData(String.valueOf(92));
		String resp =resList.get(0).toString()+",";
		System.out.println(resp);
		assertNotNull(HibernateUtilities.getEditedData(String.valueOf(92)));
	}

}
