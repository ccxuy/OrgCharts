package test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import junit.framework.Assert;

import org.hibernate.SessionFactory;
import org.hibernate.dialect.DB2390Dialect;
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

//	@Test
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
		cb = new ChartBean(82, "test chart", "123<xml>");
		System.out.println(cb);
		int ret = HibernateUtilities.saveOrUpdateChart(cb);
		assertEquals(1, ret);
	}
	
	@Test
	public void testGetAllChartByOwnerId(){
		System.out.println("testGetAllChartByOwnerId");
		List<ChartBean> cbl = HibernateUtilities.getAllChartByOwnerId(0, 100);
		System.out.println(cbl.size());
		assertNotNull(cbl);
	}

}
