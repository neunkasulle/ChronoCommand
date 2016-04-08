package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.LoginControl;
import com.github.neunkasulle.chronocommand.control.TimeSheetControl;
import com.github.neunkasulle.chronocommand.model.Category;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.time.*;
import java.util.Date;

public class NewTimeRecordView extends BaseView {
    @Override
    protected void enterTemplate(ViewChangeListener.ViewChangeEvent event, Layout contentPane) {
        Label status = new Label();
        status.setVisible(false);

        DateField begin = new DateField("Begin");
        begin.setDateFormat("dd.MM.yyyy HH:mm");
        begin.setValue(new Date());
        begin.setResolution(Resolution.MINUTE);
        begin.addValueChangeListener(event2 -> status.setVisible(false));

        DateField end = new DateField("End");
        end.setDateFormat("dd.MM.yyyy HH:mm");
        end.setValue(new Date());
        end.setResolution(Resolution.MINUTE);
        end.addValueChangeListener(event2 -> status.setVisible(false));

        ComboBox project = new ComboBox("Project");
        project.addItems(TimeSheetControl.getInstance().getAllCategories());
        project.addValueChangeListener(event2 -> status.setVisible(false));

        TextField description = new TextField("Description");
        description.addValueChangeListener(event2 -> status.setVisible(false));

        Button save = new Button("Save");

        contentPane.addComponents(begin, end, project, description, save, status);

        save.addClickListener(event1 -> {
            try {
                TimeSheetControl.getInstance().addTimeToSheet(LocalDateTime.ofInstant(begin.getValue().toInstant(), ZoneOffset.systemDefault()),
                        LocalDateTime.ofInstant(end.getValue().toInstant(), ZoneOffset.systemDefault()), (Category) project.getValue(),
                        description.getValue(), LoginControl.getInstance().getCurrentUser());
                status.setVisible(true);
                status.setValue("Saved");
                status.setStyleName(ValoTheme.LABEL_SUCCESS);
            } catch (ChronoCommandException e) {
                Notification.show("Failed to save timerecord: " + e.getReason().toString(), Notification.Type.WARNING_MESSAGE);
                status.setVisible(true);
                status.setValue("Failed to save");
                status.setStyleName(ValoTheme.LABEL_FAILURE);
            }
        });
    }
}
