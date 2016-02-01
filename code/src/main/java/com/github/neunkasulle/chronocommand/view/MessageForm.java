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

    private Button save = new Button("Save", this::save);
    private Button cancel = new Button("Cancel", this::cancel);
    private TextField content = new TextField("Nachricht");

    private final Consumer<Message> saveOperation;
    private final Consumer<Button.ClickEvent> cancelOperation;

    private Message message;

    // Easily bind forms to beans and manage validation and buffering
   private BeanFieldGroup<Message> formFieldBindings;

    public MessageForm(final Consumer<Message> saveOperation, final Consumer<Button.ClickEvent> cancelOperation) {
        this.saveOperation = saveOperation;
        this.cancelOperation = cancelOperation;

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);

        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);

        addComponents(actions, content);
    }

    /* Use any JVM language.
     *
     * Vaadin supports all languages supported by Java Virtual Machine 1.6+.
     * This allows you to program user interface in Java 8, Scala, Groovy or any other
     * language you choose.
     * The new languages give you very powerful tools for organizing your code
     * as you choose. For example, you can implement the listener methods in your
     * compositions or in separate controller classes and receive
     * to various Vaadin component events, like button clicks. Or keep it simple
     * and compact with Lambda expressions.
     */
    public void save(final Button.ClickEvent event) {
        try {
            // Commit the fields from UI to DAO
            formFieldBindings.commit();

            // Submit changes to higher level view
            this.saveOperation.accept(message);
        } catch (FieldGroup.CommitException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void cancel(final Button.ClickEvent event) {
        this.cancelOperation.accept(event);
    }

    void edit(Message message) {
        this.message  = message;
        if(message != null) {
            // Bind the properties of the contact POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(message, this);
            content.focus();
        }
        setVisible(message != null);
    }
}
