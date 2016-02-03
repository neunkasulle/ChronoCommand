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

    private final TimeRecordForm form = new TimeRecordForm(e -> {
        try {
            // Commit the fields from UI to DAO
            this.form.getFormFieldBinding().commit();

            //TODO: DO other stuff
        } catch (FieldGroup.CommitException ex) {
            throw new IllegalArgumentException(ex);
        }
    }, e -> {
        //TODO: Delete it
        refreshTimeRecords();
    }, e -> {
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

        //TODO: No back-end connection!
        /*final HorizontalLayout timeButtons = new HorizontalLayout();
        timeButtons.setSpacing(true);
        headLine.addComponent(timeButtons);
        headLine.setComponentAlignment(timeButtons, Alignment.TOP_RIGHT);*/

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
            if (newestTimeRecord != null && newestTimeRecord.getEnd() == null) {
                startButton.setVisible(false);
                stopButton.setVisible(true);
                elapsedTime.setVisible(true);
                elapsedTime.setValue("Seit " + newestTimeRecord.getBeginning().toString());
            }
        } catch(ChronoCommandException e) {
            //Notification.show(": " + e.getReason().name(), Notification.Type.ERROR_MESSAGE);
        }

        startButton.addClickListener(event1 -> {
            try {
                TimeRecord newTimeRecord = TimeSheetControl.getInstance().newTimeRecord((Category) categorySelection.getValue(), activitySelection.getValue(), LoginControl.getInstance().getCurrentUser());
                startButton.setVisible(false);
                stopButton.setVisible(true);
                elapsedTime.setVisible(true);
                elapsedTime.setValue("Seit " + newTimeRecord.getBeginning().toString());
                refreshTimeSheetList();
                refreshTimeRecords();
            } catch(ChronoCommandException e) {
                Notification.show("Failed to start time record: " + e.getReason().name(), Notification.Type.ERROR_MESSAGE);
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
                Notification.show("Failed to stop time record: " + e.getReason().name(), Notification.Type.ERROR_MESSAGE);
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
                new LocalDateTimeToLocalTimeStringConverter("end"));
        gpcontainer.addGeneratedProperty("duration", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(final Item item, final Object itemId,
                                   final Object propertyId) {
                final LocalDateTime beginning = (LocalDateTime)
                        item.getItemProperty("beginning").getValue();
                final LocalDateTime end = (LocalDateTime)
                        item.getItemProperty("end").getValue();
                final long diff = ChronoUnit.SECONDS.between(beginning, end);
                return LocalDateTime.ofEpochSecond(diff, 0, ZoneOffset.UTC).toLocalTime().toString();
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        //Remove unused columns
        recordList.removeColumn("id");
        recordList.removeColumn("beginning");
        recordList.removeColumn("end");
        recordList.removeColumn("category");

        recordList.setColumnOrder("beginningTime", "endTime", "category.name", "description");
        recordList.getDefaultHeaderRow().getCell("category.name").setHtml("Kategorie");
        recordList.getDefaultHeaderRow().getCell("description").setHtml("Tätigkeit");

        timeRecordSelection.addValueChangeListener(event1 -> {
            refreshTimeRecords();
        });

        // The action form

        formContent.addComponent(form);

        // Updae fortable

        refreshTimeRecords();
    }

    private void refreshTimeRecords() {
        /*final List<TimeRecord> records = Arrays.asList(
                new TimeRecord(LocalDateTime.of(2016, 1, 1, 8, 0), LocalDateTime.of(2016, 1, 1, 9, 0), new Category("Dummy1"), "Did Dummy work", null),
                new TimeRecord(LocalDateTime.of(2016, 1, 1, 8, 0), LocalDateTime.of(2016, 1, 1, 12, 0), new Category("Dummy2"), "Did even more dummy work", null));*/

        TimeSheet timeSheet = (TimeSheet) timeRecordSelection.getValue();
        final List<TimeRecord> records = TimeSheetDAO.getInstance().getTimeRecords(timeSheet);
        this.beanItemContainer.removeAllItems();
        this.beanItemContainer.addAll(records);
        this.form.setVisible(false);
    }


}