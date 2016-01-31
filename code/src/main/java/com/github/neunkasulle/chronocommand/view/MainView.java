package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.model.Category;
import com.github.neunkasulle.chronocommand.model.TimeRecord;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Janze on 20.01.2016.
 */
public class MainView extends BaseView {

    private Grid contactList = new Grid();
    private BeanItemContainer<TimeRecord> beanItemContainer = new BeanItemContainer<>(TimeRecord.class);

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {

        /* Headline */

        final HorizontalLayout headLine = new HorizontalLayout();
        contentPane.addComponent(headLine);

        /* Table */

        //TODO: No back-end connection!
        final ComboBox categorySelection = new ComboBox(null, Arrays.asList("Kategorie1", "Kategorie2"));
        categorySelection.setInputPrompt("Kategorie auswählen");
        headLine.addComponent(categorySelection);
        headLine.setComponentAlignment(categorySelection, Alignment.TOP_LEFT);

        //TODO: No back-end connection!
        final ComboBox activitySelection = new ComboBox(null, Arrays.asList("Tätigkeit1", "Tätigkeit2"));
        activitySelection.setInputPrompt("Tätigkeit auswählen");
        headLine.addComponent(activitySelection);
        headLine.setComponentAlignment(activitySelection, Alignment.TOP_CENTER);

        //TODO: No back-end connection!
        final HorizontalLayout timeButtons = new HorizontalLayout();
        headLine.addComponent(timeButtons);
        headLine.setComponentAlignment(timeButtons, Alignment.TOP_RIGHT);

        timeButtons.addComponent(new Button("Starten"));
        timeButtons.addComponent(new Button("Stopen"));
        timeButtons.addComponent(new Button("Speichern"));

        /* Table */

        contentPane.addComponent(contactList);
        beanItemContainer.addNestedContainerProperty("category.name");
        final GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(beanItemContainer);
        contactList.setContainerDataSource(gpcontainer);
        contactList.setSelectionMode(Grid.SelectionMode.SINGLE);
        contactList.setSizeFull();

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
        contactList.removeColumn("id");
        contactList.removeColumn("beginning");
        contactList.removeColumn("end");
        contactList.removeColumn("category");

        contactList.setColumnOrder("beginningTime", "endTime","category.name", "description");
        contactList.getDefaultHeaderRow().getCell("category.name").setHtml("Kategorie");
        contactList.getDefaultHeaderRow().getCell("description").setHtml("Tätigkeit");
        refreshContacts();
    }

    void refreshContacts() {
        final List<TimeRecord> records =  Arrays.asList(
                new TimeRecord(LocalDateTime.of(2016,1,1,8,0) , LocalDateTime.of(2016,1,1,9,0), new Category("Dummy1"), "Did Dummy work"),
                new TimeRecord(LocalDateTime.of(2016,1,1,8,0) , LocalDateTime.of(2016,1,1,12,0), new Category("Dummy2"), "Did even more dummy work"));
        this.beanItemContainer.removeAllItems();
        this.beanItemContainer.addAll(records);
    }


}
