package com.vaadin.uidGeneratorTabContent;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by kalistrat on 13.03.2018.
 */
public class uidGeneratorTabContent extends VerticalLayout {
    uidGeneratorLayout genLayout;
    uidSearchJournalLayout jouLayout;

    public uidGeneratorTabContent(){


        jouLayout = new uidSearchJournalLayout();
        genLayout = new uidGeneratorLayout(jouLayout);

        this.addComponent(genLayout);
        this.addComponent(jouLayout);
        this.setSpacing(true);
        this.addStyleName(ValoTheme.LAYOUT_WELL);
        this.setMargin(true);

        this.setWidth("1000px");
        this.setHeightUndefined();

    }
}
