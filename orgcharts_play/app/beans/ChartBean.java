package beans;

import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnore;

import scala.reflect.internal.Trees.This;
import utilities.ClobConverter;

public class ChartBean {

	String uuid;
	String ownerID; //NY user shortname
	String chartName;
	@JsonIgnore
	Clob xml;
	Timestamp timeLastModified;
	int version;
	String editUserId;

	@JsonIgnore
	public final static String PERMISSION_SPECIFIED = "specified";
	@JsonIgnore
	public final static String[] PERMISSION_OPTIONS = { "public", PERMISSION_SPECIFIED, "private" };
	String permission = PERMISSION_OPTIONS[0];
	String permittedUser;

	/**
	 * Empty Constructor
	 */
	public ChartBean() {
		super();
	}

	/**
	 * Constructor for update chart, please fill rest field via setter.
	 * 
	 * @param uuid
	 */
	public ChartBean(String uuid) {
		super();
		this.setUuid(uuid);
		this.setTimeLastModifiedNow();
	}

	/**
	 * Constructor for new chart with String XML, please fill rest field via setter.
	 * 
	 * @param owner_id, String represented Integer of owner's user id.
	 * @param chart_name
	 */
	public ChartBean(String owner_id, String chart_name) {
		super();
		this.setUuidNew();
		this.ownerID = owner_id;
		this.chartName = chart_name;
		this.setXmlDefault();
		this.setTimeLastModifiedNow();
		this.editUserId = "";
	}

	/**
	 * For DBA use
	 * 
	 * @param uuid
	 * @param owner_id
	 * @param chart_name
	 * @param xml
	 * @param create_time
	 * @param version
	 * @param edit_user
	 */
	public ChartBean(String uuid, String owner_id, String chart_name, Clob xml,
			Timestamp create_time, Integer version, String edit_user) {
		super();
		this.uuid = uuid;
		this.ownerID = owner_id;
		this.chartName = chart_name;
		this.xml = xml;
		this.timeLastModified = create_time;
		this.version = version;
		this.editUserId = "";
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setUuidNew() {
		this.uuid = java.util.UUID.randomUUID().toString();
	}

	@JsonIgnore
	public Timestamp getUuidTime() {
		return new Timestamp(java.util.UUID.fromString(this.uuid).timestamp());
	}

	public String getOwnerID() {
		return null==ownerID?"":ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public String getChartName() {
		return null == chartName ? "" : chartName;
	}

	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	public void setXml(Clob xml) {
		this.xml = xml;
	}

	public void setXmlString(String xmlString) {
		this.xml = ClobConverter.stringToClob(xmlString);
	}

	public void setXmlDefault() {
		this.setXmlString("<li class=\"root unit\"><span class=\"label_node\" id=\"un\"></span><br><span class=\"label_node\" id=\"ud\"></span></li>");
	}

	public Clob getXml() {
		return xml;
	}

	@JsonIgnore
	public String getXmlString() {
		return ClobConverter.clobToString(xml);
	}

	public Timestamp getTimeLastModified() {
		return timeLastModified;
	}

	public void setTimeLastModified(Timestamp time_last_modified) {
		this.timeLastModified = time_last_modified;
	}

	public void setTimeLastModifiedNow() {
		Calendar calendar = Calendar.getInstance();
		this.timeLastModified = new Timestamp(calendar.getTime().getTime());
	}

	public int getVersion() {
		return version;
	}

	public void setVersionDefault() {
		this.version = 0;
	}

	public void setVersion(Integer version) {
		if(null==version){
			this.setVersionDefault();
		}else{
			this.version = version;
		}
	}

	public String getEditUserId() {
		return editUserId;
	}

	public void setEditUserId(String editUserId) {
		this.editUserId = editUserId;
	}

	public String getPermission() {
		return null == this.permission ? this.PERMISSION_OPTIONS[0] : this.permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPermittedUser() {
		return null==permittedUser?"":permittedUser;
	}

	public void setPermittedUser(String permittedUser) {
		this.permittedUser = permittedUser;
	}
	
	public String getPermissionDisplay(){
		if(null!=this.permission){
			//If this.permission === "specified" 
			if(this.permission.equals(this.PERMISSION_SPECIFIED)){
				return "("+this.permission.toUpperCase()+" USER: "+this.permittedUser+")";
			}
			return "("+this.permission.toUpperCase()+")";
		}
		return "";
	}

	public boolean isValid() {
		if(this.uuid == null || this.uuid.equals("")
				|| this.chartName == null || this.chartName.equals("")){
			return false;
		}else if (!Arrays.asList(this.PERMISSION_OPTIONS).contains(this.permission)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ChartBean [uuid=" + uuid + ", chartName=" + chartName
				+ ", permission=" + permission + ", PERMISSION_OPTIONS="
				+ Arrays.toString(PERMISSION_OPTIONS) + ", permittedUser="
				+ permittedUser + ", timeLastModified=" + timeLastModified
				+ ", version=" + version + ", editUserId=" + editUserId
				+ ", ownerID=" + ownerID + ", xml.length()=" + getXmlString().length() + "]";
	}

}
