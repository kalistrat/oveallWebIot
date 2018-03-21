package com.vaadin.demoContent.notificationContent;


import com.vaadin.commonFuctions;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;

/**
 * Created by kalistrat on 02.11.2017.
 */
public class tNotificationCriteriaField extends VerticalLayout {
    Double sValueFrom;
    Double sValueTill;
    Label prefixLabel;
    Label midfixLabel;
    public TextField valueFromField;
    public TextField valueTillField;
    boolean isInterval;

    public tNotificationCriteriaField(){

        isInterval = false;

        valueFromField = new TextField();
        valueFromField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        valueFromField.setInputPrompt("0.00");
        valueFromField.setWidth("70px");
        valueFromField.setHeight("20px");
        sValueFrom = null;
        sValueTill = null;

        valueTillField = new TextField();
        valueTillField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        valueTillField.setInputPrompt("0.00");
        valueTillField.setWidth("70px");
        valueTillField.setHeight("20px");

        prefixLabel = new Label();
        prefixLabel.addStyleName("CriteriaTextLabel");
        prefixLabel.setValue("c");

        midfixLabel = new Label();
        midfixLabel.addStyleName("CriteriaTextLabel");
        midfixLabel.setValue("по");

        reBuildLayout(isInterval);
    }

    public void setFieldsValues(String sValue){
        List<String> sValues = commonFuctions.GetListFromString(sValue,"|");
        if (sValues.size() == 2) {
            sValueFrom = commonFuctions.ParseDouble(sValues.get(0));
            sValueTill = commonFuctions.ParseDouble(sValues.get(1));
            valueFromField.setValue(String.valueOf(sValueFrom));
            valueTillField.setValue(String.valueOf(sValueTill));
            isInterval = true;
        } else {
            sValueFrom = commonFuctions.ParseDouble(sValue);
            valueFromField.setValue(String.valueOf(sValueFrom));
            isInterval =false;
        }
        reBuildLayout(isInterval);
    }

    public void reBuildLayout(boolean isIntvalue){
        isInterval = isIntvalue;
        this.removeAllComponents();
        if (isInterval) {
            HorizontalLayout fRow = new HorizontalLayout(prefixLabel,valueFromField);
            fRow.setMargin(false);
            fRow.setHeight("20px");
            //fRow.addStyleName(ValoTheme.LAYOUT_CARD);
            addComponent(fRow);
            HorizontalLayout sRow = new HorizontalLayout(midfixLabel,valueTillField);
            sRow.setMargin(false);
            fRow.setHeight("20px");
            //sRow.addStyleName(ValoTheme.LAYOUT_CARD);
            addComponent(sRow);
        } else {
            addComponent(valueFromField);
        }
    }


}
