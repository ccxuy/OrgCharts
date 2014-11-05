/*Author: Chinthaka Jayawardena*/
package servlets;


import java.awt.print.Printable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("EditNode@doPost>>");
		try {
			HibernateUtilities.getFactory();
			
			System.out.println(">>Names:attrNames");
			Enumeration<String> attrNames = request.getParameterNames();
			while(attrNames.hasMoreElements()){
				System.out.println(attrNames.nextElement());
			}
			System.out.println(">>Names:partNames");
			Collection<Part> parts = request.getParts();
			for(Part p:parts){
				System.out.println(p.getName());
			}
			System.out.println("<<Names");
			
			String emp_id = request.getParameter("emp_Id");
			if(null==emp_id){
				System.err.println("null==emp_Id, unable to edit employee!");
				return;
			}
			ProfileBean employeeProfileBean = new ProfileBean(emp_id);
			if(null!=employeeProfileBean){
				String first_name = request.getParameter("edit_first_name");
				if(null==first_name)first_name="";
				String last_name = request.getParameter("edit_last_name");
				if(null==last_name)last_name="";
				String email = request.getParameter("email");
				if(null==email)email="";
				Part imgPart = request.getPart("image");
				InputStream imgInputStream = null;
				if(null!=imgPart){
					imgInputStream = imgPart.getInputStream();
				}
				
				employeeProfileBean.setfirstName(first_name);
				employeeProfileBean.setlastName(last_name);
				employeeProfileBean.setEmail(email);
				employeeProfileBean.setImg(imgInputStream);
				System.out.println(employeeProfileBean);

				HibernateUtilities.editNode(employeeProfileBean);
			}
		}catch (NumberFormatException e){
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	
}
