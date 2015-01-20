package controllers;

import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import beans.ChartBean;
import beans.ProfileBean;
import com.fasterxml.jackson.databind.node.ObjectNode;
import config.Setting;
import config.Setting.StorageSetting;
import model.MessageChartStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import play.Logger;
import play.api.mvc.Codec;
import play.api.mvc.Results;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import security.ChartLock;
import security.OrgChartDeadboltHandler;
import security.OrgChartRoleType;
import security.OrgChartUser;
import utilities.HibernateUtilities;

import java.util.*;

public class ChartCtrl extends Controller {
    public static HashMap<String, ChartLock> chartidToChartLockMap = new HashMap<String, ChartLock>();

    public static Result index() {
        return redirect("./test");
    }

    @SuppressWarnings("unchecked")
    @SubjectPresent
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
//				JSONObject cbj = new JSONObject();
//				cbj.put("DT_RowId", cb.getUuid());
//				cbj.put("uuid", cb.getUuid());
//				cbj.put("chartName", cb.getChartName());
//				cbj.put("ownerID", cb.getOwnerID());
//				cbj.put("permission", cb.getPermission());
//				cbj.put("permittedUser", cb.getPermittedUser());
//				cbj.put("permissionDisplay", cb.getPermissionDisplay());
//				//TODO: change to get user from cookies db?
//				ProfileBean owner = null;
//				if(null!=cb.getOwnerID())owner = HibernateUtilities.searchEmployeeById(cb
//						.getOwnerID());
//				cbj.put("OwnerName",
//						null == owner ? "(UNKNOWN USER)" : owner.getWholeName());
//				cbj.put("version",
//						cb.getVersion() == null ? "n/a" : String.valueOf(cb
//								.getVersion()));
//				if (null == cb.getTimeLastModified()) {
//					cbj.put("timeLastModified", "n/a");
//				} else {
//					cbj.put("timeLastModified", cb.getTimeLastModified()
//							.toString());
//				}
//
//				if (null == cb.getEditUserId()) {
//					cbj.put("editUser", " ");
//					cbj.put("CUName", " ");
//				} else {
//					ProfileBean cu = null;
//					if(null!=cb.getEditUserId())cu = HibernateUtilities.searchEmployeeById(cb
//							.getEditUserId());
//					cbj.put("editUser", String.valueOf(cb.getEditUserId()));
//					cbj.put("CUName",
//							null == cu ? "(UNKNOWN USER)" : cu.getWholeName());
//				}
//				chartsJson.add(cbj);
                chartsJson.add(parseChartbeanToJsonObjectNode(cb));
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
    public static Result showChart() {
        final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
        for (Map.Entry<String,String[]> entry : entries) {
            final String key = entry.getKey();
            final String value = Arrays.toString(entry.getValue());
            Logger.debug(key + " " + value);
        }
        String chartId = request().getQueryString("chartid");
        if (null == chartId || chartId.equals("")) {
            chartId = Setting.DefaultData.ChartId_default;
        }
        OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserBySession(session());
        ChartBean chartBean = new ChartBean();
        // Read ChartBean from storage
        if (Setting.STORAGE == StorageSetting.HIBERNATE) {
            try {
                System.out.println("ChartCtrl@getChart: chartid=" + chartId);
                HibernateUtilities.getFactory();
                chartBean = HibernateUtilities
                        .searchChartByUUID(chartId);

                if (isUserReadChartAllowed(chartBean, ocu)) {
                    return ok(views.html.chart.render(chartBean));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return badRequest("Unable to retrieve chart information.");
            }
        }else{
            return internalServerError("Database setting error.");
        }
        return forbidden("Permission denied, you don't have permission to access this chart." +
                " Please contact administrator or chart owner for help");
    }

    @SubjectPresent
    public static Result getChart(String id) {
        OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserBySession(session());
        // Read ChartBean from storage
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

                if (isUserReadChartAllowed(chartBean, ocu)) {
                    return ok(parseChartbeanToJsonObjectNode(chartBean));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return badRequest();
            }
        }
        Logger.info("ChartCtrl@getChart");
        return forbidden(" Permission Denied : You don't have permission to access this chart! ");
    }

