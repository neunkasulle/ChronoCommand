package com.github.neunkasulle.chronocommand.view;

import com.ejt.vaadin.loginform.LoginForm;
import com.github.neunkasulle.chronocommand.control.LoginControl;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by Janze on 20.01.2016.
 */
public class LoginView extends LoginForm implements View {
    private Logger log = LoggerFactory.getLogger(LoginView.class);

    protected CheckBox rememberMe;
    protected Label authInfoMissing;
    protected Label authenticationFailed;
    protected PasswordField passwordField;


    @Override
    protected Component createContent(TextField usernameField, PasswordField passwordField, Button loginButton) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        Label label = new Label("Please log in");
        label.setStyleName(ValoTheme.LABEL_H1);
        label.setSizeUndefined();
        layout.addComponent(label);
        layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);


        usernameField.setCaption("Username");
        layout.addComponent(usernameField);
        layout.setComponentAlignment(usernameField, Alignment.MIDDLE_CENTER);

        this.passwordField = passwordField;
        passwordField.setCaption("Password");
        layout.addComponent(passwordField);
        layout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);

        rememberMe = new CheckBox("Remember me");
        layout.addComponent(rememberMe);
        layout.setComponentAlignment(rememberMe, Alignment.MIDDLE_CENTER);

        layout.addComponent(loginButton);
        layout.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);

        authInfoMissing = new Label("Please provide username and password!");
        authInfoMissing.setStyleName(ValoTheme.LABEL_FAILURE);
        authInfoMissing.setSizeUndefined();
        authInfoMissing.setVisible(false);
        layout.addComponent(authInfoMissing);
        layout.setComponentAlignment(authInfoMissing, Alignment.MIDDLE_CENTER);

        authenticationFailed = new Label("Username or password incorrect");
        authenticationFailed.setStyleName(ValoTheme.LABEL_FAILURE);
        authenticationFailed.setSizeUndefined();
        authenticationFailed.setVisible(false);
        layout.addComponent(authenticationFailed);
        layout.setComponentAlignment(authenticationFailed, Alignment.MIDDLE_CENTER);

        Button forgotPassword = new Button("Forgot password");
        forgotPassword.setStyleName(ValoTheme.BUTTON_LINK);
        forgotPassword.addClickListener(this::forgotPasswordClicked);
        layout.addComponent(forgotPassword);
        layout.setComponentAlignment(forgotPassword, Alignment.MIDDLE_CENTER);

        // DEBUG
        HorizontalLayout links = new HorizontalLayout();
        layout.addComponent(links);

        Button timeRecord = new Button("TimeRecord");
        timeRecord.addClickListener(event -> {
            getUI().getNavigator().navigateTo(MainUI.TIMERECORDVIEW);
        });
        links.addComponent(timeRecord);

        Button admin = new Button("Admin");
        admin.addClickListener(event -> {
            getUI().getNavigator().navigateTo(MainUI.ADMINVIEW);
        });
        links.addComponent(admin);

        Button employees = new Button("Employees");
        employees.addClickListener(event -> {
            getUI().getNavigator().navigateTo(MainUI.SUPERVISORVIEW);
        });
        links.addComponent(employees);

        Button createUser = new Button("Create User");
        createUser.addClickListener(event -> {
            getUI().getNavigator().navigateTo(MainUI.CREATEUSERVIEW);
        });
        links.addComponent(createUser);

        Button timesheet = new Button("Timesheet");
        timesheet.addClickListener(event -> {
            getUI().getNavigator().navigateTo(MainUI.TIMESHEETVIEW);
        });
        links.addComponent(timesheet);

        Button settings = new Button("Usersettings");
        settings.addClickListener(event -> {
            getUI().getNavigator().navigateTo(MainUI.SETTINGSVIEW);
        });
        links.addComponent(settings);



        return layout;
    }

    public void loginClicked(LoginEvent loginEvent) {
        log.info("User: {} Password: {} Remember Me: {}", new Object[]{loginEvent.getUserName(), loginEvent.getPassword(), rememberMe.getValue()});

        if (loginEvent.getUserName().isEmpty() || loginEvent.getPassword().isEmpty()) {
            authenticationFailed.setVisible(false);
            authInfoMissing.setVisible(true);
            return;
        }
        authInfoMissing.setVisible(false);

        try {
            LoginControl.getInstance().login(loginEvent.getUserName(), loginEvent.getPassword(), rememberMe.getValue());
            authenticationFailed.setVisible(false);
            getUI().getNavigator().navigateTo(MainUI.TIMERECORDVIEW);
        } catch(ChronoCommandException e) {
            authenticationFailed.setVisible(true);
            passwordField.clear();
        }
    }

    public void forgotPasswordClicked(Button.ClickEvent clickEvent) {
        log.info("User clicked on \"Forgot password\"");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        addLoginListener(this::loginClicked);
        setSizeFull();
    }
}
