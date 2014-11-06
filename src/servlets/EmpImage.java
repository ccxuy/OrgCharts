package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.HibernateUtilities;
import beans.ProfileBean;

/**
 * Servlet implementation class EmpImage
 */
public class EmpImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmpImage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("image/jpg");
		ServletOutputStream out = response.getOutputStream();
		System.out.println("EmpImage>>");
		try {
			HibernateUtilities.getFactory();
			String emp_id = request.getParameter("empId");
			if(null!=emp_id){
				ProfileBean empBean = HibernateUtilities.searchEmployeeById(emp_id);
				System.out.println(empBean);
				
				if(empBean.hasNoImage())return;
				InputStream is = empBean.getImg().getBinaryStream();
				int length = (int) empBean.getImg().length();
				int bufferSize = 1024;
			    byte[] buffer = new byte[bufferSize];
			    while ((length = is.read(buffer)) != -1) {
//			        System.out.println("writing " + length + " bytes");
			        out.write(buffer, 0, length);
			    }
			    is.close();
			    out.flush();
			}
//			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}catch(SQLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}	
	}

}
