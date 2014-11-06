/*Author: Chinthaka Jayawardena*/
package servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.rowset.serial.SerialBlob;

import beans.ProfileBean;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * @deprecated use {@link #doPost()} instead.
	 */
	@Deprecated
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// response.setContentType("text/html");

		// String location = request.getParameter("Location");
		/*
		 * File file = new File(
		 * "C:\\Users\\kxl15\\Documents\\OrgCharts\\WebContent\\images\\close-btn.png"
		 * ); byte[] imageData = new byte[(int) file.length()]; String fname =
		 * "test.jpeg";
		 */
		// new info;
		try {
			String first_name = request.getParameter("firstName");
			if (null == first_name)
				first_name = "";
			String last_name = request.getParameter("lastName");
			if (null == last_name)
				last_name = "";
			System.out.println("firstName: " + first_name);
			System.out.println("lastName: " + last_name);
			// new info
			String email = request.getParameter("Email");
			if (null == email)
				email = "";
			System.out.println("email: " + email);
			Blob image = (Blob) request.getAttribute("Image");
			System.out.println("image: "
					+ (image == null ? "null" : image.length()));
			// FileInputStream fileInputStream = new FileInputStream(file);
			// fileInputStream.read(imageData);
			// fileInputStream.close();
			// Blob blob = new SerialBlob(imageData);
			HibernateUtilities.getFactory();
			HibernateUtilities.addRecord(first_name, last_name, email, image);
			String empId = HibernateUtilities.getId();
			response.getWriter().write(empId);
			// request.getRequestDispatcher("/index.jsp").forward(request,
			// response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("AddNode@doPost>>");
		try {
			HibernateUtilities.getFactory();

			String first_name = request.getParameter("new_first_name");
			if (null == first_name)
				first_name = " ";
			String last_name = request.getParameter("new_last_name");
			if (null == last_name)
				last_name = " ";
			String email = request.getParameter("new_node_email");
			if (null == email)
				email = "";
			Part imgPart = request.getPart("new_node_image");
			InputStream imgInputStream = null;
			if (null != imgPart) {
				imgInputStream = imgPart.getInputStream();
			}

			ProfileBean employeeProfileBean = new ProfileBean(first_name,
					last_name, email, imgInputStream);
			System.out.println(employeeProfileBean);

			int ret = HibernateUtilities.saveOrUpdateEmployee(employeeProfileBean);
			if(ret>0){
				System.out.println("employeeProfileBean.getId()"+employeeProfileBean.getId());
				System.out.println("ret"+ret);
				response.getWriter().println(employeeProfileBean.getId());
			}else{
				response.getWriter().write("-1");
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
