package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.MainControl;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.ServiceDestroyEvent;
import com.vaadin.server.ServiceDestroyListener;
import com.vaadin.ui.*;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.github.neunkasulle.chronocommand.MyAppWidgetset")
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Logger logger = LoggerFactory.getLogger(MainUI.class);
        logger.info("Request context: {} service: {} path: {}", vaadinRequest.getContextPath(), vaadinRequest.getService().toString(), vaadinRequest.getPathInfo());

        String path = vaadinRequest.getContextPath();
        if ("/main".equals(path)) {
        } else {
        }

        final VerticalLayout layout = new VerticalLayout();
        
        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Click Me!!!");
        button.addClickListener( e -> {
            layout.addComponent(new Label("Thanks " + name.getValue() 
                    + ", it works!"));
            Notification.show("Wow", "You pressed!", Notification.Type.WARNING_MESSAGE);
        });
        
        layout.addComponents(name, button);
        layout.setMargin(true);
        layout.setSpacing(true);
        
        setContent(layout);
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
