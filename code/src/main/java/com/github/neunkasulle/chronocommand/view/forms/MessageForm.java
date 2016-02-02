package com.github.neunkasulle.chronocommand.view.forms;

import com.github.neunkasulle.chronocommand.model.Message;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.function.Consumer;

/**
 * Created by Ming-Samsung on 2016/1/31.
 */
public class MessageForm extends FormLayout {

    private TextArea content = new TextArea("Nachricht");

    private RichTextArea messageInputArea = new RichTextArea();


    private Button answer = new Button("Antworten");
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
        this.answer.addClickListener(saveOperation);
        this.cancel.addClickListener(cancelOperation);

        answer.setStyleName(ValoTheme.BUTTON_PRIMARY);
        answer.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);

        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(answer, cancel);
        actions.setSpacing(true);

        addComponents(content, messageInputArea, actions);
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

}
