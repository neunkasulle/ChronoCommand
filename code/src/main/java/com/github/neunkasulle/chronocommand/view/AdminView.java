package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.UserManagementControl;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.view.forms.AdminCtrlForm;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.List;
import java.util.Set;

/**
 * Created by Janze on 20.01.2016.
 */
public class AdminView extends BaseView {

    private BeanItemContainer<User> beanItemContainer = new BeanItemContainer<>(User.class);

    private final Grid recordList = new Grid();

    private final AdminCtrlForm form = new AdminCtrlForm(e -> {
        getUI().getNavigator().navigateTo(MainUI.SUPERVISORVIEW);
    }, e -> {
        getUI().getNavigator().navigateTo(MainUI.SETTINGSVIEW);
    }, e -> {
        ConfirmDialog.show(getUI(), "Möchten sie den Benutzer wirklich löschen?",
                dialog -> {
                    if (dialog.isConfirmed()) {
                        // Confirmed to continue TODO
                    } else {
                        recordList.select(null);
                        refreshContacts();
                    }
                }
        );
    }, e -> {
        getUI().getNavigator().navigateTo(MainUI.TIMESHEETVIEW);
    }, e -> {

    }, e -> {
        this.recordList.select(null);
        refreshContacts();
    });

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {

        final Label header = new Label("Globale Benutzerverwaltung");
        header.setId("page-header");
        header.setSizeFull();
        contentPane.addComponent(header);

        final TextField filter = new TextField();
        contentPane.addComponent(filter);
        filter.setSizeFull();

        /* Form & table */

        final HorizontalLayout formContent = new HorizontalLayout();
        formContent.setSizeFull();
        contentPane.addComponent(formContent);

        /* Actual table */

        formContent.addComponent(recordList);
        beanItemContainer.addNestedContainerProperty("supervisor.realname");
        final GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(beanItemContainer);
        recordList.setContainerDataSource(gpcontainer);
        recordList.setSelectionMode(Grid.SelectionMode.SINGLE);
        recordList.addSelectionListener(e
                -> form.edit((User) recordList.getSelectedRow()));
        recordList.setSizeFull();

        //Add generated columns
        gpcontainer.addGeneratedProperty("roleSumary", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(final Item item, final Object itemId,
                                   final Object propertyId) {
                @SuppressWarnings("unchecked")
                final Set<Role> roles = (Set<Role>)
                        item.getItemProperty("roles").getValue();
                final StringBuilder ruleStr = new StringBuilder();

                for (final Role role : roles) {
                    ruleStr.append(role.getName());
                    ruleStr.append(" ");
                }

                return ruleStr.toString();
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        //Remove unused columns
        recordList.removeColumn("id");
        recordList.removeColumn("password");
        recordList.removeColumn("roles");
        recordList.removeColumn("mailFlag");
        recordList.removeColumn("disabled");
        recordList.removeColumn("supervisor");

        recordList.setColumnOrder("username", "realname", "supervisor.realname", "email", "roleSumary", "hoursPerMonth");
        recordList.getDefaultHeaderRow().getCell("username").setHtml("Benutzername");
        recordList.getDefaultHeaderRow().getCell("realname").setHtml("Name");
        recordList.getDefaultHeaderRow().getCell("supervisor.realname").setHtml("Betreuer");
        recordList.getDefaultHeaderRow().getCell("email").setHtml("E-Mail");
        recordList.getDefaultHeaderRow().getCell("roleSumary").setHtml("Rollen");
        recordList.getDefaultHeaderRow().getCell("hoursPerMonth").setHtml("Stunden / Woche");

        // The action form

        formContent.addComponent(form);

        // Updae fortable

        refreshContacts();
    }

    private void refreshContacts() {
        this.beanItemContainer.removeAllItems();

        List<User> userList;
        try {
            userList = UserManagementControl.getInstance().getAllUsers();
        } catch(ChronoCommandException e) {
            Notification.show("Failed to get all users: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
            return;
        }

        this.beanItemContainer.addAll(userList);
        this.form.setVisible(false);
    }

}
