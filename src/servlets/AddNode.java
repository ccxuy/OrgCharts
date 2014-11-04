/*Author: Chinthaka Jayawardena*/
package servlets;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;


import utilities.HibernateUtilities;


/**
 * This function is actually for add_employee button... 
 *
 */
public class AddNode extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddNode() {
        super();
        // TODO Auto-generated constructor stub
    }    
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("text/html");
		
		
		
		//String location = request.getParameter("Location");
		/*
		File file = new File("C:\\Users\\kxl15\\Documents\\OrgCharts\\WebContent\\images\\close-btn.png");
		byte[] imageData = new byte[(int) file.length()];
		String fname = "test.jpeg";
		*/
		//new info;
		try {
			String first_name = request.getParameter("firstName");
			if(null==first_name)first_name="";
			String last_name = request.getParameter("lastName");
			if(null==last_name)last_name="";
			System.out.println("firstName: "+first_name);
			System.out.println("lastName: "+last_name);
			//new info
			String email = request.getParameter("Email");
			if(null==email)email="";
			System.out.println("email: "+email);
			Blob image=(Blob) request.getAttribute("Image");
			System.out.println("image: "+ (image==null?"null":image.length()) );
			//FileInputStream fileInputStream = new FileInputStream(file);
		    //fileInputStream.read(imageData);
		    //fileInputStream.close();
		    //Blob blob = new SerialBlob(imageData);
			HibernateUtilities.getFactory();
			HibernateUtilities.addRecord(first_name, last_name, email, image);
			String empId=HibernateUtilities.getId();
			response.getWriter().write(empId);
			//request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch(Exception e){
			e.printStackTrace();
		}	
	}
}
