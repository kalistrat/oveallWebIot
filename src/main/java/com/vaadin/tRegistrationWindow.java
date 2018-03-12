package com.vaadin;

import com.vaadin.data.Item;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by kalistrat on 12.03.2018.
 */
public class tRegistrationWindow extends Window {

    TextField LoginField;
    PasswordField PassWordField;
    PasswordField ConfirmPassWordField;
    TextField PhoneTextField;
    TextField MailTextField;


    Button BeginRegistraionButton;
    Button CancelButton;

    public tRegistrationWindow(){


        this.setIcon(FontAwesome.USER_PLUS);
        this.setCaption(" Добавление нового пользователя");

        LoginField = new TextField("Логин :");
        LoginField.setIcon(VaadinIcons.USER);
        LoginField.setNullRepresentation("");
        LoginField.setInputPrompt("Мнемоническое имя, содержащее латиницу и цифры от 7 до 50 символов (GlushkovVM1923)");

        PassWordField = new PasswordField("Пароль :");
        PassWordField.setIcon(VaadinIcons.KEY);
        ConfirmPassWordField = new PasswordField("Подтверждение пароля :");
        ConfirmPassWordField.setIcon(VaadinIcons.KEY_O);

        PhoneTextField = new TextField("Номер телефона :");
        PhoneTextField.setIcon(VaadinIcons.PHONE);
        PhoneTextField.setNullRepresentation("");
        PhoneTextField.setInputPrompt("Номер телефона 11 символов (79160000000)");

        MailTextField = new TextField("Адрес электронной почты :");
        MailTextField.setIcon(VaadinIcons.ENVELOPE);
        MailTextField.setNullRepresentation("");
        MailTextField.setInputPrompt("Имя почтового ящика с доменом до 150 символов (GlushkovVM@ussras.ru)");


        BeginRegistraionButton = new Button("Начать регистрацию");
        BeginRegistraionButton.setData(this);
        BeginRegistraionButton.addStyleName(ValoTheme.BUTTON_SMALL);


        CancelButton = new Button("Отменить");
        CancelButton.setData(this);
        CancelButton.addStyleName(ValoTheme.BUTTON_SMALL);
        CancelButton.setIcon(FontAwesome.HAND_STOP_O);

        CancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().removeWindow((tRegistrationWindow) clickEvent.getButton().getData());
            }
        });

        HorizontalLayout ButtonsLayout = new HorizontalLayout(
                BeginRegistraionButton
                ,CancelButton
        );

        ButtonsLayout.setSizeUndefined();
        ButtonsLayout.setSpacing(true);

        FormLayout regParamLayout = new FormLayout(
                LoginField
                ,PassWordField
                ,ConfirmPassWordField
                ,PhoneTextField
                ,MailTextField
        );

        regParamLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        regParamLayout.setWidth("520px");
        regParamLayout.addStyleName("FormFont");
        regParamLayout.setMargin(false);

        VerticalLayout MessageLayout = new VerticalLayout(
                regParamLayout
        );
        MessageLayout.setSpacing(true);
        MessageLayout.setWidth("620px");
        MessageLayout.setHeightUndefined();
        MessageLayout.setMargin(new MarginInfo(true,false,true,false));
        MessageLayout.setComponentAlignment(regParamLayout, Alignment.MIDDLE_CENTER);
        MessageLayout.addStyleName(ValoTheme.LAYOUT_CARD);

        VerticalLayout WindowContentLayout = new VerticalLayout(
                MessageLayout
                ,ButtonsLayout
        );
        WindowContentLayout.setSizeUndefined();
        WindowContentLayout.setSpacing(true);
        WindowContentLayout.setMargin(true);
        WindowContentLayout.setComponentAlignment(ButtonsLayout, Alignment.BOTTOM_CENTER);

        this.setContent(WindowContentLayout);
        this.setSizeUndefined();
        this.setModal(true);

    }
}