    @SubjectPresent
    public static Result requestChartEdit(String id) {
        OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserBySession(session());
        String editReq = request().getQueryString("edit");
        Logger.debug("ChartCtrl@requestChartEdit chartidToChartLockMap=" + chartidToChartLockMap);
        MessageChartStatus msg = new MessageChartStatus();
        msg.editRequest = editReq;
        msg.chartId = id;
        msg.fromUserId = ocu.getIdentifier();
        msg.fromMethod = "@requestChartEdit";

        try {
            HibernateUtilities.getFactory();
            ChartBean chartBean = HibernateUtilities
                    .searchChartByUUID(id);
            if (null == chartBean) {
                msg.status = MessageChartStatus.STATUS_NOT_FOUND;
                msg.msg = "Chart not exist.";
                return notFound(Json.toJson(msg));
            }

            if ("disable".equals(editReq.trim())) {
                Logger.debug("chartidToChartLockMap=" + chartidToChartLockMap);
                if (chartidToChartLockMap.containsKey(id)) {
                    ChartLock lock = chartidToChartLockMap.get(id);
                    if (ocu.isAdmin()) {
                        chartidToChartLockMap.remove(id);
                        msg.status = MessageChartStatus.STATUS_SUCCESS;
                        msg.msg = "Admin has unlocked this chart.";
                        return ok(Json.toJson(msg));
                    }else if(ocu.getIdentifier().equals(lock.getUserId())){
                        chartidToChartLockMap.remove(id);
                        chartBean.setEditUserId(null);
                        HibernateUtilities.saveOrUpdateChart(chartBean);

                        msg.status = MessageChartStatus.STATUS_SUCCESS;
                        msg.msg = "Lock fetch from memory released.";
                        return ok(Json.toJson(msg));
                    }else if(ocu.getIdentifier().equals(chartBean.getEditUserId())){
                        chartidToChartLockMap.remove(id);
                        chartBean.setEditUserId(null);
                        HibernateUtilities.saveOrUpdateChart(chartBean);

                        msg.status = MessageChartStatus.STATUS_SUCCESS;
                        msg.msg = "Lock fetch from database released.";
                        return ok(Json.toJson(msg));
                    }else{
                        msg.status = MessageChartStatus.STATUS_DENIED;
                        msg.msg = "Permission denied, unable to release lock for this chart.";
                        return forbidden(Json.toJson(msg));
                    }
                } else {
                    msg.msg = "Chart is not locked.";
                    return notFound(Json.toJson(msg));
                }
            } else if ("enable".equals(editReq.trim())) {
                if (chartidToChartLockMap.containsKey(id)) {
                    msg.lock = chartidToChartLockMap.get(id);
                    msg.isLocked = MessageChartStatus.IS_LOCKED_TRUE;
                    if(ocu.getIdentifier().equals(chartBean.getEditUserId())){
                        msg.status = MessageChartStatus.STATUS_SUCCESS;
                        msg.msg = "You already lock this chart for editing.";
                        return ok(Json.toJson(msg));
                    }else{
                        msg.status = MessageChartStatus.STATUS_DENIED;
                        msg.msg = "A user has lock this chart for editing.";
                        return forbidden(Json.toJson(msg));
                    }
                }
                if (isUserWriteChartAllowed(chartBean, ocu)
                        ||isUserGetChartLockAllowed(chartBean, ocu)) {
                    //TODO: check database records.
                    chartBean.setEditUserId(ocu.getIdentifier());
                    HibernateUtilities.saveOrUpdateChart(chartBean);
                    ChartLock lock = new ChartLock(ocu.getIdentifier());
                    chartidToChartLockMap.put(id, lock);
                    Logger.debug("chartidToChartLockMap=" + chartidToChartLockMap);

                    msg.status = MessageChartStatus.STATUS_SUCCESS;
                    msg.lock = lock;
                    msg.isLocked = MessageChartStatus.IS_LOCKED_TRUE;
                    return ok(Json.toJson(msg));
                }  else {
                    msg.fromMethod = "@isUserWriteChartAllowed";
                    msg.status = MessageChartStatus.STATUS_DENIED;
                    msg.msg = "Permission denied, unable to edit this chart.";
                    return forbidden(Json.toJson(msg));
                }
                //clear all chart lock for this user.
            } else if ("clearUserLock".equals(editReq.trim())) {
                ArrayList<String> chartsToUnlock = new ArrayList<String>();
                for (String chartId : chartidToChartLockMap.keySet()) {
                    ChartLock lock = chartidToChartLockMap.get(chartId);
                    if (lock.getUserId().equals(ocu.getIdentifier())) {
                        //TODO: Replace for better performance.
                        chartBean.setEditUserId(null);
                        HibernateUtilities.saveOrUpdateChart(chartBean);
                        chartsToUnlock.add(chartId);
                    }
                }
                for (String chartid : chartsToUnlock) {
                    chartidToChartLockMap.remove(chartid);
                }
                msg.fromMethod = "@isUserWriteChartAllowed";
                msg.status = MessageChartStatus.STATUS_SUCCESS;
                msg.msg = "Following charts lock released: "+chartsToUnlock;
                return ok(Json.toJson(msg));
            } else if ("status".equals(editReq.trim())) {
                boolean isLocked = false;
                msg.isLocked = MessageChartStatus.IS_LOCKED_FALSE;
                if (chartidToChartLockMap.containsKey(id)) {
                    msg.isLocked = MessageChartStatus.IS_LOCKED_TRUE;
                    msg.lock = chartidToChartLockMap.get(id);
                }
                //TODO: check database records.
                return ok(Json.toJson(msg));
            } else if ("forceReleaseAllLock".equals(editReq.trim())){
                if(ocu.isAdmin()){
                    msg.msg = "All charts lock released: size="+chartidToChartLockMap.size();
                    //TODO: Perform database operation.
//                    for (String chartId : chartidToChartLockMap.keySet()) {
//                    }
                    chartidToChartLockMap.clear();
                    msg.status = MessageChartStatus.STATUS_SUCCESS;
                    return ok(Json.toJson(msg));
                }else{
                    msg.status = MessageChartStatus.STATUS_DENIED;
                    msg.msg = "Permission denied, only admin can release all lock.";
                    return forbidden(Json.toJson(msg));
                }
            }

        } catch (Exception e) {
            msg.status = MessageChartStatus.STATUS_ERROR;
            msg.msg = "Unable to fetch chart record.";

            return internalServerError(Json.toJson(msg));
        }

        msg.status = MessageChartStatus.STATUS_ERROR;
        msg.msg = "Request command not supported.";
        return new Status(play.core.j.JavaResults.MethodNotAllowed(), Json.toJson(msg), Codec.javaSupported("utf-8"));
    }

