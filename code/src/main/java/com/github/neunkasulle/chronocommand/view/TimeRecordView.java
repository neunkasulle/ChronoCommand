package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.LoginControl;
import com.github.neunkasulle.chronocommand.control.TimeSheetControl;
import com.github.neunkasulle.chronocommand.model.*;
import com.github.neunkasulle.chronocommand.view.forms.TimeRecordForm;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by Janze on 20.01.2016.
 */
public class TimeRecordView extends BaseView {

    private BeanItemContainer<TimeRecord> beanItemContainer = new BeanItemContainer<>(TimeRecord.class);

    private final Grid recordList = new Grid();

    private Button startButton;
    private Button stopButton;
    private Label elapsedTime;

    private final TimeRecordForm form = new TimeRecordForm(event -> {
        // save
        try {
            // Commit the fields from UI to DAO
            this.form.getFormFieldBinding().commit();
            TimeSheetControl.getInstance().editTimeRecord(this.form.getFormFieldBinding().getItemDataSource().getBean());
            refreshTimeRecords();

            //TODO: DO other stuff
        } catch (FieldGroup.CommitException e) {
            throw new IllegalArgumentException(e);
        } catch (ChronoCommandException e) {
            Notification.show("Failed to save Timerecord: " + e, Notification.Type.ERROR_MESSAGE);
        }
    }, event -> {
        // delete
        //TODO: Delete it
        refreshTimeRecords();
    }, event -> {
        // cancel
        this.recordList.select(null);
        refreshTimeRecords();
    });

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {

        final Label header = new Label("Zeit erfassen");
        header.setId("page-header");
        header.setSizeFull();
        contentPane.addComponent(header);

        /* Headline */

        final HorizontalLayout headLine = new HorizontalLayout();
        contentPane.addComponent(headLine);
        headLine.setSpacing(true);

        final ComboBox categorySelection = new ComboBox();
        categorySelection.setInputPrompt("Kategorie");
        categorySelection.addItems(TimeSheetControl.getInstance().getAllCategories());
        headLine.addComponent(categorySelection);
        headLine.setComponentAlignment(categorySelection, Alignment.TOP_LEFT);

        final TextField activitySelection = new TextField();
        activitySelection.setInputPrompt("Tätigkeit");
        headLine.addComponent(activitySelection);
        headLine.setComponentAlignment(activitySelection, Alignment.TOP_CENTER);

        startButton = new Button("Starten");
        headLine.addComponent(startButton);

        stopButton = new Button("Stoppen");
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
                elapsedTime.setValue("since " + newestTimeRecord.getBeginning().getHour() + ":" + newestTimeRecord.getBeginning().getMinute());
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
                elapsedTime.setValue("since " + newTimeRecord.getBeginning().getHour() + ":" + newTimeRecord.getBeginning().getMinute());
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
        recordList.addSelectionListener(e
                -> form.edit((TimeRecord) recordList.getSelectedRow()));
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
        });

        // The action form

        formContent.addComponent(form);

        // Updae fortable

        refreshTimeRecords();
    }

    private void refreshTimeRecords() {
        TimeSheet timeSheet = (TimeSheet) timeRecordSelection.getValue();
        final List<TimeRecord> records = TimeSheetDAO.getInstance().getTimeRecords(timeSheet);
        this.beanItemContainer.removeAllItems();
        this.beanItemContainer.addAll(records);
        this.form.setVisible(false);
    }


}
