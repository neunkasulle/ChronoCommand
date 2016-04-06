package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.LoginControl;
import com.github.neunkasulle.chronocommand.control.TimeSheetControl;
import com.github.neunkasulle.chronocommand.control.UserManagementControl;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.TimeSheet;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.view.forms.TimeSheetForm;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by Janze on 20.01.2016.
 */
public class TimeSheetView extends BaseView {
    private User user;

    private BeanItemContainer<TimeSheet> beanItemContainer = new BeanItemContainer<>(TimeSheet.class);

    private final Grid recordList = new Grid();

    private final TimeSheetForm form = new TimeSheetForm(e -> {
        // show timerecords
        TimeSheet timeSheet = this.form.getCurrentFormObject();
        getUI().getNavigator().navigateTo(MainUI.TIMERECORDVIEW + "/" + user.getUsername() + "/" + timeSheet.getYear() + "/" + timeSheet.getMonth().getValue());
    }, e -> {
        // export pdf
        try {
            ByteArrayOutputStream pdffile = TimeSheetControl.getInstance().printTimeSheet(this.form.getCurrentFormObject());
            StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {

                    InputStream targetStream = new ByteArrayInputStream(pdffile.toByteArray());

                    return targetStream;
                }
            };
            String username = this.form.getCurrentFormObject().getUser().getUsername();
            String date = Integer.toString(this.form.getCurrentFormObject().getYear()) + "-" + Integer.toString(this.form.getCurrentFormObject().getMonth().getValue());
            StreamResource pdf = new StreamResource(streamSource, date + "-" + username + ".pdf");
            getUI().getPage().open(pdf, "_blank", true);
        } catch (ChronoCommandException ex) {
            Notification.show("Failed to print pdf: " + ex.getReason().toString(), Notification.Type.ERROR_MESSAGE);
        }
    }, e -> {
        // mark as checked
        try {
            TimeSheetControl.getInstance().approveTimeSheet(this.form.getCurrentFormObject());
        } catch (ChronoCommandException ex) {
            Notification.show("Failed to approve timesheet:" + ex.getReason().toString());
        }
    }, e -> {
        // mark as bad
        // TODO add message to timesheet
        try {
            TimeSheetControl.getInstance().unlockTimeSheet(this.form.getCurrentFormObject());
        } catch (ChronoCommandException ex) {
            Notification.show("Failed to unlock timesheet:" + ex.getReason().toString());
        }
    }, e -> {
        // cancel
        this.recordList.select(null);
        refreshTimeSheets();
    });

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {
        if (event.getParameters().isEmpty()) {
            try {
                user = LoginControl.getInstance().getCurrentUser();
            } catch (ChronoCommandException e) {
                Notification.show("Failed to get current user: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
                return;
            }
        } else {
            try {
                user = UserManagementControl.getInstance().findUser(event.getParameters());
            } catch (ChronoCommandException e) {
                Notification.show("Failed to get user \'" + event.getParameters() + "\' because: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
                return;
            }
        }

        final HorizontalLayout header = new HorizontalLayout(new Label("All timesheets from " + user.getRealname()));
        header.setId("page-header");
        header.setSizeFull();
        header.setSpacing(true);
        contentPane.addComponent(header);

        /*
        final TextField filter = new TextField();
        contentPane.addComponent(filter);
        filter.setSizeFull();
        */
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
                return String.format("%02d", hours);
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
        recordList.removeColumn("messages");

        recordList.setColumnOrder("yearAsString", "month", "requiredHours", "currentHours", "state");
        recordList.getDefaultHeaderRow().getCell("yearAsString").setHtml("Year");
        recordList.getDefaultHeaderRow().getCell("month").setHtml("Month");
        recordList.getDefaultHeaderRow().getCell("requiredHours").setHtml("Hours per Month");
        recordList.getDefaultHeaderRow().getCell("currentHours").setHtml("Reached Hours");
        recordList.getDefaultHeaderRow().getCell("state").setHtml("Status");

        // The action form

        formContent.addComponent(form);

        // Updae fortable

        refreshTimeSheets();
    }

    private void refreshTimeSheets() {
        this.beanItemContainer.removeAllItems();

        List<TimeSheet> timeSheetList;
        try {
            timeSheetList = TimeSheetControl.getInstance().getTimeSheetsFromUser(user);
            Collections.sort(timeSheetList);
        } catch(ChronoCommandException e) {
            Notification.show("Failed to get timesheets: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
            return;
        }

        this.beanItemContainer.addAll(timeSheetList);
        this.form.setVisible(false);
    }

}
