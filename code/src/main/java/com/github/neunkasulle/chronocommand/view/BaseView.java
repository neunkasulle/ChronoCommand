package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.LoginControl;
import com.github.neunkasulle.chronocommand.control.TimeSheetControl;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.TimeSheet;
import com.github.neunkasulle.chronocommand.model.User;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

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
            public void fillRoleSpecificContent(final Layout extraPane, ComboBox timeRecordSelection) {
                /* Betreuer */

                final HorizontalLayout supervisorPane = new HorizontalLayout();
                supervisorPane.setSizeFull();
                extraPane.addComponent(supervisorPane);

                try {
                    User supervisor = LoginControl.getInstance().getCurrentUser().getSupervisor();
                    if (supervisor != null) {
                        supervisorPane.addComponent(new Label("Supervisor: " + supervisor.getRealname()));
                    }
                } catch(ChronoCommandException e) {
                    Notification.show("Failed to get supervisor: " + e.getReason().toString(), Notification.Type.WARNING_MESSAGE);
                }

                 /* Other actions  */

                final Button newTimeRecordButton = new Button("New time record");
                newTimeRecordButton.addClickListener(event -> {
                    extraPane.getUI().getNavigator().navigateTo(MainUI.NEWTIMERECORDVIEW);
                });
                extraPane.addComponent(newTimeRecordButton);

                final Button submitTimeRecordButton = new Button("Submit Timesheet");
                submitTimeRecordButton.addClickListener(event -> {
                    //TODO: Implement submission
                });
                extraPane.addComponent(submitTimeRecordButton);
            }
        }, SUPERVISOR("Supervisor") {
            @Override
            public void fillRoleSpecificContent(final Layout extraPane, ComboBox timeRecordSelection) {
                //TODO: fill me!
                /* Betreuer */

                final Button newTimeRecordButton = new Button("New time record");
                newTimeRecordButton.addClickListener(event -> {
                    extraPane.getUI().getNavigator().navigateTo(MainUI.NEWTIMERECORDVIEW);
                });
                extraPane.addComponent(newTimeRecordButton);
                newTimeRecordButton.setSizeFull();

                final Button submitTimeRecordButton = new Button("Submit Timesheet");
                submitTimeRecordButton.addClickListener(event -> {
                    try {
                        TimeSheetControl.getInstance().lockTimeSheet((TimeSheet) timeRecordSelection.getValue(), LoginControl.getInstance().getCurrentUser());
                    } catch(ChronoCommandException e) {
                        Notification.show("Failed to lock timesheet: " + e.getReason().toString(), Notification.Type.WARNING_MESSAGE);
                    }
                });
                extraPane.addComponent(submitTimeRecordButton);
                submitTimeRecordButton.setSizeFull();


                final Button listOfMyProletarierButton = new Button("Show Supervised HIWIs");
                listOfMyProletarierButton.setSizeFull();
                listOfMyProletarierButton.addClickListener(e -> {
                    extraPane.getUI().getNavigator().navigateTo(MainUI.SUPERVISORVIEW);
                    //TODO: Get the list of all his HIWIs and show it in the Grid.
                });
                extraPane.addComponent(listOfMyProletarierButton);
                listOfMyProletarierButton.setSizeFull();

            }
        }, ADMINISTRATOR("Administrator") {
            @Override
            public void fillRoleSpecificContent(final Layout extraPane, ComboBox timeRecordSelection) {
                //TODO: fill me!
                /*final HorizontalLayout adminToSupervisorPane = new HorizontalLayout();
                adminToSupervisorPane.setSizeFull();
                extraPane.addComponent(adminToSupervisorPane);
                adminToSupervisorPane.addComponent(new Label("Zu Betreuersicht wechseln:"));

                final ComboBox supervisorSelection = new ComboBox(null, Arrays.asList("Betreuer1", "Betreuer2"));
                supervisorSelection.setSizeFull();
                supervisorSelection.addValueChangeListener(event -> {
                    //TODO : Do sonething usefoll here
                });
                supervisorSelection.setInputPrompt("Betreuer auswählen");
                extraPane.addComponent(supervisorSelection);*/


                /*final HorizontalLayout accountManagementPane = new HorizontalLayout();
                accountManagementPane.setSizeFull();
                extraPane.addComponent(accountManagementPane);
                accountManagementPane.addComponent(new Label("Account Verwalten:"));

                final ComboBox accountManagementSelection = new ComboBox(null, Arrays.asList("Neuen Account zulegen", "Account löschen", "Account bearbeiten"));
                accountManagementSelection.setSizeFull();
                accountManagementSelection.setInputPrompt("Operation auswählen");
                extraPane.addComponent(accountManagementSelection);*/

                final Button listOfAllProletarierButton = new Button("Show all users");
                listOfAllProletarierButton.setSizeFull();
                extraPane.addComponent(listOfAllProletarierButton);
                listOfAllProletarierButton.addClickListener(event -> extraPane.getUI().getNavigator().navigateTo(MainUI.ADMINVIEW));


                final Button createNewAccount = new Button("Create new account");
                createNewAccount.setSizeFull();
                createNewAccount.addClickListener(event -> extraPane.getUI().getNavigator().navigateTo(MainUI.CREATEUSERVIEW));
                extraPane.addComponent(createNewAccount);

                final Button createNewProjectButton = new Button("Create new project");
                createNewProjectButton.setSizeFull();
                createNewProjectButton.addClickListener(event -> extraPane.getUI().getNavigator().navigateTo(MainUI.CREATEPROJECTVIEW));
                extraPane.addComponent(createNewProjectButton);

                /*final Button editAccount = new Button("Edit account");
                editAccount.setSizeFull();
                editAccount.addClickListener(event -> {
                    //TODO
                });
                extraPane.addComponent(editAccount);

                final Button deleteAccount = new Button("Delete account");
                deleteAccount.setSizeFull();
                deleteAccount.addClickListener(event -> {
                    //TODO
                });
                extraPane.addComponent(deleteAccount);*/

                /*final Button listOfAllSupervisorButton = new Button("Show all supervisors");
                listOfAllSupervisorButton.setSizeFull();
                listOfAllSupervisorButton.addClickListener(event -> {
                    //TODO: Get the list of all HIWIs and show it in the Grid.
                });
                extraPane.addComponent(listOfAllSupervisorButton);*/

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

        public abstract void fillRoleSpecificContent(final Layout extraPane, ComboBox timeRecordSelection);

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

        if (SecurityUtils.getSubject().isPermitted(Role.PERM_PROLETARIER)) {
            /* Date picker */

            /* TODO has no functionality right now
            final InlineDateField calendar = new InlineDateField();
            calendar.setValue(new Date());
            calendar.setImmediate(true);
            calendar.setLocale(Locale.getDefault());
            calendar.setResolution(Resolution.MINUTE);
            calendar.addValueChangeListener(e -> Notification.show("Value changed:",
                    String.valueOf(e.getProperty().getValue()),
                    Notification.Type.TRAY_NOTIFICATION));
            controlPanel.addComponent(calendar);*/

            /* Combo box */

            // Creates a new combobox using an existing container
            timeRecordSelection = new ComboBox("Please select a timesheet");
            timeRecordSelection.setSizeFull();
            timeRecordSelection.setNullSelectionAllowed(false);
            controlPanel.addComponent(timeRecordSelection);
            refreshTimeSheetList();
        }

        final VerticalLayout extraContent = new VerticalLayout();
        extraContent.addStyleName("container");
        extraContent.setSpacing(true);
        controlPanelTemp.addComponent(extraContent);

        extraContent.addStyleName("container");
        extraContent.setWidth(CONTROL_PANEL_WIDTH);

        try {
            Role role = LoginControl.getInstance().getCurrentUser().getPrimaryRole();
            RoleAction.valueOf(role.getName()).fillRoleSpecificContent(extraContent, timeRecordSelection);
        } catch (ChronoCommandException e) {
            Notification.show("Failed: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
        }

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
            if (currentSelection == null && !timeSheetList.isEmpty()) {
                currentSelection = timeSheetList.get(timeSheetList.size() - 1);
            }
            timeRecordSelection.setValue(currentSelection);
        } catch(ChronoCommandException e) {
            // TODO error
        }
    }
}
