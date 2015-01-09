package controllers;

import java.util.List;

import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import org.json.simple.*;

import com.fasterxml.jackson.databind.node.ObjectNode;

import beans.ChartBean;
import beans.ProfileBean;
import play.Logger;
import play.data.*;
import play.libs.Json;
import play.mvc.*;
import config.Setting;
import config.Setting.*;
import security.OrgChartDeadboltHandler;
import security.OrgChartRoleType;
import security.OrgChartUser;
import utilities.HibernateUtilities;

public class ChartCtrl extends Controller {

	public static Result index() {
		return redirect("./test");
	}

	@SuppressWarnings("unchecked")
	public static Result getAllChart() {
		// Change to use session later...
		String ownerId = null;
		try {
			ownerId = request().getQueryString(Setting.ChartAlias.Chart_Owner);
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
				cbj.put("ownerID", cb.getOwnerID());
				cbj.put("permission", cb.getPermission());
				cbj.put("permittedUser", cb.getPermittedUser());
				cbj.put("permissionDisplay", cb.getPermissionDisplay());
				//TODO: change to get user from cookies db?
				ProfileBean owner = HibernateUtilities.searchEmployeeById(cb
						.getOwnerID());
				cbj.put("OwnerName",
						null == owner ? "(UNKNOWN USER)" : owner.getWholeName());
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
							null == cu ? "(DELETED EMPLOYEE)" : cu.getWholeName());
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

	@SubjectPresent
	public static Result getChart(String id) {
		OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserBySession(session());
		// Read XML from storage
		String input = "";
		if (Setting.STORAGE == StorageSetting.HIBERNATE) {
			String chartId = request().getQueryString("chartid");
			if (null == chartId || chartId.equals("")) {
				chartId = Setting.DefaultData.ChartId_default;
			}
			try {
				System.out.println("ChartCtrl@getChart: chartid=" + chartId);
				HibernateUtilities.getFactory();
				ChartBean chartBean = HibernateUtilities
						.searchChartByUUID(chartId);

				if(isUserReadChartAllowed(chartBean, ocu)){
					return ok(chartBean.getXmlString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return badRequest();
			}
		}
		// session("empchart", "<ul id='org' style='display:none'>" + input
		// + "</ul>");
		Logger.info("ChartCtrl@getChart");
		return forbidden(" Permission Denied : You don't have permission to access this chart! ");
	}

	private static boolean isUserReadChartAllowed(ChartBean chartBean, OrgChartUser ocu){
//		Logger.debug("ChartCtrl@isUserReadChartAllowed chartBean="+chartBean+", ocu="+ocu);
		//if owner, private would go through here
		if(chartBean.getOwnerID().equals(ocu.getIdentifier())){
//			Logger.debug("ChartCtrl@isUserReadChartAllowed >> is owner");
			return true;
		}
		// Check roles, admin can do anything
		for(Role r : ocu.getRoles()){
			if(r.getName().equals(OrgChartRoleType.ADMIN)){
//				Logger.debug("ChartCtrl@isUserReadChartAllowed >> is admin");
				return true;
			}
		}
		// Check specified permission
		if(chartBean.getPermission().equals(ChartBean.PERMISSION_SPECIFIED)){
			for(String sid : chartBean.getPermittedUser().split(";")){
				if(sid.equals(ocu.getIdentifier())){
//					Logger.debug("ChartCtrl@isUserReadChartAllowed >> is specified");
					return true;
				}
			}
			// Lowest permission required - public
		}else if(chartBean.getPermission().equals(ChartBean.PERMISSION_OPTIONS[0])){
//			Logger.debug("ChartCtrl@isUserReadChartAllowed >> is public");
			return true;
		}else{
			Logger.error("ChartCtrl@isUserReadChartAllowed unknown chart permission : "+chartBean.getPermission());
		}
		return false;
	}

	private static boolean isUserWriteChartAllowed(ChartBean chartBean, OrgChartUser ocu){
		// Check roles, admin can do everything.
		for(Role r : ocu.getRoles()){
			if(r.getName().equals(OrgChartRoleType.ADMIN)){
				return true;
			}
		}
		// User can write chart that has read access.
		for(Role r : ocu.getRoles()){
			if(r.getName().equals(OrgChartRoleType.ADMIN)){
				return isUserReadChartAllowed(chartBean, ocu);
			}
		}
		// Readonly
		return false;
	}

	public static Result checkChartName(String chartName) {
		try {
			System.out.println("ChartCtrl@checkChartName: chartName=" + chartName);
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
			// Replace to use current login user id
//			String chart_ownerid = form.get("new_chart_owner_id");
			OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserBySession(session());
			Logger.debug("ChartCtrl@createChart OrgChartUser:="+ocu);
			String chart_ownerid = ocu.getIdentifier();
			String chart_permission = form.get("new_chart_permission");
			String chart_permitteduser = form.get("new_chart_permitted_user");
			try {
				ChartBean cb = new ChartBean(chart_ownerid, chart_name);
				cb.setVersionDefault();
				cb.setPermission(chart_permission);
				cb.setPermittedUser(chart_permitteduser);
				System.out.println("ChartCtrl@createChart:cb=" + cb);
				HibernateUtilities.getFactory();
				int ret = HibernateUtilities.saveOrUpdateChart(cb);
				if (ret >= 1) {
					return ok(parseChartbeanToJsonObjectNode(cb));
				} else {
					return internalServerError(form.get().toString());
				}
//			} catch (NumberFormatException e) {
//				return badRequest("Owner id should be a number. form.get(\"new_chart_owner_id\")="
//						+ chart_ownerid
//						+ "\nform.get().toString()=\n"
//						+ form.get().toString());
			} catch (Exception e) {
				e.printStackTrace();
				return internalServerError("ChartCtrl@createChart");
			}
		}
	}

	/**
	 * This method does not have protection for concurrent edition.
	 * 
	 * @return
	 */
	public static Result updateChart() {
		DynamicForm form = Form.form().bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(form.get().toString());
		} else {
			String chart_id = form.get("chartid");
			String chart_name = form.get("edit_chart_name");
			String chart_ownerid = form.get("edit_chart_owner_id");
			String chart_permission = form.get("edit_chart_permission");
			String chart_permitteduser = form.get("edit_chart_permitted_user");
			try {
				ChartBean cb = HibernateUtilities.searchChartByUUID(chart_id);
				if (null == cb) {
					return badRequest("Chart not exist, try to refresh data.\nChartCtrl@updateChart\n"
							+ form.get().toString());
				}
				cb.setChartName(chart_name);
				cb.setOwnerID(chart_ownerid);
				cb.setPermission(chart_permission);
				cb.setPermittedUser(chart_permitteduser);
				cb.setTimeLastModifiedNow();
				if (cb.isValid()) {
					HibernateUtilities.saveOrUpdateChart(cb);
					return ok(parseChartbeanToJsonObjectNode(cb));
				} else {
					return badRequest("Invalid chart parameters\nChartCtrl@updateChart\n"
							+ form.get().toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error(
						"ChartCtrl@updateChartXML" + form.get().toString(), e);
				return internalServerError("ChartCtrl@updateChart\n"
						+ form.get().toString());
			}
		}
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
			Logger.error("ChartCtrl@updateChartXML" + form.get().toString(), e);
			return internalServerError("ChartCtrl@updateChartXML\n"
					+ form.get().toString());
		}
	}

	public static Result deleteChart(String id) {
		System.out.println("ChartCtrl@deleteChart>> id="+id);
		DynamicForm form = Form.form().bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(form.get().toString());
		} else {
			String chart_id = null != id ? id : form.get("chartid");
			try {
				int ret = HibernateUtilities.deleteChartByUUID(chart_id);
				switch (ret) {
				case -1:
					return badRequest("chart id not exist.\nChartCtrl@deleteChart\n");
				case 0:
					return internalServerError("Hibernate Error\nChartCtrl@deleteChart\n");
				case 1:
					return ok();

				default:
					return internalServerError("Unkown Error\nChartCtrl@deleteChart\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error("ChartCtrl@deleteChart" + form.get().toString(), e);
				return internalServerError("ChartCtrl@deleteChart\n"
						+ form.get().toString());
			}
		}
	}

	/**
	 * This method parse chart bean to datatable single row json object
	 * 
	 * @param cb
	 * @return
	 */
	private static ObjectNode parseChartbeanToJsonObjectNode(ChartBean cb) {
		ObjectNode cbj = (ObjectNode) Json.toJson(cb);
		cbj.put("DT_RowId", cb.getUuid());
//		ProfileBean owner = HibernateUtilities.searchEmployeeById(cb
//				.getOwnerID());
		//TODO: establish user database and provide query api here.
		OrgChartUser owner = Application.getLoginUser(session());
		cbj.put("OwnerName",
				null == owner ? "(UNKNOWN USER)" : owner.getUserInfo().getFullname());

		if (null == cb.getTimeLastModified()) {
			cbj.put("timeLastModified", "n/a");
		} else {
			cbj.put("timeLastModified", cb.getTimeLastModified().toString());
		}

		if (null == cb.getEditUser()) {
			cbj.put("editUser", " ");
			cbj.put("CUName", " ");
		} else {
			ProfileBean cu = HibernateUtilities.searchEmployeeById(cb
					.getEditUser());
			cbj.put("CUName", null == cu ? "(DELETED EMPLOYEE)" : cu.getWholeName());
		}
		return cbj;
	}

}
