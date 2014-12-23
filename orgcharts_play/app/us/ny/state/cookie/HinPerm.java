package us.ny.state.cookie;

public class HinPerm {
	private String orgType, orgID, role;

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String toString() {

		StringBuffer sb = new StringBuffer("");
		sb.append(" HinPerm:");
		sb.append(" orgType:").append(this.orgType);
		sb.append(" orgID:").append(this.orgID);
		sb.append(" role:").append(this.role);

		return sb.toString();
	}
}