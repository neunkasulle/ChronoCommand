package com.github.neunkasulle.chronocommand.view;

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

        //TODO hier username oder realname vom entsprechenden User holen.
        siteInfo.addComponent(new Label("Account von User" + " Dummy " + "bearbeiten:"));
        contentPane.addComponent(siteInfo);

        /* Edit Area */

        final VerticalLayout userInfoEdit = new VerticalLayout();
        userInfoEdit.setSizeFull();
        userInfoEdit.setSpacing(true);
        contentPane.addComponent(userInfoEdit);

        //Edit username.
        final TextField userameEditInputField = new TextField();
        userameEditInputField.setSizeFull();
        userameEditInputField.setImmediate(true);
        userameEditInputField.setInputPrompt("Neuen Username angeben");
        userameEditInputField.setMaxLength(100);
        contentPane.addComponent(userameEditInputField);

        //Name Edit
        final TextField realnameEditInputField = new TextField();
        realnameEditInputField.setSizeFull();
        realnameEditInputField.setImmediate(true);
        realnameEditInputField.setInputPrompt("Neuen Vorname Name angeben");
        realnameEditInputField.setMaxLength(255);
        contentPane.addComponent(realnameEditInputField);

        //EMail Input
        final TextField emailEditInputField = new TextField();
        emailEditInputField.setSizeFull();
        emailEditInputField.setImmediate(true);
        emailEditInputField.setInputPrompt("Neue EMail angeben");
        emailEditInputField.setMaxLength(255);
        contentPane.addComponent(emailEditInputField);

        //PW 1st Input
        final PasswordField passwordFirstTimeEditInputField = new PasswordField();
        passwordFirstTimeEditInputField.setImmediate(true);
        passwordFirstTimeEditInputField.setMaxLength(255);
        passwordFirstTimeEditInputField.setInputPrompt("********");
        contentPane.addComponent(passwordFirstTimeEditInputField);

        //PW 2nt Input
        final PasswordField passwordSecondTimeEditInputField = new PasswordField();
        passwordSecondTimeEditInputField.setImmediate(true);
        passwordSecondTimeEditInputField.setMaxLength(255);
        passwordSecondTimeEditInputField.setInputPrompt("********");
        contentPane.addComponent(passwordSecondTimeEditInputField);

        /*Button*/

        final HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setSizeFull();
        buttonBar.setSpacing(true);
        contentPane.addComponent(buttonBar);


        //Create New User Button
        final Button editUserButton = new Button("Änderungen übernehmen");
        editUserButton.addClickListener(e -> {
            //TODO Fill me
        });
        buttonBar.addComponent(editUserButton);

        //Cancel Button goes back to the last View???
        final Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> {
            //TODO Fill me i want go back to my last View
        });
        buttonBar.addComponent(cancelButton);


    }
}
