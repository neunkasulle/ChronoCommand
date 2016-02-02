package com.github.neunkasulle.chronocommand.view.forms;

import com.github.neunkasulle.chronocommand.model.Message;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by Ming-Samsung on 2016/1/31.
 */
public class MessageForm extends FormLayout {

    private TextField recipientUsername = new TextField("Empfänger");
    private TextArea content = new TextArea("Nachricht");

    private RichTextArea messageInputArea = new RichTextArea();

    private Button send = new Button("Antworten");
    private Button cancel = new Button("Cancel");

    private Message object;
    private BeanFieldGroup<Message> formFieldBindings;

    public Message getCurrentFormObject() {
        return this.object;
    }

    public BeanFieldGroup<Message> getFormFieldBinding() {
        return this.formFieldBindings;
    }

    public MessageForm(final Button.ClickListener saveOperation, final Button.ClickListener cancelOperation) {
        this.send.addClickListener(saveOperation);
        this.cancel.addClickListener(cancelOperation);

        send.setStyleName(ValoTheme.BUTTON_PRIMARY);
        send.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);

        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(send, cancel);
        actions.setSpacing(true);

        addComponents(recipientUsername, content, messageInputArea, actions);
    }

    public void edit(Message message) {
        this.object = message;
        if (message != null) {
            // Bind the properties of the contact POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(message, this);
            content.focus();
        }
        setVisible(message != null);
    }

    public void create(Message message) {
        this.object = message;
        if (message != null) {
            // Bind the properties of the contact POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(message, this);
            formFieldBindings.bind(recipientUsername, "recipient.username");
            content.focus();
        }
        setVisible(message != null);
    }

}
