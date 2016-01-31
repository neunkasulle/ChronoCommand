package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.model.Category;
import com.github.neunkasulle.chronocommand.model.TimeRecord;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Created by Janze on 20.01.2016.
 */
public class MainView extends BaseView {

    private Grid contactList = new Grid();

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
        final BeanItemContainer<TimeRecord> beanItemContainer = new BeanItemContainer<>(TimeRecord.class);
        prepareContainer(beanItemContainer);
        contactList.setContainerDataSource(beanItemContainer);
        //those columns only contains the int values
        contactList.removeColumn("beginning");
        contactList.removeColumn("end");
        //don't want to call toString() method of category object
        contactList.removeColumn("category");
        contactList.setColumnOrder("beginningTime", "endTime", "category.name", "description", "duration");
        contactList.setSelectionMode(Grid.SelectionMode.SINGLE);
        contactList.setSizeFull();
        contactList.getDefaultHeaderRow().getCell("beginningTime").setHtml("Begin");
        contactList.getDefaultHeaderRow().getCell("endTime").setHtml("Ende");
        contactList.getDefaultHeaderRow().getCell("category.name").setHtml("Kategorie");
        contactList.getDefaultHeaderRow().getCell("description").setHtml("Tätigkeit");
        contactList.getDefaultHeaderRow().getCell("duration").setHtml("Dauer");
        refreshContacts();
    }

    void refreshContacts() {
        final BeanItemContainer<TimeRecord> container = new BeanItemContainer<>(
                TimeRecord.class, Arrays.asList(
                new TimeRecord(LocalDateTime.of(2016,1,1,8,0) , LocalDateTime.of(2016,1,1,9,0), new Category("Dummy1"), "Did Dummy work"),
                new TimeRecord(LocalDateTime.of(2016,1,1,8,0) , LocalDateTime.of(2016,1,1,12,0), new Category("Dummy2"), "Did even more dummy work")));
        prepareContainer(container);
        contactList.setContainerDataSource(container);
    }

    private void prepareContainer(BeanItemContainer<TimeRecord> timeRecordBeanItemContainer) {
        timeRecordBeanItemContainer.addNestedContainerProperty("category.name");
    }

}
