/*Author: Chinthaka Jayawardena*/
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EmpChart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmpChart() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    StringBuffer chartstr= new StringBuffer();
    
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Object obj= null;
		response.setContentType("text/html");
		JSONParser parser = new JSONParser();
		try {
			Object fileObj = parser.parse(new FileReader("c:\\org.json"));
			JSONObject orgJson = (JSONObject) fileObj;
			JSONArray employees = (JSONArray) orgJson.get("BHNSM");
			Iterator<String> employeesIterator = employees.iterator();
				
			while(employeesIterator.hasNext()){         
			    Object empObj = employeesIterator.next();
			    JSONObject empJson = (JSONObject) empObj;
			    String head = empJson.get("Name").toString();
			    String title = empJson.get("Title").toString();
			    chartstr.append("<ul id='org' style='display:none'><li>" +head);
			    obj =  parser.parse(empJson.get("sub").toString());
			    iterater(obj);	
				}			
			chartstr.append("</li></ul>");
			HttpSession sess = request.getSession(true);   
			sess.setAttribute("empchart", chartstr.toString());
			//System.out.println("I am here");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (ParseException e) {
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}		
	}
	
	public void iterater(Object obj){
	   	Object obj2 = null;
		JSONParser parser = new JSONParser();
		JSONArray jsonEmpArray = (JSONArray)  obj;
	     	for(int i=0;i<jsonEmpArray.size();i++){	     				
				JSONObject emp = (JSONObject) jsonEmpArray.get(i);	
				if (i==0){ 
					chartstr.append("<ul>");
					}		
				chartstr.append("<li>" + emp.get("Name"));
				try{
					obj2 =  parser.parse(emp.get("sub").toString());
					iterater (obj2);
					if (i==jsonEmpArray.size()-1){
						chartstr.append("</ul>");
					}
				}
				catch (ParseException e) {
					e.printStackTrace();
				}	 
			}
	     	chartstr.append("</li>");	     	
		}
}
