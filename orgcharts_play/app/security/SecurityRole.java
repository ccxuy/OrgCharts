package security;

import be.objectify.deadbolt.core.models.Role;

public class SecurityRole implements Role {

	public OrgChartAccessRole role = OrgChartAccessRole.none;

	@Override
	public String getName() {
		return role.name();
	}
	
	public enum OrgChartAccessRole{
		admin,
		user,
		readonly,
		none
	}
}
