package views;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import authentication.AccessControlFactory;

@Route("login")
@PageTitle("MainView")
public class LoginView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	public LoginView() {
		TextField nickname = new TextField("Login", "Nickname");
		Button loginButton = new Button("Lets Go!", e -> {
			if (nickname.getValue() == null || nickname.getValue().contentEquals("")) {
				Notification.show("Invalid Nickname");
			} else {
				authentication.AccessControl accessControl = AccessControlFactory.getInstance().getAccessControl();
				accessControl.signIn(nickname.getValue());
				UI.getCurrent().navigate(MainView.class);
			}
		});
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.add(nickname, loginButton);
		add(hl);
	}
}
