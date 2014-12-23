package us.ny.state.cookie;

import us.ny.state.cookie.CookieManager;

public class UserInfo {

	public UserInfo() {

	}

	public void inputCookies(CookieManager cookieman) {

		// PermCookie
		// PCVERS:2|STAGE:DEV|HINUSER:bxy02|HINAPPWD:aneasy1|HINAPDBID:none|HINAPLID:uphn|HINPROWS:1|HINPERM1:all!all!demo;
		// __utma=228115295.667518567.1205935877.1205935877.1205935877.1;
		// __utma=266341271.1604939042.1205964859.1205964859.1205964859.1;

		this.setHinuser(cookieman.getParamFromCookie("PermCookie", "HINUSER"));
		this.setStage(cookieman.getParamFromCookie("PermCookie", "STAGE"));
		String str_hinprows = cookieman.getParamFromCookie("PermCookie",
				"HINPROWS");
		int count_hinpermrows = 0;
		try {
			count_hinpermrows = new Integer(str_hinprows).intValue();
		} catch (NumberFormatException numex) {
			count_hinpermrows = 0;
		}

		this.setHinprows(count_hinpermrows);

		HinPerm[] hinperms = new HinPerm[this.getHinprows()];
		for (int i = 0; i < this.getHinprows(); i++) {
			HinPerm hinp = new HinPerm();
			String hinpermrow = cookieman.getParamFromCookie("PermCookie",
					"HINPERM" + (i + 1));
			String segs[] = hinpermrow.split("!");
			if (segs.length == 3) {
				hinp.setOrgType(segs[0]);
				hinp.setOrgID(segs[1]);
				hinp.setRole(segs[2]);
			}
			hinperms[i] = hinp;
		}

		this.setHinperms(hinperms);

		// WaarpCookie
		// TICKET%3A275318D044ED5D07F2D8C1120638F131C9B6C169D27D1FDEE314A94194EFE703737FB2D2244CCD3E%7CWCVERS%3A2%7CFULLNAME%3ATony%28Bin%29%20Yan%7CEMAIL%3Abxy02%40health.state.ny.us%7CORG%3ANYSDOH%20ISHSG%7CPHONE%3A518-473-1809%7CPHONEEXT%3A%7CCOMMERCEID%3Abxy02%7C;
		// JSESSIONID=JLLNRXzT2Gt2p2pGnXdRbCmWgFrCdl9LypBRXwPcgYrQQymfpYBW!1461955404
		this.setFullname(cookieman
				.getParamFromCookie("WaarpCookie", "FULLNAME"));
		this.setEmail(cookieman.getParamFromCookie("WaarpCookie", "EMAIL"));
		this.setPhone(cookieman.getParamFromCookie("WaarpCookie", "PHONE"));
		this.setPhoneext(cookieman
				.getParamFromCookie("WaarpCookie", "PHONEEXT"));
		this.setTicket(cookieman.getParamFromCookie("WaarpCookie", "TICKET"));
		this.setCommerceid(cookieman.getParamFromCookie("WaarpCookie",
				"COMMERCEID"));
		this.setOrg(cookieman.getParamFromCookie("WaarpCookie", "ORG"));
		this.setJsessionid(cookieman.getParamFromCookie("WaarpCookie",
				"JSESSIONID"));

	}

	private String hinuser, ticket, fullname, email, org, phone, phoneext,
			commerceid, jsessionid, stage;
	private int hinprows;
	private HinPerm[] hinperms;

	public String getJsessionid() {
		return jsessionid;
	}

	public void setJsessionid(String jsessionid) {
		this.jsessionid = jsessionid;
	}

	public String getCommerceid() {
		return commerceid;
	}

	public void setCommerceid(String commerceid) {
		this.commerceid = commerceid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public HinPerm[] getHinperms() {
		return hinperms;
	}

	public void setHinperms(HinPerm[] hinperms) {
		this.hinperms = hinperms;
	}

	public int getHinprows() {
		return hinprows;
	}

	public void setHinprows(int hinprows) {
		this.hinprows = hinprows;
	}

	public String getHinuser() {
		return hinuser;
	}

	public void setHinuser(String hinuser) {
		this.hinuser = hinuser;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoneext() {
		return phoneext;
	}

	public void setPhoneext(String phoneext) {
		this.phoneext = phoneext;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String toString() {

		StringBuffer sb = new StringBuffer("");
		sb.append("UserInfo:");
		sb.append(" hinuser=").append(hinuser);
		sb.append(" ticket=").append(ticket);
		sb.append(" fullname=").append(fullname);
		sb.append(" email=").append(email);
		sb.append(" org=").append(org);
		sb.append(" phone=").append(phone);
		sb.append(" phoneext=").append(phoneext);
		sb.append(" commerceid=").append(commerceid);
		sb.append(" jsessionid=").append(jsessionid);
		sb.append(" stage=").append(stage);
		sb.append(" hinprows=").append(this.hinprows + "");
		if (hinperms != null) {
			for (int i = 0; i < this.hinperms.length; i++) {
				sb.append("[").append(i + "").append("]=")
						.append(hinperms[i].toString());
			}
		} else {
			sb.append("No Hin Perms");
		}

		return sb.toString();

	}

}
