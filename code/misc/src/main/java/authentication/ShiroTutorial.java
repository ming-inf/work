package authentication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class ShiroTutorial {
	public static void main(String[] args) {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);

		ShiroTutorial app = new ShiroTutorial();

		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {
			String username = "darkhelmet";
			char[] password = "ludicrousspeed".toCharArray();

			// collect user principals and credentials in a gui specific manner
			// such as username/password html form, X509 certificate, OpenID, etc.
			// We'll use the username/password example here since it is the most common.
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);

			// this is all you have to do to support 'remember me' (no config - built in!):
			token.setRememberMe(true);

			currentUser.login(token);

			System.out.println("User [" + currentUser.getPrincipal() + "] logged in successfully.");

			app.authorizedAction();
		}

		try {
			app.action();
		} catch (Exception e) {
			System.out.println("unauthorized to perform goodguy action");
		}
	}

	public void authorizedAction() {
		Subject user = SecurityUtils.getSubject();
		user.checkPermission("lightsaber");
		System.out.println("lightsaber action");
	}

	public void action() {
		Subject user = SecurityUtils.getSubject();
		user.checkRole("goodguy");
		;
		System.out.println("goodguy action");
	}
}
