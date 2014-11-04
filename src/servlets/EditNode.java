/*Author: Chinthaka Jayawardena*/
package servlets;


import java.awt.print.Printable;
import java.io.IOException;
import java.sql.Blob;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.ProfileBean;

import utilities.HibernateUtilities;


public class EditNode extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditNode() {
        super();
        // TODO Auto-generated constructor stub
    }    
   
	/**
	 * TODO: un-handled exceptions for employeeProfileBean id parsing exception and hibernate exception.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("text/html");
		
		System.out.println("EDIT NODE>>");
		try {
			HibernateUtilities.getFactory();
			String emp_id = request.getParameter("emp_Id");
			ProfileBean employeeProfileBean = new ProfileBean(emp_id);
			if(null!=employeeProfileBean){
				String first_name = request.getParameter("firstName");
				String last_name = request.getParameter("lastName");
				String email = request.getParameter("Email");
				Blob image=(Blob) request.getAttribute("Image");
				employeeProfileBean.setfirstName(first_name);
				employeeProfileBean.setlastName(last_name);
				employeeProfileBean.setEmail(email);
				employeeProfileBean.setImg(image);
				System.out.println(employeeProfileBean);

				HibernateUtilities.editNode(employeeProfileBean);
			}
			//This is the old way..
			//HibernateUtilities.editNode(first_name, last_name, email, null, Integer.parseInt(emp_id));
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch(Exception e){
			e.printStackTrace();
		}	
	}
}
