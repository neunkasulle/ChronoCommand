package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.view.BaseView;
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
            //TODO Fill me
        });
        buttonBar.addComponent(createNewProjectButton);

        //Cancel Button goes back to the last View???
        final Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> {
            //TODO Fill me i want go back to my last View
        });
        buttonBar.addComponent(cancelButton);





    }
}
