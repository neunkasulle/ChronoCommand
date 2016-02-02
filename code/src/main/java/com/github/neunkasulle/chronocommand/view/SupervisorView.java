package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.view.forms.AdminCtrlForm;
import com.github.neunkasulle.chronocommand.view.forms.SupervisorCtrlForm;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.Arrays;
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
        getUI().getNavigator().navigateTo(MainUI.MESSAGEVIEW);
    }, e -> {
        this.recordList.select(null);
        refreshContacts();
    });

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {

        final HorizontalLayout header = new HorizontalLayout(new Label("Mitarbeiter von Betreuer:"),
                new Label("Max Mustermann"));
        header.setId("page-header");
        header.setSizeFull();
        header.setSpacing(true);
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

        recordList.setColumnOrder("username", "realname", "email", "roleSumary", "hoursPerMonth");
        recordList.getDefaultHeaderRow().getCell("username").setHtml("Benutzername");
        recordList.getDefaultHeaderRow().getCell("realname").setHtml("Name");
        recordList.getDefaultHeaderRow().getCell("email").setHtml("E-Mail");
        recordList.getDefaultHeaderRow().getCell("roleSumary").setHtml("Rollen");
        recordList.getDefaultHeaderRow().getCell("hoursPerMonth").setHtml("Stunden / Woche");

        // The action form

        formContent.addComponent(form);

        // Updae fortable

        refreshContacts();
    }

    private void refreshContacts() {
        final User hiwi1 = new User(new Role("Proletarier"), "HIWI2", "Ein Hiwi1", "hiwi1@kit.edu", "asdf", null, 40);
        final User hiwi2 = new User(new Role("Proletarier"), "HIWI2", "Ein Hiwi2", "hiwi2@kit.edu", "asdf", null, 40);

        final List<User> records = Arrays.asList(hiwi1, hiwi2);
        this.beanItemContainer.removeAllItems();
        this.beanItemContainer.addAll(records);
        this.form.setVisible(false);
    }


}
