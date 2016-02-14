package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.CreateUserControl;
import com.github.neunkasulle.chronocommand.control.MainControl;
import com.github.neunkasulle.chronocommand.control.UserManagementControl;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.User;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Janze on 20.01.2016.
 */
public class CreateUserView extends BaseView {
    private final Logger LOGGER = LoggerFactory.getLogger(CreateUserView.class);

    private ComboBox selectRole;
    private ComboBox selectSupervisor;
    private TextField hoursPerMonth;
    private TextField usernameInputField;
    private TextField realnameInputField;
    private TextField emailInputField;
    private PasswordField passwordFirstTimeInputField;
    private PasswordField passwordSecondTimeInputField;

    public void createUserClicked() {

    }

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {

        final VerticalLayout siteInfo = new VerticalLayout();
        siteInfo.setSizeFull();

        siteInfo.addComponent(new Label("Create new account:"));
        contentPane.addComponent(siteInfo);

        //Combobox Area
        final HorizontalLayout selectProperty = new HorizontalLayout();
        selectProperty.setSizeFull();
        selectProperty.setSpacing(true);
        contentPane.addComponent(selectProperty);

        selectRole = new ComboBox("Role:");
        List<Role> roleList = UserManagementControl.getInstance().getAllRoles();
        for (Role role : roleList) {
            if (role.isPrimaryRole()) {
                selectRole.addItem(role);
            }
        }
        selectRole.addValueChangeListener(event1 -> {
            if (UserManagementControl.getInstance().getRoleByName(MainControl.ROLE_PROLETARIER).equals(selectRole.getValue())) {
                selectSupervisor.setVisible(true);
                hoursPerMonth.setVisible(true);
            } else if (UserManagementControl.getInstance().getRoleByName(MainControl.ROLE_SUPERVISOR).equals(selectRole.getValue())) {
                selectSupervisor.setVisible(false);
                hoursPerMonth.setVisible(true);
            } else {
                selectSupervisor.setVisible(false);
                hoursPerMonth.setVisible(false);
            }
        });
        selectProperty.addComponent(selectRole);

        selectSupervisor = new ComboBox("Supervisor:");
        try {
            List<User> supervisorList = UserManagementControl.getInstance().getUsersByRole(UserManagementControl.getInstance().getRoleByName(MainControl.ROLE_SUPERVISOR));
            selectSupervisor.addItems(supervisorList);
        } catch (ChronoCommandException e) {
            LOGGER.warn("Failed to get supervisors: " + e.getReason().toString());
            e.printStackTrace();
            Notification.show("Failed to get supervisors: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
        }
        selectSupervisor.setVisible(false);
        selectProperty.addComponent(selectSupervisor);


        /* Input Area */

        hoursPerMonth = new TextField("Hours per Month:");
        hoursPerMonth.setSizeFull();
        hoursPerMonth.setImmediate(true);
        hoursPerMonth.setVisible(false);
        contentPane.addComponent(hoursPerMonth);

        //TODO @Wang Xiaoming 想办法把前3个Field拉伸
        /*final VerticalLayout userInfoInput = new VerticalLayout();
        userInfoInput.setSizeFull();
        userInfoInput.setSpacing(true);
        contentPane.addComponent(userInfoInput);*/

        //Username Input
        usernameInputField = new TextField("Username:");
        usernameInputField.setSizeFull();
        usernameInputField.setImmediate(true);
        usernameInputField.setMaxLength(100);
        contentPane.addComponent(usernameInputField);

        //Name Input
        realnameInputField = new TextField("Realname:");
        realnameInputField.setSizeFull();
        realnameInputField.setImmediate(true);
        realnameInputField.setMaxLength(255);
        contentPane.addComponent(realnameInputField);

        //EMail Input
        emailInputField = new TextField("Email:");
        emailInputField.setSizeFull();
        emailInputField.setImmediate(true);
        emailInputField.setMaxLength(255);
        contentPane.addComponent(emailInputField);

        //PW 1st Input
        passwordFirstTimeInputField = new PasswordField("Password:");
        passwordFirstTimeInputField.setImmediate(true);
        passwordFirstTimeInputField.setMaxLength(255);
        contentPane.addComponent(passwordFirstTimeInputField);

        //PW 2nt Input
        passwordSecondTimeInputField = new PasswordField("Repeat password:");
        passwordSecondTimeInputField.setImmediate(true);
        passwordSecondTimeInputField.setMaxLength(255);
        contentPane.addComponent(passwordSecondTimeInputField);

        /*Button*/

        final HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setSizeFull();
        buttonBar.setSpacing(true);
        contentPane.addComponent(buttonBar);


        //Create New User Button
        final Button createNewUserButton = new Button("Create");
        createNewUserButton.addClickListener(event1 -> {
            int hoursPerMonth_int;
            try {
                hoursPerMonth_int = Integer.parseInt(hoursPerMonth.getValue());
            } catch (NumberFormatException e) {
                Notification.show("Error: Hours per month is not an integer!", Notification.Type.WARNING_MESSAGE);
                hoursPerMonth.selectAll();
                return;
            }
            if (!passwordFirstTimeInputField.getValue().equals(passwordSecondTimeInputField.getValue())) {
                Notification.show("Error: Passwords do not match!", Notification.Type.WARNING_MESSAGE);
                passwordFirstTimeInputField.selectAll();
                return;
            }
            try {
                CreateUserControl.getInstance().createUser((Role) selectRole.getValue(), usernameInputField.getValue(), emailInputField.getValue(), passwordFirstTimeInputField.getValue(), realnameInputField.getValue(), (User) selectSupervisor.getValue(), hoursPerMonth_int);
                LOGGER.info("New user \'{}\' created.", usernameInputField.getValue());
                clearAll();
            } catch (ChronoCommandException e) {
                Notification.show("Failed to create user: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
            }
        });
        buttonBar.addComponent(createNewUserButton);

        //Cancel Button goes back to the last View???
        final Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event1 -> {
            //TODO Fill me i want go back to my last View
            clearAll();
        });
        buttonBar.addComponent(cancelButton);
    }

    private void clearAll() {
        // TODO
    }
}
