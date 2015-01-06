package providers;

import com.feth.play.module.pa.user.AuthUser;

public class NyAuthUser extends AuthUser{
	
	String shortname = "none";
    String role = "none";

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    @Override
	public String getId() {
		return shortname;
	}

	@Override
	public String getProvider() {
		// TODO Auto-generated method stub
		return OrgChartAuthProvider.PROVIDER_KEY;
	}

}
