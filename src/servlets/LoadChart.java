package servlets;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Comparator;
import java.io.IOException;
import java.util.Date;
import java.text.*;
import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoadChart extends HttpServlet {

	public LoadChart() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName=request.getParameter("fileName");
		if(null==fileName)fileName="";
		String fileNameToRead;
		response.setContentType("text/html");
		try {
			if(!fileName.contains("\\")&&!fileName.equals("")){
				fileNameToRead="C:\\Users\\yxx03\\workspace\\OrgCharts\\WebContent\\output_files\\"+fileName;
			}
			else if(fileName.equals("")){
				File directory=new File("C:\\Users\\yxx03\\workspace\\OrgCharts\\WebContent\\output_files");
				File[] files=directory.listFiles();
				Arrays.sort(files, new Comparator<File>(){
					public int compare(File file1, File file2){
						return Long.valueOf(file1.lastModified()).compareTo(file2.lastModified());
					}
				});
//				System.out.println(files.length);
//				for(int x=0;x<files.length;x++){
//					System.out.println(files[x].getName());
//				}
				fileNameToRead="C:\\Users\\yxx03\\workspace\\OrgCharts\\WebContent\\output_files\\"+files[files.length-1].getName();
			}
			else{
				fileNameToRead=fileName;
			}
			System.out.println(fileNameToRead);
			File toRead=new File(fileNameToRead);
			Scanner scan=new Scanner(toRead);
			String input="";
			while(scan.hasNext()){
				input+=scan.next();
				input+=" ";
			}
			scan.close();
			input=input.trim();
			String inputToResponse=input.substring(input.indexOf(">")+1, input.lastIndexOf("<"));
			HttpSession sess = request.getSession(true);   
			sess.setAttribute("empchart", input);
			response.getWriter().write(inputToResponse);
			//response.getWriter().flush();
			//request.getRequestDispatcher("/index.jsp").forward(request, response);
			
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch(Exception e){
			e.printStackTrace();
		}	
	}
}
