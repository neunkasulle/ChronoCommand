package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.TimeSheetControl;
import com.github.neunkasulle.chronocommand.model.Category;
import com.github.neunkasulle.chronocommand.model.CategoryDAO;
import com.github.neunkasulle.chronocommand.model.Session;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Created by Janze on 20.01.2016.
 */
public abstract class BaseView extends VerticalLayout implements View {
    ComboBox categoryDropdown;
    TextField description;
    Button start;
    Button stop;

    public BaseView(Session session) {
    }

    public BaseView() {
    }

    public Session getSessionID() {

        return null;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isPermitted("proletarier")) {
            //DEBUG
            //return;
        }

        HorizontalLayout header = new HorizontalLayout();
        header.setSpacing(true);
        header.setSizeUndefined();
        addComponent(header);
        setComponentAlignment(header, Alignment.TOP_CENTER);

        /*Label categoryLabel = new Label("Category:");
        header.addComponent(categoryLabel);
        header.setComponentAlignment(categoryLabel, Alignment.MIDDLE_CENTER);*/

        categoryDropdown = new ComboBox();
        categoryDropdown.setInputPrompt("Category");
        for (Category category : CategoryDAO.getInstance().getAllCategories()) {
            categoryDropdown.addItem(category.getName());
        }
        header.addComponent(categoryDropdown);

        /*Label descriptionLabel = new Label("Description:");
        header.addComponent(descriptionLabel);
        header.setComponentAlignment(descriptionLabel, Alignment.MIDDLE_CENTER);*/

        description = new TextField();
        description.setInputPrompt("Description");
        header.addComponent(description);

        start = new Button("Start");
        header.addComponent(start);
        start.addClickListener(event -> {
            start.setEnabled(false);
            stop.setEnabled(true);
            // TODO implement
        });

        stop = new Button("Stop");
        stop.setEnabled(false);
        header.addComponent(stop);
        stop.addClickListener(event -> {
            stop.setEnabled(false);
            start.setEnabled(true);
            // TODO implement
        });
    }

    public void logoutClicked() {

    }

    public void menuClicked() {

    }

    public void inboxClicked() {

    }

    public void showErrorMessage(String errorCode) {

    }

    public void showErrorMessage() {

    }

    public void buttonClick(Button.ClickEvent event) {

    }

    public void init(VaadinRequest request) {

    }
}
