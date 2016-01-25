package com.github.neunkasulle.chronocommand.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * Created by Janze on 20.01.2016.
 */
public class MainView extends HorizontalLayout implements View, ViewChangeListener {

    public MainView() {
        Label label = new Label("You made it to the main page!");
        addComponent(label);
    }

    public void init(VaadinRequest request) {

    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

    public boolean beforeViewChange(ViewChangeListener.ViewChangeEvent event) {

        return true;
    }

    public void afterViewChange(ViewChangeListener.ViewChangeEvent event) {

    }
}
