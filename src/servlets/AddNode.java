/*Author: Chinthaka Jayawardena*/
package servlets;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;


import utilities.HibernateUtilities;


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
		
		
		String first_name = request.getParameter("firstName");
		String last_name = request.getParameter("lastName");
		//new info
		//Blob image=request.getParameter("Image");
		String email = request.getParameter("Email");
		
		//String location = request.getParameter("Location");
		/*
		File file = new File("C:\\Users\\kxl15\\Documents\\OrgCharts\\WebContent\\images\\close-btn.png");
		byte[] imageData = new byte[(int) file.length()];
		String fname = "test.jpeg";
		*/
		System.out.println("emp_name: "+first_name);
		System.out.println("node_name: "+last_name);
		//new info;
		//System.out.println("image: "+image);
		System.out.println("email: "+email);
		try {
			//FileInputStream fileInputStream = new FileInputStream(file);
		    //fileInputStream.read(imageData);
		    //fileInputStream.close();
		    //Blob blob = new SerialBlob(imageData);
			HibernateUtilities.getFactory();
			HibernateUtilities.addRecord(first_name, last_name, email, null);
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
