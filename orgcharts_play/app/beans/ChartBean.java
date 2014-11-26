package beans;

import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Calendar;

import utilities.ClobConverter;

public class ChartBean {

	String uuid;
	int ownerID;
	String chartName;
	Clob xml;
	Timestamp timeLastModified;
	Integer version;
	Integer editUser;

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
	 * Constructor for new chart with String XML
	 * 
	 * @param owner_id
	 * @param chart_name
	 * @param xml
	 */
	public ChartBean(int owner_id, String chart_name, String xml) {
		super();
		this.setUuidNew();
		this.ownerID = owner_id;
		this.chartName = chart_name;
		this.setXmlString(xml);
		this.setTimeLastModifiedNow();
		this.editUser = null;
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
	public ChartBean(String uuid, int owner_id, String chart_name, Clob xml,
			Timestamp create_time, Integer version, Integer edit_user) {
		super();
		this.uuid = uuid;
		this.ownerID = owner_id;
		this.chartName = chart_name;
		this.xml = xml;
		this.timeLastModified = create_time;
		this.version = version;
		this.editUser = edit_user;
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

	public Timestamp getUuidTime() {
		return new Timestamp(java.util.UUID.fromString(this.uuid).timestamp());
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
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
		this.setXmlString("<li class=\"root\"><span class=\"label_node\"></span><br><span class=\"label_node\"></span></li>");
	}

	public Clob getXml() {
		return xml;
	}

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

	public Integer getVersion() {
		return version;
	}

	public void setVersionDefault() {
		this.version = 1;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getEditUser() {
		return editUser;
	}

	public void setEditUser(Integer editUser) {
		this.editUser = editUser;
	}

	@Override
	public String toString() {
		return "ChartBean [uuid=" + uuid + ", ownerID=" + ownerID
				+ ", chartName=" + chartName + ", xml=" + getXmlString()
				+ ", timeLastModified=" + timeLastModified + ", version="
				+ version + ", editUser=" + editUser + "]";
	}

}
