package com.github.neunkasulle.chronocommand.view.forms;

import com.github.neunkasulle.chronocommand.model.User;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;

/**
 * Created by Ming-Samsung on 2016/1/31.
 */
public class SupervisorCtrlForm extends FormLayout {

    private Button toTimeSheets = new Button("Stundenzettel anzeigen");
    private Button sendMessage = new Button("Nachricht senden");
    private Button cancel = new Button("Abbrechen");

    private User object;

    private BeanFieldGroup<User> formFieldBindings;

    public User getCurrentFormObject() {
        return this.object;
    }

    public BeanFieldGroup<User> getFormFieldBinding() {
        return this.formFieldBindings;
    }

    public SupervisorCtrlForm(final Button.ClickListener toTimeSheetsOperation,
                              final Button.ClickListener sendMessageOperation,
                              final Button.ClickListener cancelOperation) {
        this.toTimeSheets.addClickListener(toTimeSheetsOperation);
        this.sendMessage.addClickListener(sendMessageOperation);
        this.cancel.addClickListener(cancelOperation);

        addComponents(toTimeSheets, sendMessage, cancel);
    }

    public void edit(final User object) {
        this.object = object;
        if (object != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(object, this);
            toTimeSheets.focus();
        }
        setVisible(object != null);
    }

}
