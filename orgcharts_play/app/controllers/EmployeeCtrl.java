package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.common.collect.Iterables;
import model.MessageCommon;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import security.OrgChartDeadboltHandler;
import security.OrgChartRoleType;
import security.OrgChartUser;
import utilities.HibernateUtilities;
import beans.ChartBean;
import beans.ProfileBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import config.Setting;
import config.Setting.StorageSetting;

public class EmployeeCtrl extends Controller {

	public static Result index() {
		return redirect("./test");
	}

	@BodyParser.Of(BodyParser.Json.class)
	@Restrict({@Group(OrgChartRoleType.ADMIN), @Group(OrgChartRoleType.USER), @Group(OrgChartRoleType.READONLY)})
	public static Result getAllEmployee() {
		try {
			HibernateUtilities.getFactory();
			List<ProfileBean> empList = HibernateUtilities.getAllEmployee();

			ObjectMapper mapper = new ObjectMapper();
//			mapper.addMixIn(ProfileBean.class, ProfileBeanIgnoreFieldsMixIn.class);
			ArrayNode empArray = mapper.valueToTree(empList);
			ObjectNode dataNode = mapper.createObjectNode();
			dataNode.putArray("data").addAll(empArray);

			return ok(dataNode);
		} catch (Exception e) {
			Logger.error("EmployeeCtrl@getAllEmployee", e);
			return internalServerError();
		}
	}

	/**
	 * @return
	 * @deprecated
	 */
	@Deprecated
	@Restrict({@Group(OrgChartRoleType.ADMIN), @Group(OrgChartRoleType.USER), @Group(OrgChartRoleType.READONLY)})
	public static Result getAllEmployeeNameList() {
		try {
			HibernateUtilities.getFactory();
			@SuppressWarnings("unchecked")
			List mylist = HibernateUtilities.searchId();
			String temp = "";
			String resp = "";
			for (int x = 0; x < mylist.size() - 1; x++) {
				temp = mylist.get(x).toString();
				resp += temp;
				resp += ",";
			}
			resp += mylist.get(mylist.size() - 1);
			return ok(resp);
		} catch (IOException e) {
			e.printStackTrace();
			return internalServerError();
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError();
		}
	}

	/**
	 * @return
	 * @deprecated
	 */
	@Deprecated
	@Restrict({@Group(OrgChartRoleType.ADMIN), @Group(OrgChartRoleType.USER), @Group(OrgChartRoleType.READONLY)})
	public static Result getEmployeeEditData() {
		try {
			String empId = request().getQueryString("empId");
			HibernateUtilities.getFactory();
			@SuppressWarnings("unchecked")
			List mylist = HibernateUtilities.getEditedData(empId);
			if(null==mylist){
				return internalServerError();
			}
			String temp = "";
			String tmp0 = "";
			if (null != mylist.get(0))
				tmp0 = mylist.get(0).toString();
			String resp = tmp0 + ",";
			for (int x = 1; x < mylist.size(); x++) {
				temp = mylist.get(x).toString();
				resp += temp
						.substring(temp.indexOf('[') + 1, temp.indexOf(']'))
						+ ",";
			}
			return ok(resp);
		} catch (IOException e) {
			e.printStackTrace();
			return internalServerError();
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError();
		}
	}

	@Restrict({@Group(OrgChartRoleType.ADMIN), @Group(OrgChartRoleType.USER), @Group(OrgChartRoleType.READONLY)})
	public static Result getEmployee(String id) {
		OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserBySession(session());
		MessageCommon msg = new MessageCommon();
		msg.fromMethod = "getEmployee";
		msg.fromUserId = ocu.getIdentifier();
		// Read XML from storage
		String input = "";
		if (Setting.STORAGE == StorageSetting.HIBERNATE) {
			try {
				HibernateUtilities.getFactory();
				ProfileBean employee = HibernateUtilities.searchEmployeeById(id);
				ObjectNode employeeJson = (ObjectNode)Json.toJson(employee);
				employeeJson.put("hasRelatedChart", employee.hasRelatedChart());
				employeeJson.put("hasImage", employee.getImg()!=null);
				return ok(employeeJson);
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error("EmployeeCtrl@getEmployee", e);
				msg.msg = "Unable to get employee with id="+id+". It may already got deleted.";
				return badRequest(Json.toJson(msg));
			}
		}
//		session("empchart", "<ul id='org' style='display:none'>" + input
//				+ "</ul>");
		return ok(input);
	}