    @SubjectPresent
    public static Result getChartXML(String id) {
        OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserBySession(session());
        // Read XML from storage
        if (Setting.STORAGE == StorageSetting.HIBERNATE) {
            String chartId = request().getQueryString("chartid");
            if (null == chartId || chartId.equals("")) {
                chartId = Setting.DefaultData.ChartId_default;
            }
            try {
                System.out.println("ChartCtrl@getChartXML: chartid=" + chartId);
                HibernateUtilities.getFactory();
                ChartBean chartBean = HibernateUtilities
                        .searchChartByUUID(chartId);

                if (isUserReadChartAllowed(chartBean, ocu)) {
                    return ok(chartBean.getXmlString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return badRequest();
            }
        }
        // session("empchart", "<ul id='org' style='display:none'>" + input
        // + "</ul>");
        Logger.info("ChartCtrl@getChartXML");
        return forbidden(" Permission Denied : You don't have permission to access this chart! ");
    }

    @SubjectPresent
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

    @Restrict({@Group(OrgChartRoleType.ADMIN), @Group(OrgChartRoleType.USER)})
    public static Result createChart() {
        DynamicForm form = Form.form().bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.get().toString());
        } else {
            String chart_name = form.get("new_chart_name");
            // Replace to use current login user id
//			String chart_ownerid = form.get("new_chart_owner_id");
            OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserBySession(session());
            Logger.debug("ChartCtrl@createChart OrgChartUser:=" + ocu);
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
//		return forbidden(" Permission Denied : You don't have permission to create chart! ");
    }

    /**
     * This method does not have protection for concurrent edition.
     *
     * @return
     */
    @Restrict({@Group(OrgChartRoleType.ADMIN), @Group(OrgChartRoleType.USER)})
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

                OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserBySession(session());
                if (isUserWriteChartAllowed(cb, ocu)) {
                    cb.setChartName(chart_name);
                    cb.setOwnerID(chart_ownerid);
                    cb.setPermission(chart_permission);
                    cb.setPermittedUser(chart_permitteduser);
                    cb.setTimeLastModifiedNow();
                    cb.setVersion(cb.getVersion() + 1);
                    if (cb.isValid()) {
                        HibernateUtilities.saveOrUpdateChart(cb);
                        return ok(parseChartbeanToJsonObjectNode(cb));
                    } else {
                        return badRequest("Invalid chart parameters\nChartCtrl@updateChart\n"
                                + form.get().toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.error(
                        "ChartCtrl@updateChartXML" + form.get().toString(), e);
                return internalServerError("ChartCtrl@updateChart\n"
                        + form.get().toString());
            }
        }
        return forbidden(" Permission Denied : You need to enter EDIT mode to edit this chart! ");
    }

    @Restrict({@Group(OrgChartRoleType.ADMIN), @Group(OrgChartRoleType.USER)})
    public static Result updateChartXML() {
        System.out.println("ChartCtrl@updateChartXML>>");
        DynamicForm form = Form.form().bindFromRequest();
        String chartId = form.get("chartid");
        String update_str = form.get("UptString");
        try {
            HibernateUtilities.getFactory();
            ChartBean chartBean = HibernateUtilities.searchChartByUUID(chartId);
            if (null != chartBean && null != update_str) {
                OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserBySession(session());
                if (isUserWriteChartAllowed(chartBean, ocu)) {
                    chartBean.setVersion(chartBean.getVersion() + 1);
                    chartBean.setXmlString(update_str);
                    chartBean.setTimeLastModifiedNow();
                    HibernateUtilities.saveOrUpdateChart(chartBean);
                    return ok();
                }
            } else {
                return notFound(chartId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("ChartCtrl@updateChartXML" + form.get().toString(), e);
            return internalServerError("ChartCtrl@updateChartXML\n"
                    + form.get().toString());
        }
        return forbidden(" Permission Denied : You don't have permission to edit this chart! ");
    }

    @Restrict({@Group(OrgChartRoleType.ADMIN), @Group(OrgChartRoleType.USER)})
    public static Result deleteChart(String id) {
        System.out.println("ChartCtrl@deleteChart>> id=" + id);
        DynamicForm form = Form.form().bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.get().toString());
        } else {
            String chart_id = null != id ? id : form.get("chartid");
            try {
                HibernateUtilities.getFactory();
                ChartBean chartBean = HibernateUtilities.searchChartByUUID(chart_id);
                OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserBySession(session());
                if (isUserWriteChartAllowed(chartBean, ocu)) {
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
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.error("ChartCtrl@deleteChart" + form.get().toString(), e);
                return internalServerError("ChartCtrl@deleteChart\n"
                        + form.get().toString());
            }
        }
        return forbidden(" Permission Denied : You don't have permission to delete this chart! ");
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

        if (null == cb.getEditUserId()) {
            cbj.put("editUser", " ");
            cbj.put("CUName", " ");
        } else {
            ProfileBean cu = HibernateUtilities.searchEmployeeById(cb
                    .getEditUserId());
            cbj.put("editUser", cb.getEditUserId());
            cbj.put("CUName", null == cu ? "(UNKNOWN USER)" : cu.getWholeName());
        }
        return cbj;
    }

    private static boolean isUserReadChartAllowed(ChartBean chartBean, OrgChartUser ocu) {
        if (null == ocu || null == chartBean) {
            return false;
        }
//		Logger.debug("ChartCtrl@isUserReadChartAllowed chartBean="+chartBean+", ocu="+ocu);
        //if owner, private would go through here
        Logger.debug("ChartCtrl@isUserReadChartAllowed >> owner=" + chartBean.getOwnerID() + " ,uid=" + ocu.getIdentifier());
        if (chartBean.getOwnerID().trim().equals(ocu.getIdentifier().trim())) {
            Logger.debug("ChartCtrl@isUserReadChartAllowed >> is owner");
            return true;
        }
        // Check roles, admin can do anything
        for (Role r : ocu.getRoles()) {
            if (r.getName().equals(OrgChartRoleType.ADMIN)) {
//				Logger.debug("ChartCtrl@isUserReadChartAllowed >> is admin");
                return true;
            }
        }
        // Check specified permission
        if (chartBean.getPermission().equals(ChartBean.PERMISSION_SPECIFIED)) {
            for (String sid : chartBean.getPermittedUser().split(";")) {
                if (sid.equals(ocu.getIdentifier())) {
//					Logger.debug("ChartCtrl@isUserReadChartAllowed >> is specified");
                    return true;
                }
            }
            // Lowest permission required - public
        } else if (chartBean.getPermission().equals(ChartBean.PERMISSION_OPTIONS[0])) {
//			Logger.debug("ChartCtrl@isUserReadChartAllowed >> is public");
            return true;
        } else {
            Logger.error("ChartCtrl@isUserReadChartAllowed unknown chart permission : " + chartBean.getPermission());
        }
        return false;
    }

    private static boolean isUserWriteChartAllowed(ChartBean chartBean, OrgChartUser ocu) {
        if (null == ocu || null == chartBean) {
            return false;
        }
        if (chartidToChartLockMap.containsKey(chartBean.getUuid())
                && chartidToChartLockMap.get(chartBean.getUuid()).getUserId().equals(ocu.getIdentifier())){
            // Check roles, admin can do everything.
            for (Role r : ocu.getRoles()) {
                if (r.getName().equals(OrgChartRoleType.ADMIN)) {
                    return true;
                }
            }
            // User can write chart that has read access.
            for (Role r : ocu.getRoles()) {
                if (r.getName().equals(OrgChartRoleType.USER)) {
                    if (true == isUserReadChartAllowed(chartBean, ocu)) {
                        return true;
                    }
                }
            }
            return false;
        }

        // Readonly
        return false;
    }

    private static boolean isUserGetChartLockAllowed(ChartBean chartBean, OrgChartUser ocu) {
        if (null == ocu || null == chartBean) {
            return false;
        }
        if (chartidToChartLockMap.containsKey(chartBean.getUuid())
                && chartidToChartLockMap.get(chartBean.getUuid()).getUserId().equals(ocu.getIdentifier())){
            // User can write chart that has read access.
            for (Role r : ocu.getRoles()) {
                if (r.getName().equals(OrgChartRoleType.USER)) {
                    if (true == isUserReadChartAllowed(chartBean, ocu)) {
                        return true;
                    }
                }
            }
            return false;
        }

        if(false == chartidToChartLockMap.containsKey(chartBean.getUuid())){
            // Check roles, admin can do everything.
            for (Role r : ocu.getRoles()) {
                if (r.getName().equals(OrgChartRoleType.ADMIN)) {
                    return true;
                }
            }
            // User can write chart that has read access.
            for (Role r : ocu.getRoles()) {
                if (r.getName().equals(OrgChartRoleType.USER)) {
                    if (true == isUserReadChartAllowed(chartBean, ocu)) {
                        return true;
                    }
                }
            }
        }

        // No permission user OR chart locked.
        return false;
    }


}
