package com.github.neunkasulle.chronocommand.view.forms;

import com.github.neunkasulle.chronocommand.model.TimeSheet;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;

/**
 * Created by Ming-Samsung on 2016/1/31.
 */
public class TimeSheetForm extends FormLayout {

    private Button showTimeRecords = new Button("Show Timerecords");
    private Button exportPDF = new Button("Export PDF");
    private Button setOK = new Button("Mark as ok");
    private Button fireIssue = new Button("Mark as not ok");
    private Button cancel = new Button("Cancel");

    private TimeSheet object;

    private BeanFieldGroup<TimeSheet> formFieldBindings;

    public TimeSheet getCurrentFormObject() {
        return this.object;
    }

    public BeanFieldGroup<TimeSheet> getFormFieldBinding() {
        return this.formFieldBindings;
    }

    public TimeSheetForm(final Button.ClickListener showTimeRecordsOperation,
                         final Button.ClickListener exportPDFOperation,
                         final Button.ClickListener setOKOperation,
                         final Button.ClickListener fireIssueOperation,
                         final Button.ClickListener cancelOperation) {
        this.showTimeRecords.addClickListener(showTimeRecordsOperation);
        this.exportPDF.addClickListener(exportPDFOperation);
        this.setOK.addClickListener(setOKOperation);
        this.fireIssue.addClickListener(fireIssueOperation);
        this.cancel.addClickListener(cancelOperation);

        addComponents(showTimeRecords, exportPDF, setOK, fireIssue, cancel);
    }

    public Button getExportPDFBtn() {
        return exportPDF;
    }

    public void edit(final TimeSheet object) {
        this.object = object;
        if (object != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(object, this);
            showTimeRecords.focus();
        }
        setVisible(object != null);
    }

}
