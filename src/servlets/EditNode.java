/*Author: Chinthaka Jayawardena*/
package servlets;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("text/html");
		
		String first_name = request.getParameter("firstName");
		String last_name = request.getParameter("lastName");
		String email = request.getParameter("Email");
		String emp_id = request.getParameter("emp_Id");
		System.out.println("emp_id:" +emp_id);
		System.out.println("first_name: "+first_name);
		System.out.println("last_name: "+last_name);
		try {
			HibernateUtilities.getFactory();
			HibernateUtilities.editNode(first_name, last_name, email, null, Integer.parseInt(emp_id));
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
