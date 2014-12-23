package security;

import java.util.List;

import scala.tools.nsc.settings.Final;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

public class OrgChartUser implements Subject {
	String id;
	String role;
	

	public OrgChartUser() {
		super();
	}

	public OrgChartUser(String id, String role) {
		super();
		this.id = id;
		this.role = role;
	}
	
	public OrgChartUser(final AuthUserIdentity identity) {
		super();
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends Permission> getPermissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends Role> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

}
