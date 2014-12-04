package beans;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.hibernate.internal.util.io.StreamCopier;

import com.fasterxml.jackson.annotation.JsonIgnore;

import utilities.ClobConverter;

@Entity
public class ProfileBean {
	@Id
	@GeneratedValue
	int id;
	String firstName;
	String lastName;
	String email;
	String employeeTitle;
	String location;
	String phone;
	String fax;
	@JsonIgnore
	Clob extra;
	@JsonIgnore
	Blob img;

	public ProfileBean() {
	}

	/**
	 * Constructor for update ProfileBean, please fill rest field via setter.
	 * 
	 * @param id
	 */
	public ProfileBean(int id) {
		super();
		this.id = id;
	}

	/**
	 * Constructor for update ProfileBean, please fill rest field via setter.
	 * This one takes string id.
	 * 
	 * @param id
	 */
	public ProfileBean(String id) throws NumberFormatException {
		super();
		this.id = Integer.parseInt(id);
	}

	/**
	 * Constructor for add new employee ProfileBean.
	 * 
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param img
	 */
	public ProfileBean(String firstName, String lastName, String email, Blob img) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.img = img;

	}

	/**
	 * Constructor for add new employee ProfileBean.
	 * 
	 * @param first_name
	 * @param last_name
	 * @param email
	 * @param imgInputStream
	 */
	public ProfileBean(String firstName, String lastName, String email,
			InputStream imgInputStream) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.setImg(imgInputStream);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = (null==email?"":email);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = (null==firstName?"":firstName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = (null==lastName?"":lastName);
	}

	public Blob getImg() {
		return img;
	}

	public void setImg(Blob img) {
		this.img = img;
	}

	public String getEmployeeTitle() {
		return employeeTitle;
	}

	public void setEmployeeTitle(String employeeTitle) {
		this.employeeTitle = employeeTitle;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = (null==location?"":location);
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = (null==phone?"":phone);
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = (null==fax?"":fax);
	}

	public Clob getExtra() {
		return extra;
	}

	public String getExtraString() {
		return ClobConverter.clobToString(this.extra);
	}

	public void setExtra(Clob extra) {
		this.extra = extra;
	}

	public void setExtraString(String extraString) {
		this.extra = ClobConverter.stringToClob(extraString);
	}

	@Override
	public String toString() {

		long imgLength = -1;
		try {
			if (null == img) {
				imgLength = 0;
			} else {
				imgLength = img.length();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "ProfileBean [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email
				+ ", employeeTitle=" + employeeTitle + ", location=" + location
				+ ", phone=" + phone + ", fax=" + fax + ", extra=" + extra
				+ ", img.length()=" + imgLength + "]";
	}

	public void setImg(File imgFile) {
		if (null == imgFile)
			return;
		try {
			this.setImg(new FileInputStream(imgFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void setImg(InputStream imgInputStream) {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			StreamCopier.copy(imgInputStream, bytes);/* close stream after copy */
			Blob blob = new SerialBlob(bytes.toByteArray());
			this.setImg(blob);
		} catch (SerialException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean hasNoImage() {
		try {
			if (null == this.img || this.img.length() <= 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getWholeName() {
		return this.getFirstName() + " " + this.getLastName();
	}

	public boolean isValid() {
		return this.firstName == null || this.firstName.equals("")
				|| this.lastName == null || this.lastName.equals("") ? false
				: true;
	}

}