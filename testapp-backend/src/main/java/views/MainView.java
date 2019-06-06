package views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import applicationstuff.Broadcaster;
import authentication.AccessControlFactory;
import authentication.CurrentUser;

@Route("")
@PageTitle("MainView")
@Push
public class MainView extends VerticalLayout implements BeforeLeaveObserver, BeforeEnterObserver {
	private static final long serialVersionUID = 1L;
	Registration broadcasterRegistration;
    VerticalLayout messages = new VerticalLayout();

	public MainView() {
		Label label = new Label("MainView");
		
		TextField message = new TextField();
	    Button send = new Button("Send", e -> {
	        Broadcaster.broadcast(CurrentUser.get() + ":    " + message.getValue());
	        message.setValue("");
	    });

	    HorizontalLayout sendBar = new HorizontalLayout(message, send);
	    add(label, sendBar, messages);
	}
	
	@Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        broadcasterRegistration = Broadcaster.register(newMessage -> {
            ui.access(() -> messages.add(new Span(newMessage)));
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        broadcasterRegistration.remove();
        broadcasterRegistration = null;
    }

	@Override
	public void beforeLeave(BeforeLeaveEvent event) {
		authentication.AccessControl accessControl = AccessControlFactory.getInstance().getAccessControl();
		accessControl.signOut();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		authentication.AccessControl accessControl = AccessControlFactory.getInstance().getAccessControl();
		if(!accessControl.isUserSignedIn()) {
			event.rerouteTo(LoginView.class);
		}
	}
}
