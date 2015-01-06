package security;

import be.objectify.deadbolt.core.models.Permission;

public class UserPermission implements Permission {
	public String value;
	public String getValue() {
		return value;
	}

}
