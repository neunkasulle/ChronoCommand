package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.LoginControl;
import com.github.neunkasulle.chronocommand.control.UserManagementControl;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.User;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.shiro.SecurityUtils;

/**
 * Created by Janze on 20.01.2016.
 */
public class UserSettingsView extends BaseView {

    final Label result = new Label();

    public void filtersChanged() {

    }

    public boolean saveClicked() {

        return false;
    }

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {

        final VerticalLayout siteInfo = new VerticalLayout();
        siteInfo.setSizeFull();

        User user;
        try {
            user = LoginControl.getInstance().getCurrentUser();
        } catch(ChronoCommandException e) {
            Notification.show("Failed to get current user. Please try to logout and login again.", Notification.Type.ERROR_MESSAGE);
            return;
        }

        siteInfo.addComponent(new Label("Edit account from " + user.getRealname()));
        contentPane.addComponent(siteInfo);

        /* Edit Area */

        final VerticalLayout userInfoEdit = new VerticalLayout();
        userInfoEdit.setSizeFull();
        userInfoEdit.setSpacing(true);
        contentPane.addComponent(userInfoEdit);

        //Edit username.
        final TextField usernameEditInputField = new TextField("Username:");
        usernameEditInputField.setSizeFull();
        usernameEditInputField.setImmediate(true);
        usernameEditInputField.setValue(user.getUsername());
        usernameEditInputField.setMaxLength(100);
        contentPane.addComponent(usernameEditInputField);

        //Name Edit
        final TextField realnameEditInputField = new TextField("Full name:");
        realnameEditInputField.setSizeFull();
        realnameEditInputField.setImmediate(true);
        realnameEditInputField.setValue(user.getRealname());
        realnameEditInputField.setMaxLength(255);
        contentPane.addComponent(realnameEditInputField);

        //EMail Input
        final TextField emailEditInputField = new TextField("Email:");
        emailEditInputField.setSizeFull();
        emailEditInputField.setImmediate(true);
        emailEditInputField.setValue(user.getEmail());
        emailEditInputField.setMaxLength(255);
        contentPane.addComponent(emailEditInputField);

        //PW 1st Input
        final PasswordField passwordFirstTimeEditInputField = new PasswordField("New password");
        passwordFirstTimeEditInputField.setImmediate(true);
        passwordFirstTimeEditInputField.setMaxLength(255);
        passwordFirstTimeEditInputField.setInputPrompt("");
        contentPane.addComponent(passwordFirstTimeEditInputField);

        //PW 2nt Input
        final PasswordField passwordSecondTimeEditInputField = new PasswordField("Repeat new password");
        passwordSecondTimeEditInputField.setImmediate(true);
        passwordSecondTimeEditInputField.setMaxLength(255);
        passwordSecondTimeEditInputField.setInputPrompt("");
        contentPane.addComponent(passwordSecondTimeEditInputField);

        /*Button*/

        final HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setSizeFull();
        buttonBar.setSpacing(true);
        contentPane.addComponent(buttonBar);


        //Create New User Button
        final Button editUserButton = new Button("Apply");
        editUserButton.addClickListener(event1 -> {
            result.setVisible(true);
            result.addStyleName(ValoTheme.LABEL_FAILURE);
            result.setValue("Failed to save");
            if (!passwordFirstTimeEditInputField.getValue().equals(passwordSecondTimeEditInputField.getValue())) {
                Notification.show("Password fields do not match", Notification.Type.WARNING_MESSAGE);
                return;
            }

            try {
                UserManagementControl.getInstance().editUser(user, usernameEditInputField.getValue(), realnameEditInputField.getValue(),
                        emailEditInputField.getValue(), passwordFirstTimeEditInputField.getValue(), user.getSupervisor(), user.getHoursPerMonth(), user.isDisabled());
                result.addStyleName(ValoTheme.LABEL_SUCCESS);
                result.setValue("Saved");
            } catch(ChronoCommandException e) {
                Notification.show("Failed to save settings: " + e.getReason().toString(), Notification.Type.WARNING_MESSAGE);
            }
        });
        buttonBar.addComponent(editUserButton);

        final Button cancelButton = new Button("Cancel");
        if (SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)) {
            cancelButton.addClickListener(e -> getUI().getNavigator().navigateTo(MainUI.ADMINVIEW));
        } else {
            cancelButton.addClickListener(e -> getUI().getNavigator().navigateTo(MainUI.TIMERECORDVIEW));
        }
        buttonBar.addComponent(cancelButton);

        result.setVisible(false);
        contentPane.addComponent(result);
    }
}
