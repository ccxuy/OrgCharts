package security;

import java.util.ArrayList;
import java.util.List;

import play.Logger;
import us.ny.state.cookie.UserInfo;
import us.ny.state.cookie.UserInfoIntegration;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

public class OrgChartUser implements Subject, UserInfoIntegration {
	String id = "NONE";
	public List<SecurityRole> roles = new ArrayList<SecurityRole>();
	public List<UserPermission> permissions = new ArrayList<UserPermission>();
	UserInfo userInfo;
	

	public OrgChartUser() {
		super();
	}
	
	public OrgChartUser(UserInfo userInfo) {
		super();
		this.userInfo = userInfo;
		this.id = userInfo.getHinuser();
		this.setRoles(userInfo.getAllRoles());
	}

	public OrgChartUser(final AuthUserIdentity identity) {
		super();
	}
	
	public void addRole(String role){
//        Logger.debug("debug OrgChartUser@addRole role="+role);
		if(null!=role){
            this.roles.add(new SecurityRole(role));
        }
	}

	public void setRoles(ArrayList<String> allRoles) {
		for(String r:allRoles){
			this.roles.add(new SecurityRole(r));
		}
	}

    public void setId(String id) {
        this.id = id;
    }

    @Override
	public String getIdentifier() {
		return id;
	}

	@Override
	public List<? extends Permission> getPermissions() {
		return this.permissions;
	}

	@Override
	public List<? extends Role> getRoles() {
		return this.roles;
	}

	@Override
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public UserInfo getUserInfo() {
		return this.userInfo;
	}

}
