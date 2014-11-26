package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.HibernateUtilities;

public class RetrieveEmpData extends HttpServlet {
	 /**
     * @see HttpServlet#HttpServlet()
     */
    public RetrieveEmpData() {
        super();
        // TODO Auto-generated constructor stub
    }    
  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try {
			HibernateUtilities.getFactory();
			List mylist = HibernateUtilities.searchId();
			String temp="";
			String resp ="";
			for(int x=0; x<mylist.size()-1; x++){
					temp = mylist.get(x).toString();
					resp+=temp;
					resp+=",";
			}
			resp+=mylist.get(mylist.size()-1);
			System.out.println(mylist);
			System.out.println(resp);
			response.setContentType("text/html");
			//response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resp);
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch(Exception e){
			e.printStackTrace();
		}	
	}
}
