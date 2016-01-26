package com.github.neunkasulle.chronocommand.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * Created by Janze on 20.01.2016.
 */
public class MainView extends HorizontalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Label label = new Label("You made it to the main page!");
        addComponent(label);
    }
}
