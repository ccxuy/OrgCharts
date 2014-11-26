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

import beans.ChartBean;

import utilities.HibernateUtilities;

import config.Setting;
import config.Setting.StorageSetting;



public class UpdateChart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateChart() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		System.out.println("UpdateChart@doPost");
		String update_str = (String)request.getParameter("UptString");
		HttpSession session = request.getSession();
		ChartBean chartBean = (ChartBean)session.getAttribute(Setting.ChartAlias.ChartBean_STR);
		
		String upt_str = saveXml(update_str, chartBean);
//		String upt_str = saveXmlWithOuterTag(update_str, chartBean);
		
		HttpSession sess = request.getSession(true);   
		sess.setAttribute("empchart", upt_str);
		request.getRequestDispatcher("/chart.jsp").forward(request, response);
			
	}
	
	private String saveXml(String update_str, ChartBean chartBean){
		ChartBean chartBeanChecked = defaultValueOfChart(chartBean, update_str);
		if(Setting.STORAGE == StorageSetting.LOCAL_XML){
			saveToLocalXML(update_str);
		}else{
			try {
				HibernateUtilities.getFactory();
				HibernateUtilities.saveOrUpdateChart(chartBeanChecked);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String upt_str = ("<ul id='org' style='display:none'>"+update_str+"</ul>");
		return upt_str;
	}

	/**
	 * This could be a obsolete function, trying to save XML without outer tag
	 * @param update_str
	 * @param chartBean
	 * @return
	 * @throws IOException
	 */
	private String saveXmlWithOuterTag(String update_str, ChartBean chartBean){
		String upt_str = ("<ul id='org' style='display:none'>"+update_str+"</ul>");
		ChartBean chartBeanChecked = defaultValueOfChart(chartBean, upt_str);
		if(Setting.STORAGE == StorageSetting.LOCAL_XML){
			saveToLocalXML(upt_str);
		}else{
			try {
				HibernateUtilities.getFactory();
				HibernateUtilities.saveOrUpdateChart(chartBeanChecked);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return upt_str;
	}

	/**
	 * If chartBean or it's uuid is null, return a generated default value for it, otherwise return original chartBean.
	 * @param chartBean
	 * @param xml
	 * @return default chartBean if chartBean null or it's uuid null
	 */
	private ChartBean defaultValueOfChart(ChartBean chartBean, String xml) {
		if(null==chartBean||null==chartBean.getUuid()){
			chartBean = new ChartBean(Setting.DefaultData.ChartId_default);
			chartBean.setXmlString(xml);
			System.out.println("Generate defaultValueOfChart"+chartBean);
		}
		return chartBean;
	}

	private void saveToLocalXML(String upt_strg){
		try {
			String out_file = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss-aaa").format(new Date());
			//String OUTPUT_FILE_NAME = "C:\\orgchart\\output.txt";
			String OUTPUT_FILE_NAME = "C:\\Users\\yxx03\\Documents\\OrgCharts\\WebContent\\output_files\\"+out_file+".xml";
			
			FileWriter fileWriter = new FileWriter(OUTPUT_FILE_NAME);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(upt_strg);
			bufferedWriter.close();
		}catch (IOException e) {
			e.printStackTrace();
		} 
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
