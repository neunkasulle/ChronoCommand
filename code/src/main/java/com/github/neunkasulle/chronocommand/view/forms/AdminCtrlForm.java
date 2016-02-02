package com.github.neunkasulle.chronocommand.view.forms;

import com.github.neunkasulle.chronocommand.control.TimeSheetControl;
import com.github.neunkasulle.chronocommand.model.Category;
import com.github.neunkasulle.chronocommand.model.TimeRecord;
import com.github.neunkasulle.chronocommand.model.User;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by Ming-Samsung on 2016/1/31.
 */
public class AdminCtrlForm extends FormLayout {

    private Button toSupervisor = new Button("Zur Betreuer-Ansicht");
    private Button editUser = new Button("Benutzerdaten bearbeiten");
    private Button deleteUser = new Button("Benutzerdaten Löschen");
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

    public AdminCtrlForm(final Button.ClickListener toSuperviserOperation,
                         final Button.ClickListener editUserOperation,
                         final Button.ClickListener  deleteUserOperation,
                         final Button.ClickListener toTimeSheetsOperation,
                         final Button.ClickListener sendMessageOperation,
                         final Button.ClickListener cancelOperation) {
        this.toSupervisor.addClickListener(toSuperviserOperation);
        this.editUser.addClickListener(editUserOperation);
        this.deleteUser.addClickListener(deleteUserOperation);
        this.toTimeSheets.addClickListener(toTimeSheetsOperation);
        this.sendMessage.addClickListener(sendMessageOperation);
        this.cancel.addClickListener(cancelOperation);

        addComponents(toSupervisor, editUser, deleteUser, toTimeSheets, sendMessage, cancel);
    }

    public void edit(final User object) {
        this.object = object;
        if (object != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(object, this);
            toSupervisor.focus();
        }
        setVisible(object != null);
    }

}
