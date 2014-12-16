package config;

import java.util.Map;

import config.Setting.StorageSetting;

public class Setting {
	public static final boolean USE_TEST_DATA = true;
	public static final StorageSetting STORAGE = StorageSetting.HIBERNATE;
	
	public enum StorageSetting{
		LOCAL_XML, HIBERNATE
	}
	
	public static class ServerSetting{
		public static int queryLimit = 1000;
	}
	
	public static class DefaultData{
		public static String ChartId_default = "test";
	}
	
	//TODO: change to annotation or rejection method.
	public static class ChartAlias{
		public static String ChartBean_STR = "ChartBean";
		public static String Chart_Id = "chartid";
		public static String Chart_Owner = "chartowner";
		public static String ChartField_OwnerId = "ownerID";
		public static String ChartField_Name = "chartName";
	}
	
	public static class EmployeeAlias{
//		Map<String, String> addEmpToEntity
	}
	
	public static class Url{
		public static String UpdateChart = "/UpdateChart.do";
	}
//	public static DefaultData getTestData(){
//		return null;
//	}
}
