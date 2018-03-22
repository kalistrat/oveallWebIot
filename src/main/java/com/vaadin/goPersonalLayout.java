package com.vaadin;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.*;

/**
 * Created by kalistrat on 12.03.2018.
 */
public class goPersonalLayout extends HorizontalLayout {
    Button personalGoButton;
    TextField userLogin;
    Button personalCabButton;

    public goPersonalLayout(){

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
        userLogin.setWidth("150px");
        //userLogin.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);


        this.addComponent(personalCabButton);
        this.setSizeUndefined();

        personalCabButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                goPersonalLayout goLayout = (goPersonalLayout) clickEvent.getButton().getData();
                goLayout.removeAllComponents();
                goLayout.addComponent(userLogin);
                goLayout.addComponent(personalGoButton);
                goLayout.setSizeUndefined();
            }
        });

        personalGoButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String usrLog = userLogin.getValue();
                String usrUrl = getUserWebServerUrl(usrLog);
                if (!usrUrl.equals("")) {
                    getUI().getPage().setLocation(usrUrl);
                    goPersonalLayout goLayout = (goPersonalLayout) clickEvent.getButton().getData();
                    goLayout.removeAllComponents();
                    goLayout.addComponent(personalCabButton);
                } else {
                    Notification.show("Пользователя с логином " + usrLog +" не зарегистрировано!",
                            null,
                            Notification.Type.TRAY_NOTIFICATION);
                }
            }
        });
    }

    public String getUserWebServerUrl(String userLog){
        String userWebServerUrl = "";

        try {

            Class.forName(commonFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    commonFuctions.TJ_DB_URL
                    , commonFuctions.USER
                    , commonFuctions.PASS
            );

            CallableStatement Stmt = Con.prepareCall("{? = call getUserWebServerUrl(?)}");
            Stmt.registerOutParameter (1, Types.VARCHAR);
            Stmt.setString(2, userLog);
            Stmt.execute();
            userWebServerUrl = Stmt.getString(1);
            Con.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        return userWebServerUrl;
    }
}
