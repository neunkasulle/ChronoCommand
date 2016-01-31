package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.MessageControl;
import com.github.neunkasulle.chronocommand.model.Category;
import com.github.neunkasulle.chronocommand.model.Message;
import com.github.neunkasulle.chronocommand.model.TimeRecord;
import com.github.neunkasulle.chronocommand.model.User;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.Arrays;

/**
 * Created by Janze on 20.01.2016.
 */
public class MessageView extends BaseView {
    private MessageControl messageControl;


    private Grid contactList = new Grid();

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {

        /* Headline */

        /* Table */

        final BeanItemContainer<Message> beanItemContainer = new BeanItemContainer<>(Message.class);
        prepareContainer(beanItemContainer);
        contactList.setContainerDataSource(beanItemContainer);
        contactList.setSelectionMode(Grid.SelectionMode.SINGLE);
        contactList.setSizeFull();

        //those columns only contains the int values
        contactList.removeColumn("beginning");
        contactList.removeColumn("end");
        //don't want to call toString() method of category object
        contactList.removeColumn("category");
        contactList.setColumnOrder("category.name", "description", "duration");
        //contactList.getDefaultHeaderRow().getCell("beginningTime").setHtml("Begin");
        //contactList.getDefaultHeaderRow().getCell("endTime").setHtml("Ende");
        contactList.getDefaultHeaderRow().getCell("category.name").setHtml("Kategorie");
        contactList.getDefaultHeaderRow().getCell("description").setHtml("Tätigkeit");
        contactList.getDefaultHeaderRow().getCell("duration").setHtml("Dauer");
        refreshContacts();
        contentPane.addComponent(contactList);
    }

    void refreshContacts() {
        final User u1= new User();
        u1.setUsername("Hiwi1");
        final User u2 = new User();
        u2.setUsername("Big Boss");

        final BeanItemContainer<Message> container = new BeanItemContainer<>(
                Message.class, Arrays.asList(
                new Message( u1,u2, "Neuer Stundenzettel")));
        prepareContainer(container);
        contactList.setContainerDataSource(container);
    }

    private void prepareContainer(BeanItemContainer<Message> timeRecordBeanItemContainer) {
        timeRecordBeanItemContainer.addNestedContainerProperty("category.name");
    }

    public void clickSendMessage() {
        messageControl.sendMessage(this.getSessionID(), getReceiverField(), getMessageField());
    }

    public void clickSaveDraft() {

    }

    private String getMessageField() {
        throw new NotYetImplementedException();
    }

    private String getReceiverField() {
        throw new NotYetImplementedException();
    }

}
