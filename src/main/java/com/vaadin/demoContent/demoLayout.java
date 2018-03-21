package com.vaadin.demoContent;

import com.vaadin.ui.VerticalLayout;

/**
 * Created by kalistrat on 21.03.2018.
 */
public class demoLayout extends VerticalLayout {
    String CurrentUsr = "k";
    tTreeContentLayout TreeContentUsr;

    public demoLayout(){

        tTree DeviceTree = new tTree(CurrentUsr,this);
        TreeContentUsr = new tTreeContentLayout(CurrentUsr,DeviceTree);
    }
}
