/*Author: Chinthaka Jayawardena*/
package servlets;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



public class UpdateChart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateChart() {
        super();
        // TODO Auto-generated constructor stub
    }    
   
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String update_strg = (String)request.getParameter("UptString");
		String upt_strg = ("<ul id='org' style='display:none'>"+update_strg+"</ul>");
		try {
			saveToLocalXML(upt_strg);
			HttpSession sess = request.getSession(true);   
			sess.setAttribute("empchart", upt_strg);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch(Exception e){
			e.printStackTrace();
		}	
	}

	private void saveToLocalXML(String upt_strg) throws IOException {
		String out_file = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss-aaa").format(new Date());
		//String OUTPUT_FILE_NAME = "C:\\orgchart\\output.txt";
		String OUTPUT_FILE_NAME = "C:\\Users\\yxx03\\Documents\\OrgCharts\\WebContent\\output_files\\"+out_file+".xml";
		
		FileWriter fileWriter = new FileWriter(OUTPUT_FILE_NAME);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(upt_strg);
		bufferedWriter.close();
	}
}
