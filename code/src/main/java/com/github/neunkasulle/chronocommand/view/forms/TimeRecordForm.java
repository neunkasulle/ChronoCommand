package com.github.neunkasulle.chronocommand.view.forms;

import com.github.neunkasulle.chronocommand.control.TimeSheetControl;
import com.github.neunkasulle.chronocommand.model.TimeRecord;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by Ming-Samsung on 2016/1/31.
 */
public class TimeRecordForm extends FormLayout {

    private TextField beginningHour = new TextField("Beginning (hour)");
    private TextField beginningMinute = new TextField("Beginning (minute)");
    private TextField endHour = new TextField("End (hour)");
    private TextField endMinute = new TextField("End (minute)");

    private ComboBox category = new ComboBox("Project");
    private TextField description = new TextField("Description");

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button cancel = new Button("Cancel");

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
        this.delete.addClickListener(deleteOperation);
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

        category.addItems(TimeSheetControl.getInstance().getAllCategories());

        final HorizontalLayout actions1 = new HorizontalLayout(save, delete);
        actions1.setSpacing(true);
        final HorizontalLayout actions2 = new HorizontalLayout(cancel);
        actions2.setSpacing(true);
        final HorizontalLayout begin = new HorizontalLayout(beginningHour, beginningMinute);
        begin.setSpacing(true);
        final HorizontalLayout end =  new HorizontalLayout(endHour, endMinute);
        end.setSpacing(true);
        addComponents(begin, end, category, description, actions1, actions2);
    }

    public void edit(TimeRecord record) {
        this.object = record;
        if (record != null) {
            if (record.getEnding() == null) {
                setVisible(false);
                return;
            }
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(record, this);
            beginningHour.focus();
        }
        setVisible(record != null);
    }

}
