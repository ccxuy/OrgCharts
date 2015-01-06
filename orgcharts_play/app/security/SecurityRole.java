package security;

import be.objectify.deadbolt.core.models.Role;


public class SecurityRole implements Role {

	public String roleName;

	public SecurityRole(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String getName() {
		return roleName;
	}
}
