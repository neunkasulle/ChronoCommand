package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.UserManagementControl;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by Janze on 20.01.2016.
 */
public class InitialStartupView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setSpacing(true);

        Label label = new Label("Please create an administrator");
        label.addStyleName(ValoTheme.LABEL_H3);
        addComponent(label);

        TextField username = new TextField("Username:");
        addComponent(username);

        TextField email = new TextField("Email:");
        addComponent(email);

        PasswordField password1 = new PasswordField("Password:");
        addComponent(password1);

        PasswordField password2 = new PasswordField("Please repeat password:");
        addComponent(password2);

        TextField realname = new TextField("Real name:");
        addComponent(realname);

        Button createAdmin = new Button("Create administrator");
        createAdmin.addClickListener(clickEvent -> {
            try {
                UserManagementControl.getInstance().createInitialAdministrator(username.getValue(), email.getValue(), password1.getValue(), realname.getValue());
                getUI().getNavigator().navigateTo(MainUI.LOGINVIEW);
            } catch(ChronoCommandException e) {
                Notification.show("Error: " + e.getReason().toString(), Notification.Type.WARNING_MESSAGE);
            }
        });
        addComponent(createAdmin);
    }
}
