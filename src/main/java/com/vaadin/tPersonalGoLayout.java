package com.vaadin;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by kalistrat on 12.03.2018.
 */
public class tPersonalGoLayout extends HorizontalLayout {
    Button personalGoButton;
    TextField userLogin;
    Button personalCabButton;

    public tPersonalGoLayout(){

        personalGoButton = new Button();
        personalGoButton.setIcon(com.vaadin.icons.VaadinIcons.SIGN_IN);
        personalGoButton.addStyleName(ValoTheme.BUTTON_LINK);
        personalGoButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        personalGoButton.setData(this);

        personalCabButton = new Button("Личный кабинет");
        personalCabButton.setIcon(com.vaadin.icons.VaadinIcons.SIGN_IN);
        personalCabButton.addStyleName(ValoTheme.BUTTON_LINK);
        personalCabButton.addStyleName(ValoTheme.BUTTON_SMALL);
        personalCabButton.setData(this);

        userLogin = new TextField();
        userLogin.setNullRepresentation("");
        userLogin.setInputPrompt("Введите логин");
        userLogin.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        this.addComponent(personalCabButton);
        this.setSizeUndefined();

        personalCabButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                tPersonalGoLayout goLayout = (tPersonalGoLayout) clickEvent.getButton().getData();
                goLayout.removeAllComponents();
                goLayout.addComponent(userLogin);
                goLayout.addComponent(personalGoButton);
                goLayout.setSizeUndefined();
            }
        });

        personalGoButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getUI().getPage().setLocation("http://localhost:8080/personal");
                tPersonalGoLayout goLayout = (tPersonalGoLayout) clickEvent.getButton().getData();
                goLayout.removeAllComponents();
                goLayout.addComponent(personalCabButton);
            }
        });
    }
}
