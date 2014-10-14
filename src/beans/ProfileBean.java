package beans;

import java.sql.Blob;

public class ProfileBean {
	
	int id;
    String firstName;
    String lastName;
    String email;
    Blob img;
    

	public ProfileBean() {}
    
    public ProfileBean(String firstName, String lastName, String email, Blob img) {
		
    	this.firstName = firstName;
		this.lastName = lastName;
		this.email=email;
		this.img = img;
		
		
	}

    //methods for added info

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}  
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getfirstName() {
		return firstName;
	}
	public void setfirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getlastName() {
		return lastName;
	}
	public void setlastName(String lastName){
		this.lastName = lastName;
	}
	public Blob getImg() {
		return img;
	}

	public void setImg(Blob img) {
		this.img = img;
	}
    
}
