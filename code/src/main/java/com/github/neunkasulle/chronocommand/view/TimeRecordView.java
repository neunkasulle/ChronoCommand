package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.LoginControl;
import com.github.neunkasulle.chronocommand.control.TimeSheetControl;
import com.github.neunkasulle.chronocommand.control.UserManagementControl;
import com.github.neunkasulle.chronocommand.model.*;
import com.github.neunkasulle.chronocommand.view.forms.TimeRecordForm;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by Janze on 20.01.2016.
 */
public class TimeRecordView extends BaseView {
    private User user;
    private TimeSheet timeSheet = null;

    private BeanItemContainer<TimeRecord> beanItemContainer = new BeanItemContainer<>(TimeRecord.class);

    private final Grid recordList = new Grid();

    private Label header;
    private Button downloadPdf;
    private Button startButton;
    private Button stopButton;
    private Label elapsedTime;

    private final TimeRecordForm form = new TimeRecordForm(event -> {
        // save
        try {
            this.form.getFormFieldBinding().commit();
            TimeSheetControl.getInstance().editTimeRecord(this.form.getFormFieldBinding().getItemDataSource().getBean());
            refreshTimeRecords();

            //TODO: DO other stuff
        } catch (FieldGroup.CommitException e) {
            throw new IllegalArgumentException(e);
        } catch (ChronoCommandException e) {
            Notification.show("Failed to save Timerecord: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
        }
    }, event -> {
        // delete
        //TODO: Delete timerecord
        refreshTimeRecords();
    }, event -> {
        // cancel
        this.recordList.select(null);
        refreshTimeRecords();
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
            String[] parameters = event.getParameters().split("/");
            if (parameters.length != 3) {
                try {
                    user = LoginControl.getInstance().getCurrentUser();
                } catch (ChronoCommandException e) {
                    Notification.show("Failed to get current user: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
                    return;
                }
            } else {
                try {
                    user = UserManagementControl.getInstance().findUser(parameters[0]);
                } catch (ChronoCommandException e) {
                    Notification.show("Failed to get user \'" + parameters[0] + "\' because: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
                    return;
                }
                int year;
                Month month;
                try {
                    year = Integer.parseInt(parameters[1]);
                    month = Month.of(Integer.parseInt(parameters[2]));
                } catch (NumberFormatException e) {
                    Notification.show("Failed to parse number", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                try {
                    timeSheet = TimeSheetControl.getInstance().getTimeSheet(month, year, user);
                } catch (ChronoCommandException e) {
                    Notification.show("Failed to get timesheet", Notification.Type.ERROR_MESSAGE);
                    return;
                }
            }
        }

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setSpacing(true);
        contentPane.addComponent(headerLayout);

        header = new Label();
        header.setId("page-header");
        header.setSizeFull();
        headerLayout.addComponent(header);

        downloadPdf = new Button("Download pdf");
        headerLayout.addComponent(downloadPdf);
        downloadPdf.addClickListener(event2 -> {
            try {
                TimeSheet timeSheet = this.timeSheet;
                if (timeSheet == null) {
                    timeSheet = (TimeSheet) timeRecordSelection.getValue();
                }
                File pdffile = TimeSheetControl.getInstance().printTimeSheet(timeSheet);
                FileResource pdf = new FileResource(pdffile);
                getUI().getPage().open(pdf, "_blank", true);
                /*FileDownloader fileDownloader = new FileDownloader(pdf);
                fileDownloader.extend(this.form.getExportPDFBtn());*/
            } catch(ChronoCommandException ex) {
                Notification.show("Failed to print pdf:" + ex.getReason().toString());
            }
        });

        if (timeSheet == null) {
            final Button submitTimeRecordButton = new Button("Submit Timesheet");
            submitTimeRecordButton.addClickListener(event2 -> {
                try {
                    TimeSheetControl.getInstance().lockTimeSheet((TimeSheet) timeRecordSelection.getValue());
                    updateHeaderLabel();
                } catch (ChronoCommandException e) {
                    Notification.show("Failed to lock timesheet: " + e.getReason().toString(), Notification.Type.WARNING_MESSAGE);
                }
            });
            headerLayout.addComponent(submitTimeRecordButton);
            submitTimeRecordButton.setSizeFull();
        }

        updateHeaderLabel();

        /* Headline */

        final HorizontalLayout headLine = new HorizontalLayout();
        contentPane.addComponent(headLine);
        headLine.setSpacing(true);

        final ComboBox categorySelection = new ComboBox();
        categorySelection.setInputPrompt("Project");
        categorySelection.addItems(TimeSheetControl.getInstance().getAllCategories());
        headLine.addComponent(categorySelection);
        headLine.setComponentAlignment(categorySelection, Alignment.TOP_LEFT);

        final TextField activitySelection = new TextField();
        activitySelection.setInputPrompt("Description");
        headLine.addComponent(activitySelection);
        headLine.setComponentAlignment(activitySelection, Alignment.TOP_CENTER);

        startButton = new Button("Start");
        headLine.addComponent(startButton);

        stopButton = new Button("Stop");
        stopButton.setVisible(false);
        headLine.addComponent(stopButton);

        elapsedTime = new Label();
        elapsedTime.addStyleName(ValoTheme.LABEL_COLORED);
        elapsedTime.addStyleName(ValoTheme.LABEL_LARGE);
        elapsedTime.setVisible(false);
        headLine.addComponent(elapsedTime);

        try {
            TimeRecord newestTimeRecord = TimeSheetControl.getInstance().getLatestTimeRecord(LoginControl.getInstance().getCurrentUser());
            if (newestTimeRecord != null && newestTimeRecord.getEnding() == null) {
                startButton.setVisible(false);
                stopButton.setVisible(true);
                elapsedTime.setVisible(true);
                String hour = String.valueOf(newestTimeRecord.getBeginning().getHour());
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }
                String minute = String.valueOf(newestTimeRecord.getBeginning().getMinute());
                if (minute.length() == 1) {
                    minute = "0" + minute;
                }
                elapsedTime.setValue("since " + hour + ":" + minute);
                categorySelection.select(newestTimeRecord.getCategory());
                activitySelection.setValue(newestTimeRecord.getDescription());
            }
        } catch(ChronoCommandException e) {
            Notification.show("Failed to get latest time record: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
        }

        startButton.addClickListener(event1 -> {
            try {
                TimeRecord newTimeRecord = TimeSheetControl.getInstance().newTimeRecord((Category) categorySelection.getValue(),
                        activitySelection.getValue(), LoginControl.getInstance().getCurrentUser());
                startButton.setVisible(false);
                stopButton.setVisible(true);
                elapsedTime.setVisible(true);
                String hour = String.valueOf(newTimeRecord.getBeginning().getHour());
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }
                String minute = String.valueOf(newTimeRecord.getBeginning().getMinute());
                if (minute.length() == 1) {
                    minute = "0" + minute;
                }
                elapsedTime.setValue("since " + hour + ":" + minute);
                refreshTimeSheetList();
                refreshTimeRecords();
            } catch(ChronoCommandException e) {
                Notification.show("Failed to start time record: " + e.getReason().toString(), Notification.Type.WARNING_MESSAGE);
            }
        });
        stopButton.addClickListener(event1 -> {
            try {
                TimeSheetControl.getInstance().closeTimeRecord((Category) categorySelection.getValue(), activitySelection.getValue(), LoginControl.getInstance().getCurrentUser());
                startButton.setVisible(true);
                stopButton.setVisible(false);
                elapsedTime.setVisible(false);
                elapsedTime.setValue("");
                refreshTimeSheetList();
                refreshTimeRecords();
            } catch(ChronoCommandException e) {
                Notification.show("Failed to stop time record: " + e.getReason().toString(), Notification.Type.WARNING_MESSAGE);
            }
        });

        /* Form & able */

        final HorizontalLayout formContent = new HorizontalLayout();
        formContent.setSizeFull();
        contentPane.addComponent(formContent);

        /* Actual table */

        formContent.addComponent(recordList);
        beanItemContainer.addNestedContainerProperty("category.name");
        final GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(beanItemContainer);
        recordList.setContainerDataSource(gpcontainer);
        recordList.setSelectionMode(Grid.SelectionMode.SINGLE);
        recordList.addSelectionListener(e -> {
            if (recordList.getSelectedRow() != null && ((TimeRecord) recordList.getSelectedRow()).getBeginning() != null
                    && this.timeSheet == null && ((TimeSheet) timeRecordSelection.getValue()).getState() == TimeSheetState.UNLOCKED) {
                form.edit((TimeRecord) recordList.getSelectedRow());
            }
        });
        recordList.setSizeFull();

        //Add generated columns
        gpcontainer.addGeneratedProperty("beginningTime",
                new LocalDateTimeToLocalTimeStringConverter("beginning"));
        gpcontainer.addGeneratedProperty("endTime",
                new LocalDateTimeToLocalTimeStringConverter("ending"));
        gpcontainer.addGeneratedProperty("duration", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(final Item item, final Object itemId,
                                   final Object propertyId) {
                final LocalDateTime beginning = (LocalDateTime)
                        item.getItemProperty("beginning").getValue();
                final LocalDateTime end = (LocalDateTime)
                        item.getItemProperty("ending").getValue();
                if (end == null) {
                    return "-";
                }
                final long diff = ChronoUnit.SECONDS.between(beginning, end);
                return LocalDateTime.ofEpochSecond(diff, 0, ZoneOffset.UTC).toLocalTime().toString();
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
        gpcontainer.addGeneratedProperty("date", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                final LocalDateTime beginningTime = (LocalDateTime) item.getItemProperty("beginning").getValue();
                String day = String.valueOf(beginningTime.getDayOfMonth());
                if (day.length() == 1) {
                    day = "0" + day;
                }
                String month = String.valueOf(beginningTime.getMonthValue());
                if (month.length() == 1) {
                    month = "0" + month;
                }
                String year = String.valueOf(beginningTime.getYear());
                return day + "." + month + "." + year;
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        recordList.setColumns("date", "duration", "category.name", "description", "beginningTime", "endTime");
        recordList.getDefaultHeaderRow().getCell("category.name").setHtml("Category");
        recordList.getDefaultHeaderRow().getCell("description").setHtml("Description");

        timeRecordSelection.addValueChangeListener(event1 -> {
            refreshTimeRecords();
            updateHeaderLabel();
        });

        // The action form

        formContent.addComponent(form);

        // Updae fortable

        refreshTimeRecords();
    }

    private void refreshTimeRecords() {
        TimeSheet timeSheet;
        if (this.timeSheet != null) {
            timeSheet = this.timeSheet;
        } else {
            if (timeRecordSelection == null) {
                return;
            }
            timeSheet = (TimeSheet) timeRecordSelection.getValue();
        }
        if (timeSheet == null) {
            return;
        }
        this.beanItemContainer.removeAllItems();
        try {
            final List<TimeRecord> records = TimeSheetControl.getInstance().getTimeRecords(timeSheet);
            this.beanItemContainer.addAll(records);
        } catch (ChronoCommandException e) {
            Notification.show("Failed to get time records: " + e.getReason().toString(), Notification.Type.ERROR_MESSAGE);
        }
        this.form.setVisible(false);

        updateHeaderLabel();
    }

    private void updateHeaderLabel() {
        if (timeRecordSelection == null) {
            header.setValue("");
            header.setVisible(false);
            return;
        }
        TimeSheet timeSheet;
        if (this.timeSheet != null) {
            timeSheet = this.timeSheet;
        } else {
            timeSheet = (TimeSheet) timeRecordSelection.getValue();
        }
        if (timeSheet == null) {
            header.setValue("");
            header.setVisible(false);
            return;
        }

        String headerValue = "";
        if (timeSheet.getState() == TimeSheetState.UNLOCKED) {
            headerValue = "Record time / ";
        }
        headerValue += "Status: " + timeSheet.getState().toString();
        header.setValue(headerValue);
        header.setVisible(true);
    }
}
