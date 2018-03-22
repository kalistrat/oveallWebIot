package com.vaadin.demoContent;

import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by kalistrat on 21.03.2018.
 */
public class demoLayout extends VerticalLayout {
    String CurrentUsr = "k";
    tTreeContentLayout TreeContentUsr;

    public demoLayout(){

        tTree DeviceTree = new tTree(CurrentUsr,this);
        DeviceTree.addStyleName("CaptionTree");
        DeviceTree.setSizeFull();

        TreeContentUsr = new tTreeContentLayout(CurrentUsr,DeviceTree);

        HorizontalSplitPanel contentPanel = new HorizontalSplitPanel();
        contentPanel.setSplitPosition(35, Unit.PERCENTAGE);
        contentPanel.addComponent(DeviceTree);
        contentPanel.addComponent(TreeContentUsr);
        contentPanel.setSizeFull();

        addComponent(contentPanel);
        setMargin(true);

    }
}
