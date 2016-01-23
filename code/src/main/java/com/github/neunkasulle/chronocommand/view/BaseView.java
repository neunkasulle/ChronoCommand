package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.model.Session;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;

/**
 * Created by Janze on 20.01.2016.
 */
public abstract class BaseView implements View {

    public BaseView(Session session) {

    }

    public BaseView() {

    }

    public Session getSessionID() {

        return null;
    }

    public void logoutClicked() {

    }

    public void menuClciked() {

    }

    public void inboxClicked() {

    }

    public void showErrorMessage(String errorCode) {

    }

    public void showErrorMessage() {

    }

    public void buttonClick(Button.ClickEvent event) {

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public void init(VaadinRequest request) {

    }

    public boolean beforeViewChange(ViewChangeListener.ViewChangeEvent event) {

        return false;
    }

    public void afterViewChange(ViewChangeListener.ViewChangeEvent event) {

    }

}