package servlets;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import utilities.HibernateUtilities;
import java.util.*;

public class getEditedData extends HttpServlet {
	Logger log = Logger.getLogger(getEditedData.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getEditedData() {
        super();
        // TODO Auto-generated constructor stub
    }    
  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String empId = request.getParameter("empId");
//		System.out.println("employee Id: "+empId);

		try {
			log.debug("getEditedData:doGet");
			HibernateUtilities.getFactory();
			log.debug("null?" + (null==HibernateUtilities.getFactory()) );
			List mylist = HibernateUtilities.getEditedData(empId);
			////////////////////
			String temp="";
			String tmp0 = "";
			if(null!=mylist.get(0)) tmp0 = mylist.get(0).toString();
			String resp = tmp0+",";
			for(int x=1; x<mylist.size(); x++){
					temp = mylist.get(x).toString();
					resp+=temp.substring(temp.indexOf('[')+1, temp.indexOf(']'))+",";
			}
//			System.out.println(mylist);
//			System.out.println(resp);
			//request.getRequestDispatcher("/index.jsp").forward(request, response);
			//new info
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







