package com.github.neunkasulle.chronocommand.view.comp;

import com.github.neunkasulle.chronocommand.control.LoginControl;
import com.github.neunkasulle.chronocommand.view.MainUI;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Ming-Samsung on 2016/1/29.
 */
public class ControllPanel extends VerticalLayout {


    public ControllPanel() {

        /* User information */

        final HorizontalLayout userInfo = new HorizontalLayout();
        userInfo.setSizeFull();
        userInfo.addComponent(new Label("Musterman"));
        userInfo.addComponent(new Label("HIWI"));
        this.addComponent(userInfo);

        /* Action links */

        final HorizontalLayout naviBar = new HorizontalLayout();
        naviBar.setSizeFull();
        this.addComponent(naviBar);

        final Button homeButton = new Button(new ThemeResource("img/home4.png"));
        homeButton .addClickListener(e -> {
            getUI().getNavigator().navigateTo(MainUI.MAINVIEW);
        });
        homeButton .setStyleName(BaseTheme.BUTTON_LINK);
        naviBar.addComponent(homeButton);

        final Button messageButton = new Button(new ThemeResource("img/black218.png"));
        messageButton.addClickListener(e -> {
            getUI().getNavigator().navigateTo(MainUI.MESSAGEVIEW);
        });
        messageButton.setStyleName(BaseTheme.BUTTON_LINK);
        naviBar.addComponent(messageButton);

        final Button logoutButton = new Button(new ThemeResource("img/logout13.png"));
        logoutButton .addClickListener(e -> {
            LoginControl.getInstance().logout();
            getUI().getNavigator().navigateTo(MainUI.LOGINVIEW);
            Page.getCurrent().reload();
        });
        logoutButton.setStyleName(BaseTheme.BUTTON_LINK);
        naviBar.addComponent(logoutButton);

        /* Date picker */

        final InlineDateField field = new InlineDateField();
        field.setValue(new Date());
        field.setImmediate(true);
        field.setTimeZone(TimeZone.getTimeZone("UTC"));
        field.setLocale(Locale.US);
        field.setResolution(Resolution.MINUTE);
        field.addValueChangeListener(e -> Notification.show("Value changed:",
                String.valueOf(e.getProperty().getValue()),
                Notification.Type.TRAY_NOTIFICATION));
        this.addComponent(field);

        /* Betreuer */

        final HorizontalLayout supervisorPane = new HorizontalLayout();
        supervisorPane.setSizeFull();
        this.addComponent(supervisorPane);

        supervisorPane.addComponent(new Label("Betreuer"));
        supervisorPane.addComponent(new Label("Betreuer1"));

         /* Other actions  */

        this.addComponent(new Label("neue Zeiterfassung"));
        this.addComponent(new Label("Stundenzettel abgeben"));

        /* Combo box */

        // Creates a new combobox using an existing container
        final ComboBox letterSelection = new ComboBox(null, Arrays.asList("02.01.2016","01.01.2016"));
        letterSelection.setInputPrompt("Bitte Stundenzettel auswählen");
        letterSelection.setNullSelectionAllowed(false);
        this.addComponent(letterSelection);

    }
}
