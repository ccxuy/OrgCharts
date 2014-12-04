package controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.simple.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import beans.ChartBean;
import beans.ProfileBean;
import play.Logger;
import play.data.*;
import play.libs.Json;
import play.mvc.*;
import config.Setting;
import config.Setting.*;
import utilities.HibernateUtilities;

public class ChartCtrl extends Controller {

	public static Result index() {
		return redirect("./test");
	}

	@SuppressWarnings("unchecked")
	public static Result getAllChart() {
		// Change to use session later...
		Integer ownerId = null;
		try {
			ownerId = Integer.parseInt(request().getQueryString(
					Setting.ChartAlias.Chart_Owner));
		} catch (NumberFormatException e) {
		}

		try {
			HibernateUtilities.getFactory();
			List<ChartBean> cbList = HibernateUtilities
					.getAllChartByOwnerId((ownerId));
			JSONArray chartsJson = new JSONArray();
			for (ChartBean cb : cbList) {
				// TODO: change it to Gson or Jackson code to avoid hardcode
				// name.
				JSONObject cbj = new JSONObject();
				cbj.put("DT_RowId", cb.getUuid());
				cbj.put("uuid", cb.getUuid());
				cbj.put("chartName", cb.getChartName());
				cbj.put("ownerID", String.valueOf(cb.getOwnerID()));
				ProfileBean owner = HibernateUtilities.searchEmployeeById(cb
						.getOwnerID());
				cbj.put("OwnerName",
						null == owner ? "(DELETED USER)" : owner.getWholeName());
				cbj.put("version",
						cb.getVersion() == null ? "n/a" : String.valueOf(cb
								.getVersion()));
				if (null == cb.getTimeLastModified()) {
					cbj.put("timeLastModified", "n/a");
				} else {
					cbj.put("timeLastModified", cb.getTimeLastModified()
							.toString());
				}

				if (null == cb.getEditUser()) {
					cbj.put("editUser", " ");
					cbj.put("CUName", " ");
				} else {
					ProfileBean cu = HibernateUtilities.searchEmployeeById(cb
							.getEditUser());
					cbj.put("editUser", String.valueOf(cb.getEditUser()));
					cbj.put("CUName",
							null == cu ? "(DELETED USER)" : cu.getWholeName());
				}
				chartsJson.add(cbj);
			}
			JSONObject resultJson = new JSONObject();
			resultJson.put("data", chartsJson);
			return ok(resultJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError();
		}
	}

	public static Result getChart(String id) {
		// Read XML from storage
		String input = "";
		if (Setting.STORAGE == StorageSetting.HIBERNATE) {
			String chartId = request().getQueryString("chartid");
			if (null == chartId || chartId.equals("")) {
				chartId = Setting.DefaultData.ChartId_default;
			}
			try {
				System.out.println("Chart@getChart: chartid=" + chartId);
				HibernateUtilities.getFactory();
				ChartBean chartBean = HibernateUtilities
						.searchChartByUUID(chartId);
				input = chartBean.getXmlString();
			} catch (Exception e) {
				e.printStackTrace();
				return badRequest();
			}
		}
		// session("empchart", "<ul id='org' style='display:none'>" + input
		// + "</ul>");
		return ok(input);
	}

	public static Result checkChartName(String chartName) {
		try {
			System.out.println("Chart@checkChartName: chartName=" + chartName);
			HibernateUtilities.getFactory();
			// ChartBean chartBean = HibernateUtilities
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError();
		}
		return ok();
	}

	public static Result createChart() {
		DynamicForm form = Form.form().bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(form.get().toString());
		} else {
			String chart_name = form.get("new_chart_name");
			String chart_ownerid = form.get("new_chart_owner_id");
			try {
				ChartBean cb = new ChartBean(chart_ownerid, chart_name);
				cb.setVersionDefault();
				System.out.println("ChartCtrl@createChart:cb="+cb);
				HibernateUtilities.getFactory();
				int ret = HibernateUtilities.saveOrUpdateChart(cb);
				if (ret >= 1) {
					ObjectNode cbj = (ObjectNode) Json.toJson(cb);
					cbj.put("DT_RowId", cb.getUuid());
					ProfileBean owner = HibernateUtilities
							.searchEmployeeById(cb.getOwnerID());
					cbj.put("OwnerName", null == owner ? "(DELETED USER)"
							: owner.getWholeName());

					if (null == cb.getTimeLastModified()) {
						cbj.put("timeLastModified", "n/a");
					} else {
						cbj.put("timeLastModified", cb.getTimeLastModified()
								.toString());
					}

					if (null == cb.getEditUser()) {
						cbj.put("editUser", " ");
						cbj.put("CUName", " ");
					} else {
						ProfileBean cu = HibernateUtilities
								.searchEmployeeById(cb.getEditUser());
						cbj.put("CUName",
								null == cu ? "(DELETED USER)" : cu
										.getWholeName());
					}
					System.out.println("ChartCtrl@createChart:cbj.textValue()="+cbj.textValue());
					return ok(cbj);
				} else {
					return internalServerError(form.get().toString());
				}
			} catch (NumberFormatException e) {
				return badRequest("Owner id should be a number. form.get(\"new_chart_owner_id\")="
						+ chart_ownerid +"\nform.get().toString()=\n"+form.get().toString());
			} catch (Exception e) {
				e.printStackTrace();
				return internalServerError("ChartCtrl@createChart");
			}
		}
	}

	public static Result updateChart() {
		return null;
	}

	public static Result updateChartXML() {
		System.out.println("ChartCtrl@updateChartXML>>");
		DynamicForm form = Form.form().bindFromRequest();
		String chartId = form.get("chartid");
		String update_str = form.get("UptString");
		try {
			HibernateUtilities.getFactory();
			ChartBean chartBean = HibernateUtilities.searchChartByUUID(chartId);
			if (null != chartBean && null != update_str) {
				chartBean.setXmlString(update_str);
				HibernateUtilities.saveOrUpdateChart(chartBean);
				return ok();
			} else {
				return notFound(chartId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("ChartCtrl@updateChartXML", e);
			return internalServerError();
		}
	}

	public static Result deleteChart(String id) {
		return null;
	}

}