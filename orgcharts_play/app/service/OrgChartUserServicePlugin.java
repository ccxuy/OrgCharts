package service;

import play.Application;

import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.service.UserServicePlugin;
import security.OrgChartUser;

public class OrgChartUserServicePlugin extends UserServicePlugin {

	public OrgChartUserServicePlugin(final Application app) {
        super(app);
        System.out.println("OrgChartUserServicePlugin@Constructor");
	}

	@Override
	public Object save(final AuthUser authUser) {
        System.out.println("OrgChartUserServicePlugin@save");
//		final boolean isLinked = User.existsByAuthUserIdentity(authUser);
//		if (!isLinked) {
//			return User.create(authUser).id;
//		} else {
//			// we have this user already, so return null
//			return null;
//		}
		return authUser;
	}

	@Override
	public Object getLocalIdentity(final AuthUserIdentity identity) {
        System.out.println("OrgChartUserServicePlugin@save identity="+identity);
//		// For production: Caching might be a good idea here...
//		// ...and dont forget to sync the cache when users get deactivated/deleted
//		final User u = User.findByAuthUserIdentity(identity);
//		if(u != null) {
//			return u.id;
//		} else {
//			return null;
//		}
//        final OrgChartUser u = new OrgChartUser();
//		return u.getIdentifier();
		// We don't need to save user in requirement.
		return identity.getId();
	}

	@Override
	public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
        System.out.println("OrgChartUserServicePlugin@merge newUser="+newUser);
//		if (!oldUser.equals(newUser)) {
//			User.merge(oldUser, newUser);
//		}
//		return oldUser;
		return null;
	}

	@Override
	public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
        System.out.println("OrgChartUserServicePlugin@link oldUser="+oldUser);
//		User.addLinkedAccount(oldUser, newUser);
//		return newUser;
		return null;
	}
	
	@Override
	public AuthUser update(final AuthUser knownUser) {
        System.out.println("OrgChartUserServicePlugin@update knownUser="+knownUser);
//		// User logged in again, bump last login date
//		User.setLastLoginDate(knownUser);
//		return knownUser;
		return super.update(knownUser);
	}

}
