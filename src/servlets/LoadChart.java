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

import utilities.HibernateUtilities;

import beans.ChartBean;

import config.Setting;
import config.Setting.StorageSetting;

public class LoadChart extends HttpServlet {

	public LoadChart() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");

		//Read XML from storage
		String input = "";
		if(Setting.STORAGE == StorageSetting.LOCAL_XML){
			String fileName = request.getParameter("fileName");
			input = readFromLocalXML(fileName);
		}else{
			String chartUuid = (String)request.getParameter("chartUuid");
			if(null==chartUuid){
				chartUuid = Setting.DefaultData.CHARTUUID_STRING;
			}
			try {
				HibernateUtilities.getFactory();
				ChartBean chartBean = HibernateUtilities.searchChartByUUID(chartUuid);
				input = chartBean.getXmlString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//END Read XML from storage
		
		// String inputToResponse = input.substring(input.indexOf(">") + 1,
		// input.lastIndexOf("<"));
		HttpSession sess = request.getSession(true);
		String inputWithNoneDisplay = ("<ul id='org' style='display:none'>"
				+ input + "</ul>");
		sess.setAttribute("empchart", inputWithNoneDisplay);
		response.getWriter().write(input);
		// response.getWriter().flush();
		// request.getRequestDispatcher("/index.jsp").forward(request,
		// response);
	}

	private String readFromLocalXML(String fileName) {
		if (null == fileName)
			fileName = "";
		String fileNameToRead;
		String input = "";
		try {
			if (!fileName.contains("\\") && !fileName.equals("")) {
				fileNameToRead = "C:\\Users\\yxx03\\workspace\\OrgCharts\\WebContent\\output_files\\"
						+ fileName;
			} else if (fileName.equals("")) {
				File directory = new File(
						"C:\\Users\\yxx03\\workspace\\OrgCharts\\WebContent\\output_files");
				File[] files = directory.listFiles();
				Arrays.sort(files, new Comparator<File>() {
					public int compare(File file1, File file2) {
						return Long.valueOf(file1.lastModified()).compareTo(
								file2.lastModified());
					}
				});
				// System.out.println(files.length);
				// for(int x=0;x<files.length;x++){
				// System.out.println(files[x].getName());
				// }
				fileNameToRead = "C:\\Users\\yxx03\\workspace\\OrgCharts\\WebContent\\output_files\\"
						+ files[files.length - 1].getName();
			} else {
				fileNameToRead = fileName;
			}
			System.out.println(fileNameToRead);
			File toRead = new File(fileNameToRead);
			Scanner scan = new Scanner(toRead);

			while (scan.hasNext()) {
				input += scan.next();
				input += " ";
			}
			scan.close();
			input = input.trim();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}
}
