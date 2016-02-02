package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.TimeSheetControl;
import com.github.neunkasulle.chronocommand.model.Category;
import com.github.neunkasulle.chronocommand.model.TimeRecord;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by Ming-Samsung on 2016/1/31.
 */
public class TimeRecordForm extends FormLayout {

    private TextField beginningHour = new TextField("Begin (Stunde)");
    private TextField beginningMinute = new TextField("Begin (Minute)");
    private TextField endHour = new TextField("Ende (Stunde)");
    private TextField endMinute = new TextField("Ende (Minute)");

    private ComboBox categoryName = new ComboBox("Kategorie");
    private TextField description = new TextField("Tätigkeit");

    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");
    private Button cancel = new Button("Abbrechen");

    private TimeRecord object;

    private BeanFieldGroup<TimeRecord> formFieldBindings;

    public TimeRecord getCurrentFormObject() {
        return this.object;
    }

    public BeanFieldGroup<TimeRecord> getFormFieldBinding() {
        return this.formFieldBindings;
    }

    public TimeRecordForm(final Button.ClickListener saveOperation,
                          final Button.ClickListener deleteOperation,
                          final Button.ClickListener cancelOperation) {
        this.save.addClickListener(saveOperation);
        this.cancel.addClickListener(cancelOperation);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);

        setSizeUndefined();
        setMargin(true);

        final String timeFieldWidth = "50px";

        beginningHour.setWidth(timeFieldWidth);
        beginningMinute.setWidth(timeFieldWidth);
        endHour.setWidth(timeFieldWidth);
        endMinute.setWidth(timeFieldWidth);

        final List<Category> categoryList = TimeSheetControl.getInstance().getAllCategories();
        for (Category c : categoryList) {
            categoryName.addItem(c.getName());
        }

        final HorizontalLayout actions1 = new HorizontalLayout(save, delete);
        actions1.setSpacing(true);
        final HorizontalLayout actions2 = new HorizontalLayout(cancel);
        actions2.setSpacing(true);
        final HorizontalLayout begin = new HorizontalLayout(beginningHour, beginningMinute);
        begin.setSpacing(true);
        final HorizontalLayout end =  new HorizontalLayout(endHour, endMinute);
        end.setSpacing(true);
        addComponents(begin, end, categoryName, description, actions1, actions2);
    }

    void edit(TimeRecord record) {
        this.object = record;
        if (record != null) {
            // Bind the properties of the contact POJO to fiels in this form
            try {
                formFieldBindings = BeanFieldGroup.bindFieldsBuffered(record, this);
            } catch (Exception e) {
                beginningHour.setValue(e.getCause().getMessage());
            }
            beginningHour.focus();
        }
        setVisible(record != null);
    }

}
