package com.vaadin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.uidGeneratorContent.uidGeneratorTabContent;

/**
 * Created by SemenovNA on 02.08.2016.
 */
public class tGuestView extends CustomComponent implements View {

    public static final String NAME = "Guest";
    Button regWindowButton;
    //Button personalCabButton;
    Button questionWindowButton;
    tPersonalGoLayout personalGoLayout;
    private TabSheet overallWebSiteSheet;


    public tGuestView(){

        ThemeResource resource = new ThemeResource("TJAY.png");

        Image logotype = new Image(null,resource);
        logotype.setSizeUndefined();
        logotype.setWidth("907px");
        logotype.setHeight("100px");

        regWindowButton = new Button("Регистрация");
        regWindowButton.setIcon(FontAwesome.USER_PLUS);
        regWindowButton.addStyleName(ValoTheme.BUTTON_LINK);
        regWindowButton.addStyleName(ValoTheme.BUTTON_SMALL);

        regWindowButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().addWindow(new tRegistrationWindow());
            }
        });

        questionWindowButton = new Button("Вопросы и предложения");
        questionWindowButton.setIcon(FontAwesome.QUESTION_CIRCLE);
        questionWindowButton.addStyleName(ValoTheme.BUTTON_LINK);
        questionWindowButton.addStyleName(ValoTheme.BUTTON_SMALL);

        personalGoLayout = new tPersonalGoLayout();

        HorizontalLayout topRightButtonLayout = new HorizontalLayout();
        topRightButtonLayout.setSpacing(true);
        topRightButtonLayout.addComponent(regWindowButton);
        topRightButtonLayout.addComponent(personalGoLayout);
        topRightButtonLayout.setSizeUndefined();

        HorizontalLayout topLeftButtonLayout = new HorizontalLayout();
        topLeftButtonLayout.setSpacing(true);
        topLeftButtonLayout.addComponent(questionWindowButton);
        topLeftButtonLayout.setSizeUndefined();

        HorizontalLayout topButtonLayout = new HorizontalLayout();
        topButtonLayout.setSpacing(true);
        topButtonLayout.addComponent(topLeftButtonLayout);
        topButtonLayout.addComponent(topRightButtonLayout);
        topButtonLayout.setComponentAlignment(topLeftButtonLayout,Alignment.TOP_LEFT);
        topButtonLayout.setComponentAlignment(topRightButtonLayout,Alignment.TOP_RIGHT);
        topButtonLayout.setWidth("100%");
        topButtonLayout.setHeightUndefined();

        uidGeneratorTabContent genLayout = new uidGeneratorTabContent();
        VerticalLayout genTabCont = new VerticalLayout(
                new Label()
                ,genLayout
        );
        genTabCont.setSizeFull();
        genTabCont.setComponentAlignment(genLayout,Alignment.MIDDLE_CENTER);

        ThemeResource resource1 = new ThemeResource("FIRST_PAGE.png");

        Image commonImage = new Image(null,resource1);
        commonImage.setWidth("700px");
        commonImage.setHeight("467px");

        VerticalLayout commonImgLay = new VerticalLayout(
                commonImage
        );
        commonImgLay.setWidth("1000px");
        commonImgLay.setHeight("667px");
        commonImgLay.setComponentAlignment(commonImage,Alignment.MIDDLE_CENTER);
        commonImgLay.addStyleName(ValoTheme.LAYOUT_WELL);

        VerticalLayout commonTabCont = new VerticalLayout(
                new Label()
                ,commonImgLay
        );
        commonTabCont.setSizeFull();
        commonTabCont.setComponentAlignment(commonImgLay,Alignment.MIDDLE_CENTER);

        overallWebSiteSheet = new TabSheet();
        overallWebSiteSheet.addTab(commonTabCont, "Общее", com.vaadin.icons.VaadinIcons.HOME);
        overallWebSiteSheet.addTab(new Label("Продукция"), "Продукция", com.vaadin.icons.VaadinIcons.FACTORY);
        overallWebSiteSheet.addTab(new Label("Подключение"), "Подключение", com.vaadin.icons.VaadinIcons.CONNECT);
        overallWebSiteSheet.addTab(new Label("Демо"), "Демо", com.vaadin.icons.VaadinIcons.CHART);
        overallWebSiteSheet.addTab(genTabCont, "Генерация UID", com.vaadin.icons.VaadinIcons.RANDOM);

        overallWebSiteSheet.setWidth("100%");
        overallWebSiteSheet.setHeightUndefined();

        VerticalLayout logoLayout = new VerticalLayout(
          logotype
        );
        logoLayout.setComponentAlignment(logotype,Alignment.MIDDLE_CENTER);
        logoLayout.setHeightUndefined();
        logoLayout.setWidth("100%");

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.addComponent(topButtonLayout);
        contentLayout.addComponent(logoLayout);
        contentLayout.addComponent(overallWebSiteSheet);
        contentLayout.setComponentAlignment(overallWebSiteSheet,Alignment.MIDDLE_CENTER);

        setCompositionRoot(contentLayout);
    }

    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
