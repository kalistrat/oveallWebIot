package com.vaadin.demoContent.diagramContent;

import com.vaadin.shared.ui.JavaScriptComponentState;



/**
 * Created by kalistrat on 13.11.2017.
 */
public class DiagramState extends JavaScriptComponentState {
    private String coords;

    public String getCoords() {
        return coords;
    }

    public void setCoords(final String coords) {
        this.coords = coords;
    }

}