	@Restrict({@Group(OrgChartRoleType.ADMIN), @Group(OrgChartRoleType.USER), @Group(OrgChartRoleType.READONLY)})
	public static Result getEmployeeImage() {
		try {
			HibernateUtilities.getFactory();
			String emp_id = request().getQueryString("empId");
			if (null != emp_id) {
				ProfileBean empBean = HibernateUtilities
						.searchEmployeeById(emp_id);

				if (empBean.hasNoImage())
					return notFound();

				InputStream is = empBean.getImg().getBinaryStream();
				int bufferSize = 1024;
				return ok(is, bufferSize).as("image/jpeg");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("EmployeeCtrl@getEmployeeImage", e);
			return internalServerError();
		}
		return badRequest();
	}

	@Restrict({@Group(OrgChartRoleType.ADMIN),@Group(OrgChartRoleType.USER)})
	public static Result createEmployee() {
		try {
			HibernateUtilities.getFactory();

			DynamicForm form = Form.form().bindFromRequest();
			String first_name = form.get("new_first_name");
			String last_name = form.get("new_last_name");
			// Since first name and last name is required, if they are empty,
			// return 400
			if (null == first_name || null == last_name) {
				return badRequest("First name and last name are required to create an employee");
			}

			ProfileBean employeeProfileBean = new ProfileBean(first_name,
					last_name);
			// Get the rest of fields
			employeeProfileBean.setEmployeeTitle(form.get("new_title"));
			employeeProfileBean.setLocation(form.get("new_location"));
			employeeProfileBean.setEmail(form.get("new_email"));
			employeeProfileBean.setPhone(form.get("new_phone"));
			employeeProfileBean.setFax(form.get("new_fax"));
			employeeProfileBean.setEmployeeStatus(form.get("new_status"));
			employeeProfileBean.setExtraString(form.get("new_extra"));

			MultipartFormData mfrom = request().body().asMultipartFormData();
			if (null != mfrom.getFile("new_image")) {
				File imgPart = mfrom.getFile("new_image").getFile();
				employeeProfileBean.setImg(imgPart);
			}
			
			System.out.println("@createEmployee: "+employeeProfileBean);

			if(false == employeeProfileBean.isValid()){
				return badRequest("invalid employee");
			}

			int ret = HibernateUtilities
					.saveOrUpdateEmployee(employeeProfileBean);
			if (ret > 0) {
				return ok(parseEmployeeBeanToJsonObjectNode(employeeProfileBean));
			} else {
				return ok("-1");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("EmployeeCtrl@createEmployee", e);
			return internalServerError();
		}
	}

	@Restrict({@Group(OrgChartRoleType.ADMIN),@Group(OrgChartRoleType.USER)})
	public static Result updateEmployee() {
		System.out.println("EmployeeCtrl@updateEmployee>>");
		try {
			DynamicForm form = Form.form().bindFromRequest();
			HibernateUtilities.getFactory();
			String emp_id = form.get("emp_Id");
			ProfileBean employeeProfileBean = HibernateUtilities.searchEmployeeById(emp_id);
			if(null!=employeeProfileBean){
				
				employeeProfileBean.setFirstName(form.get("edit_first_name"));
				employeeProfileBean.setLastName(form.get("edit_last_name"));
				employeeProfileBean.setEmployeeTitle(form.get("edit_emp_title"));
				employeeProfileBean.setLocation(form.get("edit_location"));
				employeeProfileBean.setEmail(form.get("edit_email"));
				employeeProfileBean.setPhone(form.get("edit_phone"));
				employeeProfileBean.setFax(form.get("edit_fax"));
				employeeProfileBean.setEmployeeStatus(form.get("edit_status"));
				employeeProfileBean.setExtraString(form.get("edit_extra"));

				MultipartFormData mfrom = request().body()
						.asMultipartFormData();
				if (null != mfrom.getFile("image")) {
					File image = mfrom.getFile("image").getFile();
					employeeProfileBean.setImg(image);
				}
				System.out.println(employeeProfileBean);

				MessageCommon msg = new MessageCommon();
				if(employeeProfileBean.isValid(msg)){
					HibernateUtilities.editNode(employeeProfileBean);
					return ok(parseEmployeeBeanToJsonObjectNode(employeeProfileBean));
				}else{
					return badRequest(Json.toJson(msg));
				}
			}
			// This is the old way..
			// HibernateUtilities.editNode(first_name, last_name, email, null,
			// Integer.parseInt(emp_id));
			// request.getRequestDispatcher("/index.jsp").forward(request,
			// response);
			return badRequest("Employee not found");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("EmployeeCtrl@updateEmployee", e);
			return internalServerError();
		}
	}

	@Restrict({@Group(OrgChartRoleType.ADMIN)})
	public static Result deleteEmployee(String id) {
		DynamicForm form = Form.form().bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(form.get().toString());
		} else {
			String emp_id = null != id ? id : form.get("emp_id");
			try {
				ProfileBean emp = HibernateUtilities.searchEmployeeById(emp_id);
				Logger.debug("EmployeeCtrl@deleteEmployee emp="+emp);

				//Check employee related charts
//				Set<ChartBean> relCharts = emp.getRelatedCharts();
//				if(relCharts!=null && relCharts.size()>0){
//					ChartBean c = null;
//					for(ChartBean cb : relCharts){
//						c = cb;
//						break;
//					}
//					return badRequest("This employee has linked to some charts.\n " +
//							"One of them has name:"+ c.getChartName() + " ,id:" +c.getUuid() +"\n");
//				}
				int ret = HibernateUtilities.deleteEmployeeById(emp_id);
				switch (ret) {
				case -1:
					return badRequest("employee id not exist.\n EmployeeCtrl@deleteEmployee\n");
				case 0:
					return internalServerError("Hibernate Error\n EmployeeCtrl@deleteEmployee\n");
				case 1:
					return ok();

				default:
					return internalServerError("Unkown Error\n EmployeeCtrl@deleteEmployee\nreturn error code: "+ret);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error("EmployeeCtrl@deleteEmployee" + form.get().toString(), e);
				return internalServerError("EmployeeCtrl@deleteEmployee\n"
						+ form.get().toString());
			}
		}
	}

	private static ObjectNode parseEmployeeBeanToJsonObjectNode(
			ProfileBean employeeProfileBean) {
		ObjectNode bj = (ObjectNode) Json.toJson(employeeProfileBean);
		return bj;
	}

}
