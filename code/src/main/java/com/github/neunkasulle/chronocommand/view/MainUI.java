package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.MainControl;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
public class MainUI extends UI implements ViewChangeListener {
    public static final String LOGINVIEW = "login";
    public static final String MAINVIEW = "main";
    public static final String CREATEUSERVIEW = "createuser";
    public static final String TIMESHEETVIEW = "timesheet";
    public static final String MESSAGEVIEW = "messages";
    public static final String SETTINGSVIEW = "settings";
    private Navigator navigator;
    private Label header;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Logger logger = LoggerFactory.getLogger(MainUI.class);
        logger.info("Request context: {} service: {} path: {}", vaadinRequest.getContextPath(), vaadinRequest.getService().toString(), vaadinRequest.getPathInfo());

        getPage().setTitle("ChronoCommand");

        VerticalLayout baseLayout = new VerticalLayout();
        setContent(baseLayout);

        header = new Label("You have a header!");
        header.setSizeUndefined();
        baseLayout.addComponent(header);
        baseLayout.setComponentAlignment(header, Alignment.TOP_CENTER);

        MainView mainView = new MainView();
        baseLayout.addComponent(mainView);
        baseLayout.setComponentAlignment(mainView, Alignment.TOP_CENTER);

        navigator = new Navigator(this, mainView);
        navigator.addViewChangeListener(this);
        navigator.addView(LOGINVIEW, new LoginView());
        navigator.addView(MAINVIEW, MainView.class);
        navigator.addView(CREATEUSERVIEW, CreateUserView.class);
        navigator.addView(TIMESHEETVIEW, TimeSheetView.class);
        navigator.addView(MESSAGEVIEW, MessageView.class);
        navigator.addView(SETTINGSVIEW, UserSettingsView.class);
        navigator.setErrorView(ErrorView.class);
        /*if (!SecurityUtils.getSubject().isAuthenticated()) {
            navigator.navigateTo(LOGINVIEW);
        }*/

        /*VerticalLayout content = new VerticalLayout();
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

    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        /*Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isRemembered() && LOGINVIEW.equals(event.getViewName())) {
            event.getNavigator().navigateTo(MAINVIEW);
            return false;
        }
        if (!currentUser.isRemembered() && !LOGINVIEW.equals(event.getViewName())) {
            event.getNavigator().navigateTo(LOGINVIEW);
            return false;
        }*/

        if ("".equals(event.getViewName())) {
            event.getNavigator().navigateTo(LOGINVIEW); // FIXME MAINVIEW
            return false;
        }

        return true;
    }

    @Override
    public void afterViewChange(ViewChangeEvent event) {
        Logger logger = LoggerFactory.getLogger(MainUI.class);
        Subject subject = SecurityUtils.getSubject();
        logger.info("Subject: {} remember {} authenticated {}", subject, subject.isRemembered(), subject.isAuthenticated());
        if (subject.isRemembered() || subject.isAuthenticated()) {
            header.setValue("Whoo, you're logged in!");
        } else {
            header.setValue("You're logged out.");
        }
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

            MainControl.getInstance().startup();
        }

        @Override
        public void serviceDestroy(ServiceDestroyEvent event) {
            MainControl.getInstance().shutdown();
        }
    }
}
