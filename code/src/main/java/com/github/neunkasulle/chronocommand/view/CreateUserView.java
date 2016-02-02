package com.github.neunkasulle.chronocommand.view;

import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.Arrays;

/**
 * Created by Janze on 20.01.2016.
 */
public class CreateUserView extends BaseView {

    public void createUserClicked() {

    }

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {

        final VerticalLayout siteInfo = new VerticalLayout();
        siteInfo.setSizeFull();

        siteInfo.addComponent(new Label("Neuen Account anlegen:"));
        contentPane.addComponent(siteInfo);

        //Combobox Area
        final HorizontalLayout selectProperty = new HorizontalLayout();
        selectProperty.setSizeFull();
        selectProperty.setSpacing(true);
        contentPane.addComponent(selectProperty);

        final ComboBox selectRole = new ComboBox(null, Arrays.asList("Supervisor", "Proletarier"));
        selectRole.setInputPrompt("Bitte Role auswählen");
        selectProperty.addComponent(selectRole);

        final ComboBox selectSupervisor = new ComboBox(null, Arrays.asList("Betreuer", "Proletarier"));
        selectSupervisor.setInputPrompt("Bitte Betreuer auswählen");
        selectProperty.addComponent(selectSupervisor);


        /* Input Area */

        //TODO @Wang Xiaoming 想办法把前3个Field拉伸
        final VerticalLayout userInfoInput = new VerticalLayout();
        userInfoInput.setSizeFull();
        userInfoInput.setSpacing(true);
        contentPane.addComponent(userInfoInput);

        //Username Input
        final TextField userameInputFiled = new TextField();
        userameInputFiled.setSizeFull();
        userameInputFiled.setImmediate(true);
        userameInputFiled.setInputPrompt("Username angeben");
        userameInputFiled.setMaxLength(100);
        contentPane.addComponent(userameInputFiled);

        //Name Input
        final TextField realnameInputField = new TextField();
        realnameInputField.setSizeFull();
        realnameInputField.setImmediate(true);
        realnameInputField.setInputPrompt("Vorname Name angeben");
        realnameInputField.setMaxLength(255);
        contentPane.addComponent(realnameInputField);

        //EMail Input
        final TextField emailInputField = new TextField();
        emailInputField.setSizeFull();
        emailInputField.setImmediate(true);
        emailInputField.setInputPrompt("EMail angeben");
        emailInputField.setMaxLength(255);
        contentPane.addComponent(emailInputField);

        //PW 1st Input
        final PasswordField passwordFirstTimeInputField = new PasswordField();
        passwordFirstTimeInputField.setImmediate(true);
        passwordFirstTimeInputField.setMaxLength(255);
        passwordFirstTimeInputField.setInputPrompt("********");
        contentPane.addComponent(passwordFirstTimeInputField);

        //PW 2nt Input
        final PasswordField passwordSecondTimeInputField = new PasswordField();
        passwordSecondTimeInputField.setImmediate(true);
        passwordSecondTimeInputField.setMaxLength(255);
        passwordSecondTimeInputField.setInputPrompt("********");
        contentPane.addComponent(passwordSecondTimeInputField);

        /*Button*/

        final HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setSizeFull();
        buttonBar.setSpacing(true);
        contentPane.addComponent(buttonBar);


        //Create New User Button
        final Button createNewUserButton = new Button("Create");
        createNewUserButton.addClickListener(e -> {
            //TODO Fill me
        });
        buttonBar.addComponent(createNewUserButton);

        //Cancel Button goes back to the last View???
        final Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> {
            //TODO Fill me i want go back to my last View
        });
        buttonBar.addComponent(cancelButton);





    }
}
