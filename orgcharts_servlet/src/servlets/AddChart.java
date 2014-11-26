/*Author: Chinthaka Jayawardena*/
package servlets;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.HibernateUtilities;


public class AddChart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddChart() {
        super();
        // TODO Auto-generated constructor stub
    }    
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		String emp_name = request.getParameter("name");
		
		String add_string = "";
		String start ="";
		String end ="";
//		if(add_strg.equals("<li><img src=\"img/close-btn.png\">Linh Le</li>")){
//			add_string = ("<ul id=\"org\" style=\"display:none\"><li><img src=\"img/close-btn.png\">Linh Le<ul><li><img src=\"img/close-btn.png\">Todd M. Orner</li></ul></li></ul>");
//			System.out.println("New add_string: "+add_string);
//		}
//		else {
//			if (add_strg.contains("<ul>")){	
//				int i = add_strg.indexOf("<ul>");
//				System.out.println("i is: "+i);
//				add_string = "<ul id=\"org\" style=\"display:none\">"+add_strg.substring(0,i+4)+"<li><img src=\"img/close-btn.png\">Chinthaka Jayawardena</li>"+add_strg.substring(i+4,add_strg.length())+"</ul>";
//				System.out.println("New add_string2: "+add_string);
//			}
//		}
		try {
			HibernateUtilities.getFactory();
			//HibernateUtilities.addRecord(emp_name, "");
			//System.out.println("eid: "+eid);
			HttpSession sess = request.getSession(true);
			String add_strg = (String)sess.getAttribute("empchart");
			System.out.println("add_strg: "+add_strg);
			if (add_strg==null||add_strg.equals("")){
			//	sess.setAttribute("empchart", "<ul id=\"org\" style=\"display:none\"><li><a href =\"DeleteNode.do?id="+eid+"\"><img src=\"img/close-btn.png\"></a>"+emp_name+"<ul></ul></li></ul>");
			}
			else {
				//System.out.println("ul: "+add_strg);
				//if (add_strg.contains("<ul>")){
					int i = add_strg.indexOf("<ul>");
				//	add_string = "<ul id=\"org\" style=\"display:none\">"+add_strg.substring(0,i+4)+"<li><a href =\"DeleteNode.do?id="+eid+"\"><img src=\"img/close-btn.png\"></a>"+emp_name+"</li>"+add_strg.substring(i+4,add_strg.length())+"</ul>";
					//add_string = (start+ "<li>"+emp_name+"<img src=\"img/close-btn.png\"></li>"+end);
					System.out.println("New add_string: "+add_string);
					sess.setAttribute("empchart", add_string);
				//}
			}
			
			response.sendRedirect("index.jsp");
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch(Exception e){
			e.printStackTrace();
		}	
	}
}
