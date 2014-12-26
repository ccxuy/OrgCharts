

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

import beans.ChartBean;

import utilities.HibernateUtilities;

public class TestChartFunction {
	SessionFactory sessionFactory = null;

	@Before
	public void setUp() throws Exception {
		System.out.println("Setup hibernate");
		sessionFactory = HibernateUtilities.getFactory();
		System.out.println("Finish Setup hibernate");
	}

	@Test
	public void testSearchChartByUUID() {
		System.out.println("testSearchChartByUUID");
		ChartBean cb = HibernateUtilities.searchChartByUUID("test");
		System.out.println(cb);
		assertNotNull(cb);
	}

//	@Test
	public void testSaveOrUpdateChart() {
		System.out.println("testSaveOrUpdateChart");
		ChartBean cb;
		cb = new ChartBean(82, "test chart");
		cb.setXmlDefault();
		System.out.println(cb);
		int ret = HibernateUtilities.saveOrUpdateChart(cb);
		assertEquals(1, ret);
	}
	
//	@Test
	public void testGetAllChartByOwnerId(){
		System.out.println("testGetAllChartByOwnerId");
		List<ChartBean> cbl = HibernateUtilities.getAllChartByOwnerId(0, 100);
		System.out.println(cbl.size());
		assertNotNull(cbl);
	}

}
