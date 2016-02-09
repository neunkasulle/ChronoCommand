package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.LoginControl;
import com.github.neunkasulle.chronocommand.control.UserManagementControl;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.User;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

/**
 * Created by Janze on 20.01.2016.
 */
public class UserSettingsView extends BaseView {

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
            Notification.show("Failed got get current user. Please try to logout an login again.", Notification.Type.ERROR_MESSAGE);
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
            if (!passwordFirstTimeEditInputField.getValue().equals(passwordSecondTimeEditInputField.getValue())) {
                Notification.show("Password fields do not match", Notification.Type.WARNING_MESSAGE);
                return;
            }

            try {
                UserManagementControl.getInstance().editUser(user, usernameEditInputField.getValue(), realnameEditInputField.getValue(),
                        emailEditInputField.getValue(), passwordFirstTimeEditInputField.getValue());
            } catch(ChronoCommandException e) {
                Notification.show("Failed to save settings: " + e.getReason().toString(), Notification.Type.WARNING_MESSAGE);
            }
        });
        buttonBar.addComponent(editUserButton);

        //Cancel Button goes back to the last View???
        final Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> {
            Notification.show("TODO: what to do now?", Notification.Type.WARNING_MESSAGE);
            //TODO Fill me i want go back to my last View
        });
        buttonBar.addComponent(cancelButton);


    }
}
