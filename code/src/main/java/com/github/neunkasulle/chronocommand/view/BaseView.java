package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.Session;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Janze on 20.01.2016.
 */
public abstract class BaseView extends HorizontalLayout implements View {

    private enum RoleAction {
        PROLETARIER("Proletarier") {
            @Override
            public void fillRoleSpecificContent(final Layout extraPane) {
                /* Betreuer */

                final HorizontalLayout supervisorPane = new HorizontalLayout();
                supervisorPane.setSizeFull();
                extraPane.addComponent(supervisorPane);

                supervisorPane.addComponent(new Label("Betreuer"));
                supervisorPane.addComponent(new Label("Betreuer1"));

                 /* Other actions  */

                extraPane.addComponent(new Label("neue Zeiterfassung"));
                extraPane.addComponent(new Label("Stundenzettel abgeben"));
            }
        }, SUPERVISOR("Supervisor") {
            @Override
            public void fillRoleSpecificContent(final Layout extraPane) {
                //TODO: fill me!
            }
        }, ADMIN("Admin") {
            @Override
            public void fillRoleSpecificContent(final Layout extraPane) {
                //TODO: fill me!
            }
        };

        private final String name;

        RoleAction(final String name) {
            this.name = name;
        }

        public static RoleAction valueOf(final Role role) {
            for (final RoleAction roleAction : RoleAction.values()) {
                if(roleAction.name == role.getName()) {
                    return roleAction;
                }
            }
            throw new IllegalArgumentException("Unknown role: "+ role.getId() + " " + role.getName());
        }

        public abstract void fillRoleSpecificContent(final Layout extraPane);

    }

    private static final String CONTROL_PANEL_WIDTH = "300px";

    private Grid contactList = new Grid();

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.setSizeFull();
        this.setWidth(null);
        this.initControlPanel();

        /* add actial content content */

        final VerticalLayout contentPane = new VerticalLayout();
        contentPane.addStyleName("container");
        contentPane.setSpacing(true);
        this.addComponent(contentPane);

        this.enterTemplate(event, contentPane);
    }

    public BaseView(Session session) {

    }

    public BaseView() {

    }

    public Session getSessionID() {

        return null;
    }

    public void logoutClicked() {

    }

    public void menuClicked() {

    }

    public void inboxClicked() {

    }

    public void showErrorMessage(String errorCode) {

    }

    public void showErrorMessage() {

    }

    public void buttonClick(Button.ClickEvent event) {

    }

    public void init(VaadinRequest request) {

    }

    public boolean beforeViewChange(ViewChangeListener.ViewChangeEvent event) {

        return false;
    }

    public void afterViewChange(ViewChangeListener.ViewChangeEvent event) {

    }

    protected abstract void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane);

    private void initControlPanel() {

        final VerticalLayout controlPanelTemp = new VerticalLayout();
        this.addComponent(controlPanelTemp);

        controlPanelTemp.setId("controll-panel");
        controlPanelTemp.setWidth(null);

        final VerticalLayout controlPanel = new VerticalLayout();
        controlPanelTemp.addComponent(controlPanel);

        controlPanel.setId("controll-panel");
        controlPanel.addStyleName("container");
        controlPanel.setWidth(CONTROL_PANEL_WIDTH);

        /* User information */

        final HorizontalLayout userInfo = new HorizontalLayout();
        userInfo.setSizeFull();
        userInfo.addComponent(new Label("Musterman"));
        //role.getName();
        userInfo.addComponent(new Label("HIWI"));
        controlPanel.addComponent(userInfo);

        /* Action links */

        final HorizontalLayout naviBar = new HorizontalLayout();
        naviBar.setSizeFull();
        controlPanel.addComponent(naviBar);

        final Button homeButton = new Button(new ThemeResource("img/home4.png"));
        homeButton.addClickListener(e -> {
            getUI().getNavigator().navigateTo(MainUI.MAINVIEW);
        });
        homeButton.setStyleName(BaseTheme.BUTTON_LINK);
        naviBar.addComponent(homeButton);

        final Button messageButton = new Button(new ThemeResource("img/black218.png"));
        messageButton.addClickListener(e -> {
            getUI().getNavigator().navigateTo(MainUI.MESSAGEVIEW);
        });
        messageButton.setStyleName(BaseTheme.BUTTON_LINK);
        naviBar.addComponent(messageButton);

        final Button logoutButton = new Button(new ThemeResource("img/logout13.png"));
        logoutButton.addClickListener(e -> {
            //TODO: Is this correct?
            // /SecurityUtils.getSubject().logout();
            this.logoutClicked();
        });
        logoutButton.setStyleName(BaseTheme.BUTTON_LINK);
        naviBar.addComponent(logoutButton);

        /* Date picker */

        final InlineDateField calendar = new InlineDateField();
        calendar.setValue(new Date());
        calendar.setImmediate(true);
        calendar.setLocale(Locale.getDefault());
        calendar.setResolution(Resolution.MINUTE);
        calendar.addValueChangeListener(e -> Notification.show("Value changed:",
                String.valueOf(e.getProperty().getValue()),
                Notification.Type.TRAY_NOTIFICATION));
        controlPanel.addComponent(calendar);

        /* Combo box */

        // Creates a new combobox using an existing container
        final ComboBox letterSelection = new ComboBox(null, Arrays.asList("02.01.2016", "01.01.2016"));
        letterSelection.setInputPrompt("Bitte Stundenzettel auswählen");
        letterSelection.setNullSelectionAllowed(false);
        controlPanel.addComponent(letterSelection);

        final VerticalLayout extraContent = new VerticalLayout();
        controlPanelTemp.addComponent(extraContent);

        extraContent.addStyleName("container");
        extraContent.setWidth(CONTROL_PANEL_WIDTH);

        //TODO: DUmmy code, use real role here!
        RoleAction.valueOf(new Role("Proletarier")).fillRoleSpecificContent(extraContent);

    }


}
