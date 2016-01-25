package com.github.neunkasulle.chronocommand.view;

import com.ejt.vaadin.loginform.*;
import com.ejt.vaadin.loginform.LoginForm;
import com.github.neunkasulle.chronocommand.control.MainControl;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.ui.*;

/**
 *
 */
@Theme("chronocommand")
@Widgetset("com.github.neunkasulle.chronocommand.ChronoCommandWidgetset")
public class MainUI extends UI {
    public static final String LOGINVIEW = "login";
    public static final String MAINVIEW = "main";
    Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Logger logger = LoggerFactory.getLogger(MainUI.class);
        logger.info("Request context: {} service: {} path: {}", vaadinRequest.getContextPath(), vaadinRequest.getService().toString(), vaadinRequest.getPathInfo());

        getPage().setTitle("ChronoCommand");

        navigator = new Navigator(this, this);
        navigator.addView(LOGINVIEW, new LoginView());
        navigator.addView(MAINVIEW, MainView.class);
        navigator.navigateTo(LOGINVIEW);

        /*DefaultVerticalLoginForm loginForm = new DefaultVerticalLoginForm();
        loginForm.addLoginListener(loginEvent -> {
            Logger loginLogger = LoggerFactory.getLogger(LoginView.class);
            loginLogger.info("User: {} Password: {}", loginEvent.getUserName(), loginEvent.getPassword());
        });
        setContent(loginForm);*/

        /*final VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        setContent(content);

        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Click Me!!!");
        button.addClickListener( e -> {
            content.addComponent(new Label("Thanks " + name.getValue()
                    + ", it works!"));
            Notification.show("Wow", "You pressed!", Notification.Type.WARNING_MESSAGE);
        });
        
        content.addComponents(name, button);
        content.setMargin(true);
        content.setSpacing(true);*/
    }

    @WebServlet(urlPatterns = "/*", name = "ChronoCommandServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class ChronoCommandServlet extends VaadinServlet implements ServiceDestroyListener {
        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();
            getService().addServiceDestroyListener(this);

            Logger logger = LoggerFactory.getLogger(MainUI.class);
            logger.info("Starting up ChronoCommand servlet");

            //MainControl.getInstance().startup();
        }

        @Override
        public void serviceDestroy(ServiceDestroyEvent event) {
            //MainControl.getInstance().shutdown();
        }
    }
}
