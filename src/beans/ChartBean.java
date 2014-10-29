package beans;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

import utilities.ClobConverter;

public class ChartBean implements Serializable{

	String uuid;
	int owner_id;
	String chart_name;
	Clob xml;
	Timestamp create_time;
	int version;
	Integer edit_user;

	public ChartBean() {
		super();
	}

	/**
	 * For new chart with String XML
	 * @param owner_id
	 * @param chart_name
	 * @param xml
	 * @throws SQLException 
	 * @throws SerialException 
	 */
	public ChartBean(int owner_id, String chart_name, String xml) throws SerialException, SQLException {
		super();
		this.setUuidNew();
		this.owner_id = owner_id;
		this.chart_name = chart_name;
		setXmlString(xml);
		this.setCreate_timeNow();
		this.edit_user = null;
	}

	/**
	 * For DBA use
	 * @param uuid
	 * @param owner_id
	 * @param chart_name
	 * @param xml
	 * @param create_time
	 * @param version
	 * @param edit_user
	 * @throws SQLException 
	 * @throws SerialException 
	 */
	public ChartBean(String uuid, int owner_id, String chart_name, Clob xml,
			Timestamp create_time, int version, Integer edit_user) throws SerialException, SQLException {
		super();
		this.uuid = uuid;
		this.owner_id = owner_id;
		this.chart_name = chart_name;
		this.xml = xml;
		this.create_time = create_time;
		this.version = version;
		this.edit_user = edit_user;
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

	public int getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}

	public String getChart_name() {
		return chart_name;
	}

	public void setChart_name(String chart_name) {
		this.chart_name = chart_name;
	}

	public Clob getXml() {
		return xml;
	}

	public void setXml(Clob xml) {
		this.xml = xml;
	}
	
	public void setXmlString(String xmlString){
		this.xml = ClobConverter.stringToClob(xmlString);
	}
	
	public String getXmlString(){
		return ClobConverter.clobToString(xml);
	}

	public Timestamp getCreate_time() {
		return create_time;
	}
	
	public void setCreate_timeNow() {
		Calendar calendar = Calendar.getInstance();
		this.create_time = new Timestamp(calendar.getTime().getTime());
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public int getVersion() {
		return version;
	}
	
	public void setVersionDefault() {
		this.version = 1;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Integer getEdit_user() {
		return edit_user;
	}

	public void setEdit_user(Integer edit_user) {
		this.edit_user = edit_user;
	}

	@Override
	public String toString() {
		return "ChartBean [uuid=" + uuid + ", owner_id=" + owner_id
				+ ", chart_name=" + chart_name + ", xml=" + getXmlString()
				+ ", create_time=" + create_time + ", version=" + version
				+ ", edit_user=" + edit_user + "]";
	}

}
