package com.github.neunkasulle.chronocommand.view.forms;

import com.github.neunkasulle.chronocommand.control.MainControl;
import com.github.neunkasulle.chronocommand.control.UserManagementControl;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.User;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.List;

/**
 * Created by Ming-Samsung on 2016/1/31.
 */
public class AdminCtrlForm extends FormLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminCtrlForm.class);

    private VerticalLayout actionLayout = new VerticalLayout();
    //private Button btnToSupervisor = new Button("Zur Betreuer-Ansicht");
    private Button btnEditUser = new Button("Edit user");
    private Button btnToTimeSheets = new Button("Show timesheets");
    private Button btnCancel = new Button("Cancel");

    private VerticalLayout editUserLayout = new VerticalLayout();
    //private ComboBox selectRole;
    private ComboBox selectSupervisor;
    private TextField hoursPerMonth;
    private TextField usernameInputField;
    private TextField realnameInputField;
    private TextField emailInputField;
    private PasswordField passwordFirstTimeInputField;
    private PasswordField passwordSecondTimeInputField;
    private CheckBox disable;

    private Button.ClickListener postUserEditOperation;

    private User object;

    private BeanFieldGroup<User> formFieldBindings;

    public User getCurrentFormObject() {
        return this.object;
    }

    public BeanFieldGroup<User> getFormFieldBinding() {
        return this.formFieldBindings;
    }

    public AdminCtrlForm(final Button.ClickListener editUserOperation,
                         final Button.ClickListener postUserEditOperation,
                         final Button.ClickListener toTimeSheetsOperation,
                         final Button.ClickListener cancelOperation) {
        this.btnEditUser.addClickListener(editUserOperation);
        this.postUserEditOperation = postUserEditOperation;
        this.btnToTimeSheets.addClickListener(toTimeSheetsOperation);
        this.btnCancel.addClickListener(cancelOperation);
        actionLayout.setSpacing(true);
        actionLayout.addComponents(btnEditUser, btnToTimeSheets, btnCancel);

        editUserLayout.setVisible(false);

        addComponents(actionLayout, editUserLayout);
    }

    public void createEditUser() {
        clearUserEdit();
        editUserLayout.setVisible(true);
        actionLayout.setVisible(false);

        editUserLayout.setSpacing(true);

        selectSupervisor = new ComboBox("Supervisor:");
        try {
            List<User> supervisorList = UserManagementControl.getInstance().getUsersByRole(UserManagementControl.getInstance().getRoleByName(MainControl.ROLE_SUPERVISOR));
            selectSupervisor.addItems(supervisorList);
        } catch (ChronoCommandException e) {
            Notification.show("Failed to get supervisors: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
        }
        selectSupervisor.setVisible(false);
        selectSupervisor.select(object.getSupervisor());
        editUserLayout.addComponent(selectSupervisor);

        /*selectRole.addValueChangeListener(event1 -> {
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
        });*/

        /* Input Area */

        hoursPerMonth = new TextField("Hours per Month:");
        hoursPerMonth.setSizeFull();
        hoursPerMonth.setImmediate(true);
        hoursPerMonth.setVisible(false);
        hoursPerMonth.setValue(String.valueOf(object.getHoursPerMonth()));
        editUserLayout.addComponent(hoursPerMonth);

        if (object.isPermitted(Role.PERM_ADMINISTRATOR)) {
            selectSupervisor.setVisible(false);
            hoursPerMonth.setVisible(false);
        } else if (object.isPermitted(Role.PERM_SUPERVISOR)) {
            selectSupervisor.setVisible(false);
            hoursPerMonth.setVisible(true);
        } else {
            selectSupervisor.setVisible(true);
            hoursPerMonth.setVisible(true);
        }

        //Username Input
        usernameInputField = new TextField("Username:");
        usernameInputField.setSizeFull();
        usernameInputField.setImmediate(true);
        usernameInputField.setMaxLength(100);
        usernameInputField.setValue(object.getUsername());
        editUserLayout.addComponent(usernameInputField);

        //Name Input
        realnameInputField = new TextField("Realname:");
        realnameInputField.setSizeFull();
        realnameInputField.setImmediate(true);
        realnameInputField.setMaxLength(255);
        realnameInputField.setValue(object.getRealname());
        editUserLayout.addComponent(realnameInputField);

        //EMail Input
        emailInputField = new TextField("Email:");
        emailInputField.setSizeFull();
        emailInputField.setImmediate(true);
        emailInputField.setMaxLength(255);
        emailInputField.setValue(object.getEmail());
        editUserLayout.addComponent(emailInputField);

        //PW 1st Input
        passwordFirstTimeInputField = new PasswordField("Password:");
        passwordFirstTimeInputField.setImmediate(true);
        passwordFirstTimeInputField.setMaxLength(255);
        editUserLayout.addComponent(passwordFirstTimeInputField);

        //PW 2nt Input
        passwordSecondTimeInputField = new PasswordField("Repeat password:");
        passwordSecondTimeInputField.setImmediate(true);
        passwordSecondTimeInputField.setMaxLength(255);
        editUserLayout.addComponent(passwordSecondTimeInputField);

        disable = new CheckBox("Disable user");
        disable.setImmediate(true);
        disable.setValue(object.isDisabled());
        disable.addValueChangeListener(event -> {
            if (disable.getValue()) {
                ConfirmDialog.show(getUI(), "Disable user?", "Are you sure you want to disable this user?", "Yes", "No", event1 -> {
                    if (event1.isCanceled()) {
                        disable.setValue(false);
                    }
                });
            }
        });
        editUserLayout.addComponent(disable);


        final HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setSizeFull();
        buttonBar.setSpacing(true);
        editUserLayout.addComponent(buttonBar);


        //Create New User Button
        final Button createNewUserButton = new Button("Save");
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
                UserManagementControl.getInstance().editUser(object, usernameInputField.getValue(), realnameInputField.getValue(),
                        emailInputField.getValue(), passwordFirstTimeInputField.getValue(), (User) selectSupervisor.getValue(), hoursPerMonth_int, disable.getValue());
                clearUserEdit();
                postUserEditOperation.buttonClick(event1);
            } catch (ChronoCommandException e) {
                LOGGER.warn("Failed to save user: " + e.getReason().toString(), e);
                Notification.show("Failed to save user: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
            }
        });
        buttonBar.addComponent(createNewUserButton);

        final Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event1 -> {
            clearUserEdit();
            postUserEditOperation.buttonClick(event1);
        });
        buttonBar.addComponent(cancelButton);
    }

    private void clearUserEdit() {
        editUserLayout.removeAllComponents();
        editUserLayout.setVisible(false);
        actionLayout.setVisible(true);
    }

    public void edit(final User object) {
        this.object = object;
        if (object != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(object, this);
            //toSupervisor.focus();
        }
        setVisible(object != null);
    }

}
