package authentication;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

/**
 * Default mock implementation of {@link AccessControl}. This implementation
 * accepts any string as a password, and considers the user "admin" as the only
 * administrator.
 */
public class BasicAccessControl implements AccessControl {

	@Override
	public boolean isUserSignedIn() {
		try {
			CurrentUser.get();
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public boolean isUserInRole(String role) {
		if ("admin".equals(role)) {
			// Only the "admin" user is in the "admin" role
			return getPrincipalName().equals("admin");
		}
		return true;
	}

	@Override
	public String getPrincipalName() {
		return CurrentUser.get();
	}

	@Override
	public void signOut() {
		VaadinSession.getCurrent().getSession().invalidate();
	}

	@Override
	public boolean signIn(String name) {
		CurrentUser.set(name);
		return true;
	}
}
