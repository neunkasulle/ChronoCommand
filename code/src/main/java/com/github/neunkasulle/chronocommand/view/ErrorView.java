package com.github.neunkasulle.chronocommand.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ErrorView extends VerticalLayout implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Label error = new Label("Failed to find requested site: " + event.getViewName());
        error.setStyleName(ValoTheme.LABEL_H1);
        addComponent(error);

        Button link = new Button("Please go to the login page");
        link.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        addComponent(link);
        link.addClickListener(event1 -> getUI().getNavigator().navigateTo(MainUI.LOGINVIEW));
    }
}
