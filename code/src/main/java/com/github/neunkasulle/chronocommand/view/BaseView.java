package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.LoginControl;
import com.github.neunkasulle.chronocommand.control.TimeSheetControl;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.TimeSheet;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Janze on 20.01.2016.
 */
public abstract class BaseView extends HorizontalLayout implements View {
    protected ComboBox timeRecordSelection;

    //It would not work wenn the supervisor himself also does SZ

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

                final Button newTimeRecordButton = new Button("Neue Zeiterfassung");
                newTimeRecordButton.addClickListener(e -> {
                    extraPane.getUI().getNavigator().navigateTo(MainUI.TIMERECORDVIEW);
                });
                extraPane.addComponent(newTimeRecordButton);

                final Button submitTimeRecordButton = new Button("Stundenzetten abschicken");
                submitTimeRecordButton.addClickListener(e -> {
                    //TODO: Implement submission
                });
                extraPane.addComponent(submitTimeRecordButton);
            }
        }, SUPERVISOR("Supervisor") {
            @Override
            public void fillRoleSpecificContent(final Layout extraPane) {
                //TODO: fill me!
                /* Betreuer */

                final HorizontalLayout supervisorPane = new HorizontalLayout();
                supervisorPane.setSizeFull();
                extraPane.addComponent(supervisorPane);

                supervisorPane.addComponent(new Label("Betreuer"));
                supervisorPane.addComponent(new Label("Betreuer1"));

                final Button newTimeRecordButton = new Button("Neue Zeiterfassung");
                newTimeRecordButton.addClickListener(e -> {
                    extraPane.getUI().getNavigator().navigateTo(MainUI.TIMERECORDVIEW);
                });
                extraPane.addComponent(newTimeRecordButton);
                newTimeRecordButton.setSizeFull();

                final Button submitTimeRecordButton = new Button("Stundenzetten abschicken");
                submitTimeRecordButton.addClickListener(e -> {
                    //TODO: Implement submission
                });
                extraPane.addComponent(submitTimeRecordButton);
                submitTimeRecordButton.setSizeFull();


                final Button listOfMyProletarierButton = new Button("Meine HIWIs anzeigen");
                listOfMyProletarierButton.setSizeFull();
                listOfMyProletarierButton.addClickListener(e -> {
                    //TODO: Get the list of all his HIWIs and show it in the Grid.
                });
                extraPane.addComponent(listOfMyProletarierButton);
                listOfMyProletarierButton.setSizeFull();

            }
        }, ADMIN("Admin") {
            @Override
            public void fillRoleSpecificContent(final Layout extraPane) {
                //TODO: fill me!
                final HorizontalLayout adminToSupervisorPane = new HorizontalLayout();
                adminToSupervisorPane.setSizeFull();
                extraPane.addComponent(adminToSupervisorPane);
                adminToSupervisorPane.addComponent(new Label("Zu Betreuersicht wechseln:"));

                final ComboBox supervisorSelection = new ComboBox(null, Arrays.asList("Betreuer1", "Betreuer2"));
                supervisorSelection.setSizeFull();
                supervisorSelection.addValueChangeListener(e -> {
                    //TODO : Do sonething usefoll here
                });
                supervisorSelection.setInputPrompt("Betreuer auswählen");
                extraPane.addComponent(supervisorSelection);


                final HorizontalLayout accountManagementPane = new HorizontalLayout();
                accountManagementPane.setSizeFull();
                extraPane.addComponent(accountManagementPane);
                accountManagementPane.addComponent(new Label("Account Verwalten:"));

                final ComboBox accountManagementSelection = new ComboBox(null, Arrays.asList("Neuen Account zulegen", "Account löschen", "Account bearbeiten"));
                accountManagementSelection.setSizeFull();
                accountManagementSelection.setInputPrompt("Operation auswählen");
                extraPane.addComponent(accountManagementSelection);

                final Button listOfAllProletarierButton = new Button("Alle HIWIs anzeigen");
                listOfAllProletarierButton.setSizeFull();
                listOfAllProletarierButton.addClickListener(e -> {
                    //TODO: Get the list of all HIWIs and show it in the Grid.
                });
                extraPane.addComponent(listOfAllProletarierButton);
                listOfAllProletarierButton.setSizeFull();

                final Button listOfAllSupervisorButton = new Button("Alle Betreuer anzeigen");
                listOfAllSupervisorButton.setSizeFull();
                listOfAllSupervisorButton.addClickListener(e -> {
                    //TODO: Get the list of all HIWIs and show it in the Grid.
                });
                extraPane.addComponent(listOfAllSupervisorButton);
                listOfAllSupervisorButton.setSizeFull();

            }

        };

        private final String name;

        RoleAction(final String name) {
            this.name = name;
        }

        public static RoleAction valueOf(final Role role) {
            for (final RoleAction roleAction : RoleAction.values()) {
                if (roleAction.name.equals(role.getName())) {
                    return roleAction;
                }
            }
            throw new IllegalArgumentException("Unknown role: " + role.getId() + " " + role.getName());
        }

        public abstract void fillRoleSpecificContent(final Layout extraPane);

    }

    private static final String CONTROL_PANEL_WIDTH = "300px";

    //Hier the ContactList ist from Vaadin Demo Adress book
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

    public void logoutClicked(Button.ClickEvent clickEvent) {
        LoginControl.getInstance().logout();
        getUI().getNavigator().navigateTo(MainUI.LOGINVIEW);
        //getUI().getPage().reload();
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
        controlPanel.addStyleName("container");
        controlPanel.setSpacing(true);
        controlPanelTemp.addComponent(controlPanel);

        controlPanel.setId("controll-panel");
        controlPanel.addStyleName("container");
        controlPanel.setWidth(CONTROL_PANEL_WIDTH);

        /* User information */

        final HorizontalLayout userInfo = new HorizontalLayout();
        userInfo.setSizeFull();

        String nameOfUser;
        try {
            nameOfUser = LoginControl.getInstance().getCurrentUser().getRealname();
        } catch (ChronoCommandException e) {
            nameOfUser = "Not logged in!";
        }
        final Button userSettingsButton = new Button(nameOfUser);
        userSettingsButton.setStyleName(BaseTheme.BUTTON_LINK);
        userSettingsButton.addClickListener(e -> {
            getUI().getNavigator().navigateTo(MainUI.SETTINGSVIEW);
        });
        userInfo.addComponent(userSettingsButton);

        //TODO: use role.getName() or something like that here
        String roleOfUser;
        if (SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)) {
            roleOfUser = "Administrator";
        } else if (SecurityUtils.getSubject().isPermitted(Role.PERM_SUPERVISOR)) {
            roleOfUser = "Supervisor";
        } else if (SecurityUtils.getSubject().isPermitted(Role.PERM_PROLETARIER)) {
            roleOfUser = "HIWI";
        } else {
            roleOfUser = "No role!";
        }
        userInfo.addComponent(new Label(roleOfUser));
        controlPanel.addComponent(userInfo);

        /* Action links */

        final HorizontalLayout naviBar = new HorizontalLayout();
        naviBar.setSizeFull();
        controlPanel.addComponent(naviBar);

        final Button homeButton = new Button(new ThemeResource("img/home4.png"));
        homeButton.addClickListener(e -> {
            getUI().getNavigator().navigateTo(MainUI.TIMERECORDVIEW);
        });
        homeButton.setStyleName(BaseTheme.BUTTON_LINK);
        naviBar.addComponent(homeButton);


        final Button logoutButton = new Button(new ThemeResource("img/logout13.png"));
        logoutButton.addClickListener(this::logoutClicked);
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
        timeRecordSelection = new ComboBox("Bitte Stundenzettel auswählen");
        timeRecordSelection.setSizeFull();
        timeRecordSelection.setNullSelectionAllowed(false);
        controlPanel.addComponent(timeRecordSelection);
        refreshTimeSheetList();

        final VerticalLayout extraContent = new VerticalLayout();
        extraContent.addStyleName("container");
        extraContent.setSpacing(true);
        controlPanelTemp.addComponent(extraContent);

        extraContent.addStyleName("container");
        extraContent.setWidth(CONTROL_PANEL_WIDTH);

        //TODO: Dummy code, use real role here!
        RoleAction.valueOf(new Role("Admin")).fillRoleSpecificContent(extraContent);

        /*for (final Role role : user.getRoles()) {
            RoleAction.valueOf(role).fillRoleSpecificContent(extraContent);
        }*/
        //RoleAction.valueOf(new Role("Supervisor")).fillRoleSpecificContent(extraContent);

    }

    protected void refreshTimeSheetList() {
        try {
            Object currentSelection = timeRecordSelection.getValue();
            timeRecordSelection.removeAllItems();
            List<TimeSheet> timeSheetList = TimeSheetControl.getInstance().getTimeSheetsFromUser(LoginControl.getInstance().getCurrentUser());
            timeRecordSelection.addItems(timeSheetList);
            if (currentSelection == null && timeSheetList.size() > 0) {
                currentSelection = timeSheetList.get(timeSheetList.size() - 1);
            }
            timeRecordSelection.setValue(currentSelection);
        } catch(ChronoCommandException e) {
            // TODO error
        }
    }
}
