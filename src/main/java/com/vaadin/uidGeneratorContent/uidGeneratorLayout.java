package com.vaadin.uidGeneratorContent;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.*;

/**
 * Created by kalistrat on 13.03.2018.
 */
public class uidGeneratorLayout extends VerticalLayout {
    TextField genUIDTextField;
    Button generateButton;
    NativeSelect uidTypeSelect;

    public uidGeneratorLayout(uidSearchJournalLayout journalLayout){

        Label Header = new Label();
        Header.setContentMode(ContentMode.HTML);
        Header.setValue(VaadinIcons.RANDOM.getHtml() + "  " + "Генерация UID для выпускаемых устройств");
        Header.addStyleName(ValoTheme.LABEL_COLORED);
        Header.addStyleName(ValoTheme.LABEL_SMALL);

        generateButton = new Button("Сгенерировать UID");
        generateButton.setIcon(VaadinIcons.RANDOM);
        generateButton.addStyleName(ValoTheme.BUTTON_SMALL);
        generateButton.addStyleName(ValoTheme.BUTTON_LINK);

        generateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String newUIDNum;
                String newUID = null;
                String uidPrefix;


                while (newUID == null) {

                    newUIDNum = RandomStringUtils.random(12, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");

                    if (uidTypeSelect.getValue().equals("Бридж")) {
                        uidPrefix = "BRI-";
                    } else if (uidTypeSelect.getValue().equals("Датчик")) {
                        uidPrefix = "SEN-";
                    } else {
                        uidPrefix = "RET-";
                    }

                    newUID = uidPrefix + newUIDNum;
                    if (fisUIDExists(newUID)) {
                        newUID = null;
                    }
                }


                addNewUID(newUID);
                genUIDTextField.setValue(newUID);
                journalLayout.UidJournalContainerRefresh("");
            }
        });

        genUIDTextField = new TextField();
        genUIDTextField.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        genUIDTextField.setEnabled(false);

        uidTypeSelect = new NativeSelect();
        uidTypeSelect.addItem("Бридж");
        uidTypeSelect.addItem("Датчик");
        uidTypeSelect.addItem("Ретранслятор");
        uidTypeSelect.setNullSelectionAllowed(false);
        uidTypeSelect.select("Датчик");
        uidTypeSelect.addStyleName("SelectFont");

        HorizontalLayout HeaderLayout = new HorizontalLayout(
                Header
        );
        HeaderLayout.setWidth("100%");
        HeaderLayout.setHeightUndefined();
        HeaderLayout.setComponentAlignment(Header, Alignment.MIDDLE_LEFT);

        HorizontalLayout ButtonLayout = new HorizontalLayout(
                uidTypeSelect
                ,generateButton
        );
        ButtonLayout.setWidth("300px");
        ButtonLayout.setHeightUndefined();
        ButtonLayout.setSpacing(true);

        HorizontalLayout GeneratorLayout = new HorizontalLayout(
                genUIDTextField
                ,ButtonLayout
        );
        GeneratorLayout.setSpacing(true);
        GeneratorLayout.setWidth("100%");
        GeneratorLayout.setHeightUndefined();
        GeneratorLayout.setComponentAlignment(genUIDTextField, Alignment.MIDDLE_LEFT);
        GeneratorLayout.setComponentAlignment(ButtonLayout, Alignment.MIDDLE_RIGHT);


        VerticalLayout ContentLayout = new VerticalLayout(
                HeaderLayout
                ,GeneratorLayout
        );
        ContentLayout.setSpacing(true);
        ContentLayout.setWidth("100%");
        ContentLayout.setHeightUndefined();

        this.addComponent(ContentLayout);
    }

    private void addNewUID(String iUID){
        try {

            Class.forName(com.vaadin.tUsefulFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    com.vaadin.tUsefulFuctions.DB_URL
                    , com.vaadin.tUsefulFuctions.USER
                    , com.vaadin.tUsefulFuctions.PASS
            );

            CallableStatement Stmt = Con.prepareCall("{call pAddNewUID(?)}");
            Stmt.setString(1, iUID);
            Stmt.execute();
            Con.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();

        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();

        }
    }

    private boolean fisUIDExists(String iUID){
        boolean isExist = false;

        try {

            Class.forName(com.vaadin.tUsefulFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    com.vaadin.tUsefulFuctions.DB_URL
                    , com.vaadin.tUsefulFuctions.USER
                    , com.vaadin.tUsefulFuctions.PASS
            );

            CallableStatement Stmt = Con.prepareCall("{? = call fisUIDExists(?)}");
            Stmt.registerOutParameter (1, Types.INTEGER);
            Stmt.setString(2, iUID);
            Stmt.execute();
            if (Stmt.getInt(1) == 1 ){
                isExist = true;
            }
            Con.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        return isExist;
    }

}
