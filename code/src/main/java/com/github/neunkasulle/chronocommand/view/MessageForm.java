package com.github.neunkasulle.chronocommand.view;

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

    private TextField content = new TextField("Nachricht");
    private Button save = new Button("Save");
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
        this.save.addClickListener(saveOperation);
        this.cancel.addClickListener(cancelOperation);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);

        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);

        addComponents(content, actions);
    }

    void edit(Message message) {
        this.object = message;
        if (message != null) {
            // Bind the properties of the contact POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(message, this);
            content.focus();
        }
        setVisible(message != null);
    }

}
