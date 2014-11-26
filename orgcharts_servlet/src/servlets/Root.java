/*Author: Chinthaka Jayawardena*/
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.ProfileBean;

import utilities.HibernateUtilities;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

public class Root extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Root() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String rootName = "";
		try {

			HibernateUtilities.getFactory();
			List records = HibernateUtilities.searchName(1);
			for (Iterator iterator = records.iterator(); iterator.hasNext();) {
				ProfileBean data = (ProfileBean) iterator.next();
				rootName = data.getfirstName();
			}
			HttpSession sess = request.getSession(true);
			// sess.setAttribute("empchart",
			// "<ul id=\"org\" style=\"display:none\"><li><img src=\"img/close-btn.png\">"+rootName+"</li></ul>");
			// request.getRequestDispatcher("/index.jsp").forward(request,
			// response);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
