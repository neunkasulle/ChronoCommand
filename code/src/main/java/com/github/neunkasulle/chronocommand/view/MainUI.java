package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.LoginControl;
import com.github.neunkasulle.chronocommand.control.MainControl;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.ui.themes.ValoTheme;
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
    public static final String TIMERECORDVIEW = "timerecord";
    public static final String ADMINVIEW = "admin";
    public static final String CREATEUSERVIEW = "createuser";
    public static final String TIMESHEETVIEW = "timesheet";
    public static final String EMPLOYEEVIEW = "employee";
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

        header = new Label("ChronoCommand");
        header.setSizeUndefined();
        header.addStyleName(ValoTheme.LABEL_H1);
        baseLayout.addComponent(header);
        baseLayout.setComponentAlignment(header, Alignment.TOP_CENTER);

        TimeRecordView timeRecordView = new TimeRecordView();
        baseLayout.addComponent(timeRecordView);
        baseLayout.setComponentAlignment(timeRecordView, Alignment.TOP_CENTER);

        navigator = new Navigator(this, timeRecordView);
        navigator.addViewChangeListener(this);
        navigator.addView(LOGINVIEW, new LoginView());
        navigator.addView(EMPLOYEEVIEW, new SupervisorView());
        navigator.addView(ADMINVIEW, new AdminView());
        navigator.addView(TIMERECORDVIEW, TimeRecordView.class);
        navigator.addView(CREATEUSERVIEW, CreateUserView.class);
        navigator.addView(TIMESHEETVIEW, TimeSheetView.class);
        navigator.addView(SETTINGSVIEW, UserSettingsView.class);
        navigator.setErrorView(ErrorView.class);
        /*if (!LoginControl.getInstance().isLoggedIn()) {\
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
        if (LoginControl.getInstance().isLoggedIn() && LOGINVIEW.equals(event.getViewName())) {
            event.getNavigator().navigateTo(TIMERECORDVIEW);
            return false;
        }
        if (!LoginControl.getInstance().isLoggedIn() && !LOGINVIEW.equals(event.getViewName())) {
            event.getNavigator().navigateTo(LOGINVIEW);
            return false;
        }*/

        if ("".equals(event.getViewName())) {
            event.getNavigator().navigateTo(LOGINVIEW); // FIXME TIMERECORDVIEW
            return false;
        }

        return true;
    }

    @Override
    public void afterViewChange(ViewChangeEvent event) {
        if (LoginControl.getInstance().isLoggedIn()) {
            header.addStyleName(ValoTheme.LABEL_COLORED);
        } else {
            header.removeStyleName(ValoTheme.LABEL_COLORED);
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
