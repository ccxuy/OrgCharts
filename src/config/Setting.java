package config;

import com.sun.org.apache.bcel.internal.generic.NEW;

import config.Setting.StorageSetting;

public class Setting {
	public static final boolean USE_TEST_DATA = true;
	public static final StorageSetting STORAGE = StorageSetting.HIBERNATE;
	
	public enum StorageSetting{
		LOCAL_XML, HIBERNATE
	}
	
	public static class DefaultData{
		public static final String CHARTUUID_STR = "test";
	}
	
	public static class ChartAlias{
		public static final String CHARTBEAN_STR = "ChartBean";
		public static final String CHARTBEAN_ID_STR = "chartid";
	}
	
	public static class Url{
		public static String UpdateChart = "/UpdateChart.do";
	}
//	public static DefaultData getTestData(){
//		return null;
//	}
}
