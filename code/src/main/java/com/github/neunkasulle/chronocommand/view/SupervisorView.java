package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.LoginControl;
import com.github.neunkasulle.chronocommand.control.UserManagementControl;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.view.forms.SupervisorCtrlForm;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.List;
import java.util.Set;

/**
 * Created by Janze on 20.01.2016.
 */
public class SupervisorView extends BaseView {

    private BeanItemContainer<User> beanItemContainer = new BeanItemContainer<>(User.class);

    private final Grid recordList = new Grid();

    private final SupervisorCtrlForm form = new SupervisorCtrlForm( e -> {
        getUI().getNavigator().navigateTo(MainUI.TIMESHEETVIEW);
    }, e -> {
        this.recordList.select(null);
        refreshSupervisedUsers();
    });

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {

        /*
        final TextField filter = new TextField();
        filter.setSizeFull();
        */

        final HorizontalLayout header = new HorizontalLayout(new Label("Supervised HIWIs:"));
        header.setId("page-header");
        header.setSizeFull();
        header.setSpacing(true);
        contentPane.addComponent(header);

        /* Form & table */

        final HorizontalLayout formContent = new HorizontalLayout();
        formContent.setSizeFull();
        contentPane.addComponent(formContent);

        /* Actual table */

        formContent.addComponent(recordList);
        final GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(beanItemContainer);
        recordList.setContainerDataSource(gpcontainer);
        recordList.setSelectionMode(Grid.SelectionMode.SINGLE);
        recordList.addSelectionListener(e
                -> form.edit((User) recordList.getSelectedRow()));
        recordList.setSizeFull();

        //Add generated columns
        gpcontainer.addGeneratedProperty("roleSummary", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(final Item item, final Object itemId,
                                   final Object propertyId) {
                @SuppressWarnings("unchecked")
                final Set<Role> roles = (Set<Role>)
                        item.getItemProperty("roles").getValue();
                final StringBuilder ruleStr = new StringBuilder();

                for (final Role role : roles) {
                    ruleStr.append(role.getDescription());
                    ruleStr.append(" ");
                }

                return ruleStr.toString();
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });


        recordList.setColumns("username", "realname", "email", "hoursPerMonth", "roleSummary");

        recordList.setColumnOrder("username", "realname", "email", "hoursPerMonth", "roleSummary");
        recordList.getDefaultHeaderRow().getCell("username").setHtml("Username");
        recordList.getDefaultHeaderRow().getCell("realname").setHtml("Name");
        recordList.getDefaultHeaderRow().getCell("email").setHtml("Email");
        recordList.getDefaultHeaderRow().getCell("hoursPerMonth").setHtml("Hours per month");
        recordList.getDefaultHeaderRow().getCell("roleSummary").setHtml("Roles");

        // The action form

        formContent.addComponent(form);

        // Updae fortable

        refreshSupervisedUsers();
    }

    private void refreshSupervisedUsers() {
        this.beanItemContainer.removeAllItems();

        List<User> userList;
        try {
            userList = UserManagementControl.getInstance().getUsersBySupervisor(LoginControl.getInstance().getCurrentUser());
        } catch(ChronoCommandException e) {
            Notification.show("Failed to get supervised users: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
            return;
        }

        this.beanItemContainer.addAll(userList);
        this.form.setVisible(false);
    }


}
