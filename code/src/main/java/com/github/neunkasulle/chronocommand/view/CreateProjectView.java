package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.TimeSheetControl;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.Arrays;

/**
 * Created by Janze on 20.01.2016.
 */
public class CreateProjectView extends BaseView {

    public void createPrejectClicked() {

    }

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {

        final VerticalLayout siteInfo = new VerticalLayout();
        siteInfo.setSizeFull();

        siteInfo.addComponent(new Label("Create new project:"));
        contentPane.addComponent(siteInfo);

        /* Input Area */

        final VerticalLayout projectInfoInput = new VerticalLayout();
        projectInfoInput.setSizeFull();
        projectInfoInput.setSpacing(true);
        contentPane.addComponent(projectInfoInput);

        //Project name Input
        final TextField projectNameInputFiled = new TextField();
        projectNameInputFiled.setSizeFull();
        projectNameInputFiled.setImmediate(true);
        projectNameInputFiled.setInputPrompt("Name of the new project");
        projectNameInputFiled.setMaxLength(100);
        contentPane.addComponent(projectNameInputFiled);

        /*Button*/

        final HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setSizeFull();
        buttonBar.setSpacing(true);
        contentPane.addComponent(buttonBar);


        //Create New Project Button
        final Button createNewProjectButton = new Button("Create");
        createNewProjectButton.addClickListener(e -> {
            try {
                TimeSheetControl.getInstance().createProject(projectNameInputFiled.getValue());
                Notification.show("Created", Notification.Type.HUMANIZED_MESSAGE);
                projectNameInputFiled.clear();
            } catch(ChronoCommandException ex) {
                Notification.show("Failed to save Project: " + ex.getReason().toString(), Notification.Type.WARNING_MESSAGE);
                projectNameInputFiled.focus();
            }
        });
        buttonBar.addComponent(createNewProjectButton);
    }
}
