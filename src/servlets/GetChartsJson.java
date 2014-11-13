package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import beans.ChartBean;
import beans.ProfileBean;

import utilities.HibernateUtilities;

import config.Setting;

/**
 * Servlet implementation class GetChartsJson
 */
public class GetChartsJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetChartsJson() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		Integer ownerId = null;
		try {
			ownerId = Integer.parseInt(request.getParameter(Setting.ChartAlias.Chart_Owner));
		} catch (NumberFormatException e) {
			// If no ownerId specified, just return all.
		}
		
		try {
			HibernateUtilities.getFactory();
			List<ChartBean> cbList = HibernateUtilities.getAllChartByOwnerId((ownerId));
			JSONArray chartsJson = new JSONArray();
			for(ChartBean cb:cbList){
				JSONObject cbj = new JSONObject();
				cbj.put("DT_RowId", cb.getUuid());
				cbj.put("ID", cb.getUuid());
				cbj.put("ChartName", cb.getChartName());
				cbj.put("OwnerID", String.valueOf(cb.getOwnerID()));
				ProfileBean owner = HibernateUtilities.searchEmployeeById(cb.getOwnerID());
				cbj.put("OwnerName", null==owner?"(DELETED USER)":owner.getWholeName());
				cbj.put("Version", cb.getVersion()==null?"n/a":String.valueOf(cb.getVersion()));
				if(null==cb.getTimeLastModified()){
					cbj.put("LMTime", "n/a");
				}else{
					cbj.put("LMTime", cb.getTimeLastModified().toString());
				}
				
				if(null==cb.getEditUser()){
					cbj.put("CUID", " ");
					cbj.put("CUName", " ");
				}else{
					ProfileBean cu = HibernateUtilities.searchEmployeeById(cb.getEditUser());
					cbj.put("CUID", String.valueOf(cb.getEditUser()));
					cbj.put("CUName", null==cu?"(DELETED USER)":cu.getWholeName());
				}
				chartsJson.add(cbj);
			}
			JSONObject resultJson = new JSONObject();
			resultJson.put("data", chartsJson);
			response.getOutputStream().print(resultJson.toJSONString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//	}

}
