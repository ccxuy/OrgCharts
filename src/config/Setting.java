package config;

import com.sun.org.apache.bcel.internal.generic.NEW;

import config.Setting.StorageSetting;

public class Setting {
	public static final boolean USE_TEST_DATA = true;
	public static final StorageSetting STORAGE = StorageSetting.HIBERNATE;
	
	public enum StorageSetting{
		LOCAL_XML, HIBERNATE
	}
	
	public class DefaultData{
		public static final String CHARTUUID_STRING = "test";
	}
//	public static DefaultData getTestData(){
//		return null;
//	}
}
