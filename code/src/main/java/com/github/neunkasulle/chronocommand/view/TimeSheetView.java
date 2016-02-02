package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.TimeSheet;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.view.forms.AdminCtrlForm;
import com.github.neunkasulle.chronocommand.view.forms.TimeSheetForm;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;

import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Janze on 20.01.2016.
 */
public class TimeSheetView extends BaseView {

    private BeanItemContainer<TimeSheet> beanItemContainer = new BeanItemContainer<>(TimeSheet.class);

    private final Grid recordList = new Grid();

    private final TimeSheetForm form = new TimeSheetForm(e -> {
        //TODO
    }, e -> {
        //TODO
    }, e -> {
        //TODO
    }, e -> {
        this.recordList.select(null);
        refreshContacts();
    });

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {

        final HorizontalLayout header = new HorizontalLayout(new Label("Stundenzettel von:"),
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
                -> form.edit((TimeSheet) recordList.getSelectedRow()));
        recordList.setSizeFull();

        //Add generated columns
        gpcontainer.addGeneratedProperty("yearAsString", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(final Item item, final Object itemId,
                                   final Object propertyId) {
                final int year = (int)
                        item.getItemProperty("year").getValue();
                return String.valueOf(year);
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
        gpcontainer.addGeneratedProperty("currentHours", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(final Item item, final Object itemId,
                                   final Object propertyId) {
                final int minutes = (int)
                        item.getItemProperty("currentMinutesThisMonth").getValue();
                return String.format("%02d:%02d", minutes / 60, minutes % 60);
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
        gpcontainer.addGeneratedProperty("requiredHours", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(final Item item, final Object itemId,
                                   final Object propertyId) {
                final int hours = (int)
                        item.getItemProperty("requiredHoursPerMonth").getValue();
                return String.format("%02d:00", hours);
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        //Remove unused columns
        recordList.removeColumn("id");
        recordList.removeColumn("user");
        recordList.removeColumn("year");
        recordList.removeColumn("currentMinutesThisMonth");
        recordList.removeColumn("requiredHoursPerMonth");

        recordList.setColumnOrder("yearAsString", "month", "requiredHours", "currentHours", "state");
        recordList.getDefaultHeaderRow().getCell("yearAsString").setHtml("Jahr");
        recordList.getDefaultHeaderRow().getCell("month").setHtml("Monat");
        recordList.getDefaultHeaderRow().getCell("requiredHours").setHtml("Vorgeschriebene Stundenzahl");
        recordList.getDefaultHeaderRow().getCell("currentHours").setHtml("Erreichte Stundenzahl");
        recordList.getDefaultHeaderRow().getCell("state").setHtml("Status");

        // The action form

        formContent.addComponent(form);

        // Updae fortable

        refreshContacts();
    }

    private void refreshContacts() {
        final User hiwi = new User(new Role("Proletarier"), "HIWI2", "Ein Hiwi2", "hiwi2@kit.edu", "asdf", null, 40);

        final List<TimeSheet> records = Arrays.asList(new TimeSheet(hiwi, Month.JANUARY, 2016),
                new TimeSheet(hiwi, Month.APRIL, 2016));
        this.beanItemContainer.removeAllItems();
        this.beanItemContainer.addAll(records);
        this.form.setVisible(false);
    }

}
