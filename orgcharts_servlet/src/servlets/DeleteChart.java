/*Author: Chinthaka Jayawardena*/
package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;




public class DeleteChart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteChart() {
        super();
        // TODO Auto-generated constructor stub
    }    
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int lines = 0;
		try {			
			URL myURL = new URL("http://localhost:8180/OrgCharts/");
		    URLConnection con = myURL.openConnection();
		    con.connect();
		    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		    String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	        	lines++;
	        	if (lines == 10){
	            System.out.println(inputLine);
	        	}
	        }
	        in.close();		        
		} 
		
		catch (MalformedURLException e) { 
		    System.out.println("MalformedURLException: " + e.getMessage());
		} 
		catch (IOException e) {   
			System.out.println("IOException: " + e.getMessage());
		}
	}
	
}
