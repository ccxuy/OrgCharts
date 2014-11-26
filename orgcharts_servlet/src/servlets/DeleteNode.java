package servlets;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.HibernateUtilities;


public class DeleteNode extends HttpServlet{
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteNode() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("text/html");
		String node_name = (String)request.getParameter("DeleteNode");
		//String chart = null;
		System.out.println("node_name: "+node_name);
		//int nodeId = Integer.parseInt(request.getParameter("id"));
		try {
			HibernateUtilities.getFactory();
			HibernateUtilities.deleteRecord(node_name);					
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch(Exception e){
				e.printStackTrace();
			}		
	}


}
