package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.view.comp.ControllPanel;
import com.ibm.icu.impl.CalendarAstronomer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

/**
 * Created by Janze on 20.01.2016.
 */
public class MainView extends HorizontalLayout implements View {

    private ControllPanel controllPane = new ControllPanel();

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.setSizeFull();
        this.addComponent(controllPane);

        /* Actual content */

        final VerticalLayout contentPane = new VerticalLayout();
        contentPane.setSizeFull();
        this.addComponent(contentPane);

        /* Headline */

        final HorizontalLayout headLine = new HorizontalLayout();
        headLine.setSizeFull();
        contentPane.addComponent(headLine);

        //TODO: No back-end connection!
        final Label titleLabel = new Label("Erfasste Zeiten");
        headLine.addComponent(titleLabel);
        headLine.setComponentAlignment(titleLabel, Alignment.MIDDLE_LEFT);

        //TODO: No back-end connection!
        final Label accountLabel = new Label("HIWI ry001");
        headLine.addComponent(accountLabel);
        headLine.setComponentAlignment(accountLabel, Alignment.MIDDLE_CENTER);

        //TODO: No back-end connection!
        final Label timeLabel = new Label("vom 02.01.2016");
        headLine.addComponent(timeLabel);
        headLine.setComponentAlignment(timeLabel, Alignment.MIDDLE_RIGHT);
    }
}
