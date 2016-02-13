package com.github.neunkasulle.chronocommand.view.forms;

import com.github.neunkasulle.chronocommand.model.TimeSheet;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;

/**
 * Created by Ming-Samsung on 2016/1/31.
 */
public class TimeSheetForm extends FormLayout {

    private Button exportPDF = new Button("PDF exportieren");
    private Button setOK = new Button("Als geprüft markieren");
    private Button fireIssue = new Button("Mängel melden");
    private Button cancel = new Button("Abbrechen");

    private TimeSheet object;

    private BeanFieldGroup<TimeSheet> formFieldBindings;

    public TimeSheet getCurrentFormObject() {
        return this.object;
    }

    public BeanFieldGroup<TimeSheet> getFormFieldBinding() {
        return this.formFieldBindings;
    }

    public TimeSheetForm(final Button.ClickListener exportPDFOperation,
                         final Button.ClickListener setOKOperation,
                         final Button.ClickListener  fireIssueOperation,
                         final Button.ClickListener cancelOperation) {
        this.exportPDF.addClickListener(exportPDFOperation);
        this.setOK.addClickListener(setOKOperation);
        this.fireIssue.addClickListener(fireIssueOperation);
        this.cancel.addClickListener(cancelOperation);

        addComponents(exportPDF, setOK, fireIssue, cancel);
    }

    public void edit(final TimeSheet object) {
        this.object = object;
        if (object != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(object, this);
            exportPDF.focus();
        }
        setVisible(object != null);
    }

}
