import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import providers.NyGovUsernameAuthProvider;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.AuthProvider;


public class TestNyGovUsernameAuthProvider {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		AuthProvider obj = PlayAuthenticate.getProvider(NyGovUsernameAuthProvider.PROVIDER_KEY);
		System.out.println(obj);
	}

}
