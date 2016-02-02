package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.MessageControl;
import com.github.neunkasulle.chronocommand.model.Message;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.view.forms.MessageForm;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.hibernate.cfg.NotYetImplementedException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Janze on 20.01.2016.
 */
public class MessageView extends BaseView {
    private MessageControl messageControl;

    private final BeanItemContainer<Message> beanItemContainer = new BeanItemContainer<>(Message.class);

    private final Grid messagetList = new Grid();

    private final MessageForm form = new MessageForm(e -> {
        try {
            // Commit the fields from UI to DAO
            this.form.getFormFieldBinding().commit();

           //TODO: DO other stuff
        } catch (FieldGroup.CommitException ex) {
            throw new IllegalArgumentException(ex);
        }
    },e -> {
        messagetList.select(null);
        refreshContacts();
    });

    @Override
    protected void enterTemplate(final ViewChangeListener.ViewChangeEvent event, final Layout contentPane) {

        final TextField filter = new TextField();
        contentPane.addComponent(filter);
        filter.setSizeFull();

        final HorizontalLayout formContent = new HorizontalLayout();
        formContent.setSizeFull();
        contentPane.addComponent(formContent);

         /* Table */

        formContent.addComponent(messagetList);
        beanItemContainer.addNestedContainerProperty("sender.username");
        final GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(beanItemContainer);
        messagetList.setContainerDataSource(gpcontainer);
        messagetList.setSelectionMode(Grid.SelectionMode.SINGLE);
        messagetList.addSelectionListener(e
                -> form.edit((Message) messagetList.getSelectedRow()));
        messagetList.setSizeFull();

        //Add generated columns
        gpcontainer.addGeneratedProperty("send", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(final Item item, final Object itemId,
                                   final Object propertyId) {
                final int time = (int)
                        item.getItemProperty("sendTime").getValue();
                final LocalDateTime localDataTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
                return String.format("%s %s", localDataTime.toLocalDate().toString(), localDataTime.toLocalTime().toString());
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        //Remove unused columns
        messagetList.removeColumn("id");
        messagetList.removeColumn("sender");
        messagetList.removeColumn("recipient");
        messagetList.removeColumn("sendTime");
        messagetList.removeColumn("receiveTime");
        messagetList.removeColumn("read");

        //Ordering & column names

        messagetList.setColumnOrder("sender.username", "content");
        messagetList.getDefaultHeaderRow().getCell("sender.username").setHtml("Sender");
        messagetList.getDefaultHeaderRow().getCell("content").setHtml("Nachricht");


        // The action form

        formContent.addComponent(form);

        // Updae fortable

        refreshContacts();

    }

    private void refreshContacts() {
        final User u1 = new User();
        u1.setUsername("Hiwi1");
        final User u2 = new User();
        u2.setUsername("Big Boss");

        final List<Message> records = Arrays.asList(new Message(u1, u2, "Neuer Stundenzettel"),
                new Message(u2, u1, "You are fired"));
        this.beanItemContainer.removeAllItems();
        this.beanItemContainer.addAll(records);
        this.form.setVisible(false);
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
